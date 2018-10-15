package cain.tencent.com.androidexercisedemo

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import cain.tencent.com.androidexercisedemo.databinding.ActivityBitmapBinding

class BitmapActivity : AppCompatActivity() {
    lateinit var binding: ActivityBitmapBinding

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_bitmap, null, false)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.ivBitmap.setImageBitmap(createBitmap())
    }

    private fun createBitmap() = BitmapFactory.decodeResource(resources, R.mipmap.sm)
}
