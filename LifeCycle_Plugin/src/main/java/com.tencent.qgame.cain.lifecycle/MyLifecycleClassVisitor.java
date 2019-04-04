package com.tencent.qgame.cain.lifecycle;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author cainjiang
 */
public class MyLifecycleClassVisitor extends ClassVisitor implements Opcodes {

    private String mClassName;
    public static final String TARGET_CLASS_NAME = "android/support/v4/app/FragmentActivity";
    public static final String METHOD_ON_CREATE = "onCreate";
    public static final String METHOD_ON_DESTROY = "onDestroy";

    public MyLifecycleClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        //匹配FragmentActivity
        if (TARGET_CLASS_NAME.equals(this.mClassName)) {
            if (METHOD_ON_CREATE.equals(name)) {//匹配onCreate()方法
                //处理onCreate
                System.out.println("MyLifecycleClassVisitor#visitMethod()#access: " + access + ", name: " + name + ", desc: " + desc + ", signature: " + signature + ", exceptions: " + exceptions + ", change method " + name);
                return new MyLifecycleOnCreateMethodVisitor(mv);
            } else if (METHOD_ON_DESTROY.equals(name)) {//匹配onDestroy()方法
                //处理onDestroy
                System.out.println("MyLifecycleClassVisitor#visitMethod()#access: " + access + ", name: " + name + ", desc: " + desc + ", signature: " + signature + ", exceptions: " + exceptions + ", change method " + name);
                return new MyLifecycleOnDestroyMethodVisitor(mv);
            }
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
