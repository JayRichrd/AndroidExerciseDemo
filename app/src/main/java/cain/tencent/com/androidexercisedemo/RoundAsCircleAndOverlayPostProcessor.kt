package cain.tencent.com.androidexercisedemo

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.facebook.imagepipeline.postprocessors.RoundAsCirclePostprocessor

/**
 * @author cainjiang
 * @date 2018/11/13
 */
class RoundAsCircleAndOverlayPostProcessor: RoundAsCirclePostprocessor(){

    override fun process(bitmap: Bitmap?) {
        super.process(bitmap)
        val width = bitmap?.getWidth()
        val height = bitmap?.getHeight()
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.setColor(Color.RED)
        paint.setStyle(Paint.Style.STROKE);//不填充
        paint.setStrokeWidth(1F) //线的宽度
        paint.isAntiAlias = true
        // 画矩形
//        canvas.drawRect(10F, 20F, 100F, 100F, paint)
        // 画圆
        width?.let {
            canvas.drawCircle((it / 2).toFloat(), (it / 2).toFloat(), (it / 2).toFloat(), paint)
        }

    }
}