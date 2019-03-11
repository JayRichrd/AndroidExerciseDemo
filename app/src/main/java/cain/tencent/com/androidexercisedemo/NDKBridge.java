package cain.tencent.com.androidexercisedemo;

public class NDKBridge {
    static {
        // 实现jni库
        System.loadLibrary("ndk_test_jni");
    }

    // 实现native方法
    public native String getStr();

    // 获取lame版本号
    public native String getLameVersion();

    // 将wav文件转换成mp3
    public native void wav2Mp3(String wav, String map3, int inSamplerate);
}
