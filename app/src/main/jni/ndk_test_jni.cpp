//
// Created by 姜瑜 on 2019/3/10.
//
#include <jni.h>
#include <cstdio>

extern "C"
JNIEXPORT jstring JNICALL
Java_cain_tencent_com_androidexercisedemo_NDKBridge_getStr(JNIEnv *env, jobject instance) {
    printf("invoke getStr function in c++ ! \n");
    return env->NewStringUTF("Hello! I am from C++ though JNI.");
}



