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


/**
 * wav装换换成map3
 */
extern "C"
JNIEXPORT void JNICALL
Java_cain_tencent_com_androidexercisedemo_NDKBridge_wav2Mp3(JNIEnv *env, jobject instance,
                                                            jstring wav_, jstring map3_,
                                                            jint inSamplerate) {
    const char *wav = env->GetStringUTFChars(wav_, 0);
    const char *map3 = env->GetStringUTFChars(map3_, 0);
    // 1.初始化lame的编码器
    lame_t lameConvert = lame_init();
    // 单声道
    int channel = 1;
    // 2.设置lame mp3编码的采样率
    lame_set_in_samplerate(lameConvert, inSamplerate);
    lame_set_out_samplerate(lameConvert, inSamplerate);
    lame_set_num_channels(lameConvert, channel);
    // 3.设置mp3的编码方式
    lame_set_VBR(lameConvert, vbr_default);
    lame_init_params(lameConvert);
    // 4.打开wav、mp3文件
    FILE *fwav = fopen(wav, "rb");
    fseek(fwav, 4 * 1024, SEEK_CUR);
    FILE *fmp3 = fopen(map3, "wb+");

    const int SIZE = (inSamplerate / 20) + 7200;
    // 读写的buffer
    short int wav_buffer[SIZE * channel];
    unsigned char map3_buffer[SIZE];

    // 读了多少次和写了多少次
    int read, write;
    // 当前读的wav文件的byte数目
    int total = 0;
    do {
        read = fread(wav_buffer, sizeof(short int) * channel, SIZE, fwav);
        total += read * sizeof(short int) * channel;
        if (read != 0) {
            write = lame_encode_buffer(lameConvert, wav_buffer, NULL, read, map3_buffer, SIZE);
        } else {
            write = lame_encode_flush(lameConvert, map3_buffer, SIZE);
        }
        fwrite(map3_buffer, sizeof(unsigned char), write, fmp3);

    } while (read != 0);

    lame_mp3_tags_fid(lameConvert, fmp3);

    // 5.关闭/释放资源和文件
    lame_close(lameConvert);
    fclose(fmp3);
    fclose(fwav);
    env->ReleaseStringUTFChars(wav_, wav);
    env->ReleaseStringUTFChars(map3_, map3);
}