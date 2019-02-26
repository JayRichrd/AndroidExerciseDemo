package cain.tencent.com.androidexercisedemo.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue
import java.io.BufferedInputStream
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author cainjiang
 * @date 2019/2/22
 */
class ImageUtil {
    interface DecodeResourceCallback {
        fun onSucess(bitmap: Bitmap?, resId: Int)
    }

    companion object {

        /**
         * 解码图片
         */
        fun decodeImage(resource: Resources, resId: Int, decodeResourceCallback: DecodeResourceCallback, isResize: Boolean, size: Int) {
            val cbPtr: WeakReference<DecodeResourceCallback> = WeakReference(decodeResourceCallback)
            val rsPtr: WeakReference<Resources> = WeakReference(resource)
            Thread(Runnable {
                val cb = cbPtr.get()
                val rs = rsPtr.get()
                cb?.apply {
                    rs?.let {
                        var bitmap: Bitmap? = null
                        if (isResize) {

                        } else {
                            bitmap = decodeResourceNonResize(it, resId)
                        }
                        this.onSucess(bitmap, resId)
                    }
                }
            }).start()

        }

        /**
         * 根据资源id解码出Bitmap
         * 不用resize
         */
        fun decodeResourceNonResize(rs: Resources, resId: Int): Bitmap? {
            val value = TypedValue()
            var bis = BufferedInputStream(rs.openRawResource(resId, value))
            val option = BitmapFactory.Options()
            option.inPreferredConfig = Bitmap.Config.RGB_565
            option.inSampleSize = 1
            option.inJustDecodeBounds = false
            return BitmapFactory.decodeResourceStream(rs, value, bis, null, option)
        }

        /**
         * 根据资源id解码出Bitmap
         * 不用resize
         */
        fun decodeResourceWithResize(rs: Resources, resId: Int, size: Int): Bitmap? {
            val value = TypedValue()
            val bis = BufferedInputStream(rs.openRawResource(resId, value))
            val option = BitmapFactory.Options()
            option.inPreferredConfig = Bitmap.Config.RGB_565
            option.inJustDecodeBounds = true
            BitmapFactory.decodeResourceStream(rs, value, bis, null, option)
            if (size > 0)
        }

        fun calculateInSampleSizeWithSize(option: BitmapFactory.Options, size: Int): Int {
            if (size == 0){
                return 1
            }
            val height = option.outHeight
            val width = option.outWidth

        }


    }
}