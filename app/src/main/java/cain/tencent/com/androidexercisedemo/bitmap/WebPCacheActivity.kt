package cain.tencent.com.androidexercisedemo.bitmap

import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import cain.tencent.com.androidexercisedemo.R
import cain.tencent.com.androidexercisedemo.databinding.ActivityWebPcacheBinding
import com.facebook.drawee.backends.pipeline.Fresco

class WebPCacheActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_load_other_image -> loadOtherImage()
        }

    }

    private fun loadOtherImage() {
        val uri = Uri.parse("res://cain.tencent.com.androidexercisedemo/" + R.drawable.toutiao_is_live)
        val webPController = Fresco.newDraweeControllerBuilder().setUri(uri).setAutoPlayAnimations(true).build()
        binding.sdvWebP1.controller = webPController
    }

    lateinit var binding: ActivityWebPcacheBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_web_pcache, null, false)
        setContentView(binding.root)
        binding.btnLoadOtherImage.setOnClickListener(this)
        val uri = Uri.parse("res://cain.tencent.com.androidexercisedemo/" + R.drawable.toutiao_is_live)
        val webPController = Fresco.newDraweeControllerBuilder().setUri(uri).setAutoPlayAnimations(true).build()
        binding.sdvWebP.controller = webPController
    }
}
