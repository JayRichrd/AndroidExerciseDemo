package com.tencent.qgame.cain.lifecycle

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry


class MyLifeCyclePlugin extends Transform implements Plugin<Project> {
    private static final String TAG = "MyLifeCyclePlugin"

    @Override
    void apply(Project project) {
        // 注册Transform
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(this)
    }

    @Override
    String getName() {
        return TAG
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        println("--------------- LifecyclePlugin visit start --------------- ")
        def startTime = System.currentTimeMillis()
        // 输入
        Collection<TransformInput> inputs = transformInvocation.inputs
        // 输出
        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        // 删除之前的输出，使用变换后的输出
        if (outputProvider != null) {
            outputProvider.deleteAll()
        }
        // 遍历输入
        inputs.each { TransformInput input ->
            // 遍历处理directoryInputs，针对源码
            input.directoryInputs.each { DirectoryInput directoryInput ->
                handleDirectoryInput(directoryInput, outputProvider)
            }
            // 遍历处理jarInputs，针对jar包
            input.jarInputs.each { JarInput jarInput ->
                handleJarInput(jarInput, outputProvider)
            }
        }
        println("--------------- LifecyclePlugin visit end ---------------")
        def endTime = System.currentTimeMillis()
        def cost = (endTime - startTime) / 1000
        println("--------------- LifecyclePlugin cost： $cost s")

    }

    /**
     * 处理源码
     * @param directoryInput Transform得到的源码输入
     * @param outputProvider Transform的输出
     */
    private
    static void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        if (directoryInput.file.isDirectory()) {
            // 处理目录下的所有文件
            directoryInput.file.eachDirRecurse { File file ->
                def name = file.name
                if (checkClassFile(name)) {
                    println '----------- deal with "class" file <' + name + '> -----------'
                    ClassReader classReader = new ClassReader(file.bytes)
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    ClassVisitor classVisitor = new MyLifecycleClassVisitor(classWriter)
                    // 给权限
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    // 重写文件
                    byte[] code = classWriter.toByteArray()
                    FileOutputStream fileOutputStream = new FileOutputStream(file.parentFile.absolutePath + File.separator + name)
                    fileOutputStream.write(code)
                    fileOutputStream.close()
                }
                // 重新定义输出文件
                def destinationFile = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, destinationFile)
            }
        }
    }

    /**
     * 处理jar包
     * @param jarInput Transform得到的jar包输入
     * @param outputProvider Transform的输出
     */
    private static void handleJarInput(JarInput jarInput, TransformOutputProvider outputProvider) {
        if (jarInput.file.getAbsolutePath().endsWith(".jar")) {
            def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
            // 重命名输出文件，因为可能同名被覆盖
            def jarName = jarInput.name
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4)
            }

            JarFile jarFile = new JarFile(jarInput.file)
            Enumeration<JarEntry> enumeration = jarFile.entries()
            File tempFile = new File(jarInput.file.getParent() + File.separator + "classes_temp.jar")
            if (tempFile.exists()) {//避免上次的缓存被重复插入
                tempFile.delete()
            }
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tempFile))
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = enumeration.nextElement()
                String entryName = jarEntry.getName()
                ZipEntry zipEntry = new ZipEntry(entryName)
                InputStream inputStream = jarFile.getInputStream(jarEntry)
                //插桩class
                if (checkClassFile(entryName)) {
                    //class文件处理
                    println '----------- deal with "jar" class file <' + entryName + '> -----------'
                    jarOutputStream.putNextEntry(zipEntry)
                    ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream))
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    ClassVisitor classVisitor = new MyLifecycleClassVisitor(classWriter)
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    byte[] code = classWriter.toByteArray()
                    jarOutputStream.write(code)
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                jarOutputStream.closeEntry()
            }
            //结束
            jarOutputStream.close()
            jarFile.close()
            // 重新定义输出文件
            def destinationFile = outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
            FileUtils.copyFile(tempFile, destinationFile)
            tempFile.delete()
        }
    }

    /**
     * 判断类文件
     * @param classFileName 输入的类名
     * @return
     */
    private static boolean checkClassFile(String classFileName) {
        return (classFileName.endsWith(".class") && !classFileName.startsWith("R\$")) &&
                classFileName != "R.class" && // 排除R文件
                classFileName != "BuildConfig.class" && // 排除配置文件
                classFileName == "android/support/v4/app/FragmentActivity.class"
    }
}