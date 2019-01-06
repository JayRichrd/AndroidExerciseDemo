package cain.tencent.com.androidexercisedemo.bitmap

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import cain.tencent.com.androidexercisedemo.R
import cain.tencent.com.androidexercisedemo.databinding.ActivityPhotoWallBinding

@SuppressLint("ActivityRouterAnnotationDetector")
class PhotoWallActivity : AppCompatActivity() {
    lateinit var binding: ActivityPhotoWallBinding
    lateinit var adapter: PhotoWallAdapter

    companion object {
        const val TAG = "PhotoWallActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_photo_wall, null, false)
        setContentView(binding.root)
        adapter = PhotoWallAdapter(this, 0, Images.imageThumbUrls, binding.photoWall)
        binding.photoWall.adapter = adapter
    }

    override fun onPause() {
        super.onPause()
        adapter.flushCache()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 退出程序时结束所有的下载任务
        adapter.cancelAllTasks()
    }
}
