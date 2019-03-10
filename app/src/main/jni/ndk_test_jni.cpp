//
// Created by 姜瑜 on 2019/3/10.
//
#include <jni.h>
#include <cstdio>
#include "lame.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_cain_tencent_com_androidexercisedemo_NDKBridge_getStr(JNIEnv *env, jobject instance) {
    printf("invoke getStr function in c++ ! \n");
    return env->NewStringUTF("Hello! I am from C++ though JNI.");
}


extern "C"
JNIEXPORT jstring JNICALL
Java_cain_tencent_com_androidexercisedemo_NDKBridge_getLameVersion(JNIEnv *env, jobject instance) {

    return env->NewStringUTF(get_lame_version());
}

extern "C"
JNIEXPORT void JNICALL
Java_cain_tencent_com_androidexercisedemo_NDKBridge_wav2Mp3(JNIEnv *env, jobject instance,
                                                            jstring wav_, jstring map3_) {
    const char *wav = env->GetStringUTFChars(wav_, 0);
    const char *map3 = env->GetStringUTFChars(map3_, 0);

    env->ReleaseStringUTFChars(wav_, wav);
    env->ReleaseStringUTFChars(map3_, map3);
}