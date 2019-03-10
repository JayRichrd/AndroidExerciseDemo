cmake_minimum_required(VERSION 3.4.1)
add_library(
        # Sets the name of the library.
        ndk_test_jni
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        # 这里的路径和文件后缀一定要写对，否则后面无法sync
        src/main/jni/ndk_test_jni.cpp
        src/main/jni/bitstream.c
        src/main/jni/encoder.c
        src/main/jni/fft.c
        src/main/jni/gain_analysis.c
        src/main/jni/id3tag.c
        src/main/jni/lame.c
        src/main/jni/mpglib_interface.c
        src/main/jni/newmdct.c
        src/main/jni/presets.c
        src/main/jni/psymodel.c
        src/main/jni/quantize_pvt.c
        src/main/jni/quantize.c
        src/main/jni/reservoir.c
        src/main/jni/set_get.c
        src/main/jni/tables.c
        src/main/jni/takehiro.c
        src/main/jni/util.c
        src/main/jni/vbrquantize.c
        src/main/jni/VbrTag.c
        src/main/jni/version.c
)
find_library(
        # Sets the name of the path variable.
        log-lib
        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log
)
target_link_libraries(
        # Specifies the target library.
        ndk_test_jni
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
)