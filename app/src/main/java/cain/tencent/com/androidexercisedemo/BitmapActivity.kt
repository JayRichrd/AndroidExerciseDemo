package cain.tencent.com.androidexercisedemo

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import cain.tencent.com.androidexercisedemo.databinding.ActivityBitmapBinding

class BitmapActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityBitmapBinding

    companion object {
        const val TAG = "BitmapActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_bitmap, null, false)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        // 缩放以后再加载
        binding.ivBitmap.post { binding.ivBitmap.setImageBitmap(createBitmap(binding.ivBitmap.measuredWidth, binding.ivBitmap.measuredHeight)) }
        // 直接加载
        binding.ivBitmap1.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.sm))
        binding.btnGetSize.setOnClickListener(this)
        binding.btnParseBitmap.setOnClickListener(this)
    }

    private fun createBitmap(reqWidth: Int, reqHeight: Int): Bitmap {
        val option = BitmapFactory.Options()
        // 仅仅解析尺寸信息
        option.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.sm, option)
        Log.i(TAG, "reqWidth: $reqWidth, reqHeght: $reqHeight")
        option.inSampleSize = calculateInSimpleSize(reqWidth, reqHeight, option)
        option.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(resources, R.drawable.sm, option)
    }

    private fun calculateInSimpleSize(reqWidth: Int, reqHeight: Int, options: BitmapFactory.Options): Int {
        val height = options.outHeight
        val width = options.outWidth
        Log.i(TAG, "outWidth: $width, outHeight: $height")
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2
            }
        }
        Log.i(TAG, "inSampleSize: $inSampleSize")
        return inSampleSize
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_get_size -> getBitmapSize()
            R.id.btn_parse_bitmap -> parseBitmap()
            else -> "nothing to do"
        }
    }

    private fun parseBitmap() {
        val bitmap = AppImageUtil.getScaledBitmap(this.resources, R.drawable.guardian_hover_danmaku_bg, DensityUtil.dp2px(this, 30f))
        bitmap?.let {
            Log.d(TAG, "bitmap width = ${bitmap.width}, height = ${bitmap.height}")
        }
    }

    /**
     * 从ImageView中获取Bitmap，并计算Bitmap的大小
     */
    private fun getBitmapSize() {
        val bitmapDrawable: BitmapDrawable = binding.ivBitmap.drawable as BitmapDrawable
        val bitmapDrawable1: BitmapDrawable = binding.ivBitmap1.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val bitmap1 = bitmapDrawable1.bitmap
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.i(TAG, "bitmap size: ${bitmap.allocationByteCount / 1024}k")
            Log.i(TAG, "bitmap1 size: ${bitmap1.allocationByteCount / 1024}k")
        } else {
            Log.i(TAG, "bitmap size: ${bitmap.byteCount / 1024}k")
            Log.i(TAG, "bitmap1 size: ${bitmap1.byteCount / 1024}k")
        }
    }
}
