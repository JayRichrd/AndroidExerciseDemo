@file:JvmName("MyUtils")

package cain.tencent.com.androidexercisedemo.utils

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import java.io.File
import java.security.MessageDigest
import kotlin.text.StringBuilder

fun getUniqueCacheDir(ctx: Context, uniqueName: String): File? {
    var cachePath: String? = null
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageDirectory()) || !Environment.isExternalStorageRemovable()) {
        cachePath = ctx.externalCacheDir.path
    } else {
        cachePath = ctx.cacheDir.path
    }
    if (cachePath == null || TextUtils.isEmpty(cachePath)) {
        return null
    }
    return File(cachePath + File.separator + uniqueName)
}

fun getAppVersion(ctx: Context): Int {
    val packageInfo = ctx.packageManager.getPackageInfo(ctx.packageName, 0)
    return packageInfo.versionCode
}

fun hashKeyForDisk(key: String): String {
    val digest = MessageDigest.getInstance("MD5")
    digest.update(key.toByteArray())
    return bytesToHexString(digest.digest())
}

fun bytesToHexString(bytes: ByteArray) = StringBuilder().apply {
    for (letter in bytes.asList()) {
        var hex = Integer.toHexString(0xFF.and(letter.toInt()))
        if (hex.length == 1) {
            this.append('0')
        }
        this.append(hex)
    }
}.toString()
