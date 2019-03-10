package cain.tencent.com.androidexercisedemo;

public class NDKBridge {
    static {
        // 实现jni库
        System.loadLibrary("ndk_test_jni");
    }

    // 实现native方法
    public native String getStr();
}
