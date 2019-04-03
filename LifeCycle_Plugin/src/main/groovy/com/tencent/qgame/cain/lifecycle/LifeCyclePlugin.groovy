package com.tencent.qgame.cain.lifecycle

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Plugin
import org.gradle.api.Project

class LifeCyclePlugin extends Transform implements Plugin<Project> {
    private static final String TAG = "LifeCyclePlugin"

    @Override
    void apply(Project project) {
        println("--------------------LifeCyclePlugin#apply()#project: " + project.extensions.toString())
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
        Collection<TransformInput> inputs = transformInvocation.inputs
        TransformOutputProvider outputProvider = transformInvocation.outputProvider

        if (outputProvider != null) {// 删除之前的输出
            outputProvider.deleteAll()
        }

        // 遍历imupts
        inputs.each { TransformInput input ->
            // 遍历处理directoryInputs
            input.directoryInputs.each { DirectoryInput directoryInput ->
                // todo handleDirectoryInput
                handleDirectoryInput(directoryInput, outputProvider)
            }
            // 遍历处理jarInputs
            input.jarInputs.each { JarInput jarInput ->
                // todo handleJarInput
                handleJarInput(jarInput, outputProvider)
            }
        }

        println("--------------- LifecyclePlugin visit end ---------------")
        def endTime = System.currentTimeMillis()
        def cost = (endTime - startTime) / 1000
        println("LifecyclePlugin cost： $cost s")

    }


    private void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        if (directoryInput.file.isDirectory()) {
            directoryInput.file.eachDirRecurse { File file ->
                def name = file.name
                def handled = checkClassFile(name)
                println("handle: $handled")
                if (handled)
            }
        }
    }

    private void handleJarInput(JarInput jarInput, TransformOutputProvider outputProvider) {

    }

    private boolean checkClassFile(String classFileName) {
        println("classFileName: $classFileName")
        return (classFileName.endsWith(".class") && !classFileName.startsWith("R\$")) &&
                !classFileName.equals("R.class") && // 排除R文件
                !classFileName.equals("BuildConfig.class") && // 排除配置文件
                classFileName.equals("android/support/v4/app/FragmentActivity.class")
    }
}