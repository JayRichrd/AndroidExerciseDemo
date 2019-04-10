//
// Created by 姜瑜 on 2019/3/10.
//
#include <jni.h>
#include <string>

extern "C" {// 必须添加这个，否则会报很多undefined reference错误

#include <cstdio>
#include "lame/lame.h"
//封装格式处理
#include <libavformat/avformat.h>
#include <android/native_window_jni.h>
#include <libavfilter/avfilter.h>
#include <libavcodec/avcodec.h>
//封装格式处理
#include <libavformat/avformat.h>
//像素处理
#include <libswscale/swscale.h>
#include <unistd.h>

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

/**
 * 解码视频播,渲染播放
 */
extern "C"
JNIEXPORT void JNICALL
Java_cain_tencent_com_androidexercisedemo_ffmpeg_FFVideoPlayer_render(JNIEnv *env, jobject instance,
                                                                      jstring url_,
                                                                      jobject surface) {
    const char *url = env->GetStringUTFChars(url_, 0);
    /**
     * 注册
     * 在4.0之后已经过期，可以直接忽略调用这个函数
     */
//    av_register_all();

    // 打开地址并获取里面的内容
    // avFormatContext是内容的一个上下文
    AVFormatContext *avFormatContext = avformat_alloc_context();
    avformat_open_input(&avFormatContext, url, NULL, NULL);
    avformat_find_stream_info(avFormatContext, NULL);

    // 找出视频流
    int video_index = -1;
    for (int i = 0; i < avFormatContext->nb_streams; ++i) {
//        if (avFormatContext->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO) {
//            video_index = i;
//        }
        if (avFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            video_index = i;
        }
    }

    /**
     * 下面将进行解码、转换、绘制
     */
    // 获取解码器上下文
    AVCodecContext *avCodecContext = avFormatContext->streams[video_index]->codec;
    // 获取解码器
    AVCodec *avCodec = avcodec_find_decoder(avCodecContext->codec_id);
    // 打开解码器
    if (avcodec_open2(avCodecContext, avCodec, NULL) < 0) { //打开失败直接返回
        return;
    }
    /**
     * 申请AVPacket和AVFrame
     * AVPacket的作用是保存解码之前的数据和一些附加信息，例如显示时间戳(pts)、解码时间戳(dts)、数据时长和所在媒体流的索引等
     * AVFrame的作用是存放解码过后的数据
     */
    AVPacket *avPacket = static_cast<AVPacket *>(av_malloc(sizeof(AVPacket)));
    av_init_packet(avPacket);
    /**
     * 分配一个AVFrame结构体
     * AVFrame结构体一般用于存储原始数,指向解码后的原始帧
     */
    AVFrame *avFrame = av_frame_alloc();
    /**
     * 分配一个AVFrame结构体，指向存放转换成rgb后的帧
     */
    AVFrame *rgb_frame = av_frame_alloc();
    /**
     * rgb_frame是一个缓存区域，需要设置
     * 缓存区
     */
    uint8_t *out_buffer = static_cast<uint8_t *>(av_malloc(
            avpicture_get_size(AV_PIX_FMT_RGBA, avCodecContext->width, avCodecContext->height)));
    /**
     * 与缓存区关联
     * 设置rgb_frame缓存区
     */
    avpicture_fill((AVPicture *) rgb_frame, out_buffer, AV_PIX_FMT_RGBA, avCodecContext->width,
                   avCodecContext->height);
    /**
     * 需要一个ANativeWindow来进行原生绘制
     */
    ANativeWindow *pANativeWindow = ANativeWindow_fromSurface(env, surface);
    if (pANativeWindow == 0) { // 获取native window失败直接返回
        return;
    }

    SwsContext *swsContext = sws_getContext(avCodecContext->width, avCodecContext->height,
                                            avCodecContext->pix_fmt, avCodecContext->width,
                                            avCodecContext->height, AV_PIX_FMT_RGBA, SWS_BICUBIC,
                                            NULL, NULL, NULL);
    // 视频缓冲区
    ANativeWindow_Buffer nativeWindow_outBuffer;

    /**
     * 开始解码
     */
    int frameCount;
    while (av_read_frame(avFormatContext, avPacket) >= 0) {
        if (avPacket->stream_index == video_index) {
            avcodec_decode_video2(avCodecContext, avFrame, &frameCount, avPacket);
            if (frameCount) {
                ANativeWindow_setBuffersGeometry(pANativeWindow, avCodecContext->width,
                                                 avCodecContext->height, WINDOW_FORMAT_RGBA_8888);
                /**
                 * 上锁
                 */
                ANativeWindow_lock(pANativeWindow, &nativeWindow_outBuffer, NULL);
                /**
                 * 转换成RGB格式
                 */
                sws_scale(swsContext, (const uint8_t *const *) avFrame->data, avFrame->linesize, 0,
                          avFrame->height, rgb_frame->data, rgb_frame->linesize);
                uint8_t *dst = static_cast<uint8_t *>(nativeWindow_outBuffer.bits);
                int destStride = nativeWindow_outBuffer.stride * 4;
                uint8_t *src = rgb_frame->data[0];
                int srcStride = rgb_frame->linesize[0];
                for (int i = 0; i < avCodecContext->height; i++) {
                    memcpy(dst + i * destStride, src + i * srcStride, srcStride);
                }
                ANativeWindow_unlockAndPost(pANativeWindow);

            }
        }
        av_free_packet(avPacket);
    }

    ANativeWindow_release(pANativeWindow);
    av_frame_free(&avFrame);
    av_frame_free(&rgb_frame);
    avcodec_close(avCodecContext);
    avformat_free_context(avFormatContext);

    env->ReleaseStringUTFChars(url_, url);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_cain_tencent_com_androidexercisedemo_ffmpeg_FFMpegActivity_avFilterInfo(JNIEnv *env,
                                                                             jobject instance) {
    char info[40000] = {0};
//    avfilter_register_all();
    AVFilter *f_temp = (AVFilter *) avfilter_next(NULL);
    while (f_temp != NULL) {
        sprintf(info, "%s%s\n", info, f_temp->name);
        f_temp = f_temp->next;
    }
    return env->NewStringUTF(info);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_cain_tencent_com_androidexercisedemo_ffmpeg_FFMpegActivity_avCodecInfo(JNIEnv *env,
                                                                            jobject instance) {
    char info[40000] = {0};
//    av_register_all();
    AVCodec *c_temp = av_codec_next(NULL);
    while (c_temp != NULL) {
        if (c_temp->decode != NULL) {
            sprintf(info, "%sdecode: ", info);
        } else {
            sprintf(info, "%sencode: ", info);
        }
        switch (c_temp->type) {
            case AVMEDIA_TYPE_VIDEO:
                sprintf(info, "%s(video): ", info);
                break;
            case AVMEDIA_TYPE_AUDIO:
                sprintf(info, "%s(audio): ", info);
                break;
            default:
                sprintf(info, "%s(other): ", info);
                break;
        }
        sprintf(info, "%s[%10s]\n", info, c_temp->name);
        c_temp = c_temp->next;
    }

    return env->NewStringUTF(info);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_cain_tencent_com_androidexercisedemo_ffmpeg_FFMpegActivity_avFormatInfo(JNIEnv *env,
                                                                             jobject instance) {
    char info[40000] = {0};
//    av_register_all();
    AVInputFormat *if_temp = av_iformat_next(NULL);
    AVOutputFormat *of_temp = av_oformat_next(NULL);
    while (if_temp != NULL) {
        sprintf(info, "%sInput: %s\n", info, if_temp->name);
        if_temp = if_temp->next;
    }
    while (of_temp != NULL) {
        sprintf(info, "%sOutput: %s\n", info, of_temp->name);
        of_temp = of_temp->next;
    }
    return env->NewStringUTF(info);
}


extern "C"
JNIEXPORT jstring JNICALL
Java_cain_tencent_com_androidexercisedemo_ffmpeg_FFMpegActivity_urlProtocolInfo(JNIEnv *env,
                                                                                jobject instance) {
    char info[40000] = {0};
//    av_register_all();
    struct URLProtocol *pup = NULL;
    struct URLProtocol **p_temp = &pup;
    avio_enum_protocols((void **) p_temp, 0);
    while ((*p_temp) != NULL) {
        sprintf(info, "%sInput: %s\n", info, avio_enum_protocols((void **) p_temp, 0));
    }

    return env->NewStringUTF(info);
}

}
