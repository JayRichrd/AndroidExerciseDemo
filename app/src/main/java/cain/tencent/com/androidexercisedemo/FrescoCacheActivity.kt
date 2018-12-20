package cain.tencent.com.androidexercisedemo

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import cain.tencent.com.androidexercisedemo.databinding.ActivityFrescoCacheBinding
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequestBuilder

@SuppressLint("ActivityRouterAnnotationDetector")
class FrescoCacheActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityFrescoCacheBinding
    var imgUrlStr: String = "http://imgcache.gtimg.cn/ACT/svip_act/act_img/public/201811/m1541474041_sctc.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_fresco_cache, null, false)
        setContentView(binding.root)
        binding.sdvView.setImageURI(imgUrlStr)
        binding.btnLoadOtherImage.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_load_other_image -> loadOtherImage()
        }
    }

    private fun loadOtherImage() {
//        val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imgUrlStr)).build()
//        val controller = Fresco.newDraweeControllerBuilder().setImageRequest(imageRequest).build()
//        binding.sdvView1.controller = controller
        binding.sdvView1.setImageURI(imgUrlStr)
    }
}
