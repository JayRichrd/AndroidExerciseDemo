# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# 定义变量
set(distribution_DIR ../../../../libs)

# 添加库——自己编写的库
# 库名称：ndk_test_jni
# 库类型：SHARED，表示动态库，后缀为.so（如果是STATIC，则表示静态库，后缀为.a）
# 库源码文件：src/main/jni/ndk_test_jni.cpp
add_library(ndk_test_jni
        SHARED
        src/main/jni/ndk_test_jni.cpp
        src/main/jni/lame/bitstream.c
        src/main/jni/lame/encoder.c
        src/main/jni/lame/fft.c
        src/main/jni/lame/gain_analysis.c
        src/main/jni/lame/id3tag.c
        src/main/jni/lame/lame.c
        src/main/jni/lame/mpglib_interface.c
        src/main/jni/lame/newmdct.c
        src/main/jni/lame/presets.c
        src/main/jni/lame/psymodel.c
        src/main/jni/lame/quantize_pvt.c
        src/main/jni/lame/quantize.c
        src/main/jni/lame/reservoir.c
        src/main/jni/lame/set_get.c
        src/main/jni/lame/tables.c
        src/main/jni/lame/takehiro.c
        src/main/jni/lame/util.c
        src/main/jni/lame/vbrquantize.c
        src/main/jni/lame/VbrTag.c
        src/main/jni/lame/version.c)

# 添加库——外部引入的库
# 库名称：avcodec（不需要包含前缀lib）
# 库类型：SHARED，表示动态库，后缀为.so（如果是STATIC，则表示静态库，后缀为.a）
# IMPORTED表明是外部引入的库
add_library(avcodec
        SHARED
        IMPORTED)
# 设置目标属性
# 设置avcodec目标库的IMPORTED_LOCATION属性，用于说明引入库的位置
# 还可以设置其他属性，格式：PROPERTIES key value
set_target_properties(avcodec
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libavcodec.so)


find_library(
        log-lib
        log)
add_library(avfilter
        SHARED
        IMPORTED)
set_target_properties(avfilter
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libavfilter.so)

add_library(avformat
        SHARED
        IMPORTED)
set_target_properties(avformat
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libavformat.so)

add_library(avutil
        SHARED
        IMPORTED)
set_target_properties(avutil
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libavutil.so)

add_library(swresample
        SHARED
        IMPORTED)
set_target_properties(swresample
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libswresample.so)

add_library(swscale
        SHARED
        IMPORTED)
set_target_properties(swscale
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libswscale.so)

# 引入头文件
include_directories(libs/include)

# 告诉编译器生成ndk_test_jni库需要链接的库
# native-lib库需要依赖avcodec、avfilter等库
target_link_libraries(ndk_test_jni
        avcodec
        avfilter
        avformat
        avutil
        swresample
        swscale
        -landroid
        ${log-lib})

