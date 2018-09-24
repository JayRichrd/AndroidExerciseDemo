package cain.tencent.com.androidexercisedemo

import android.databinding.DataBindingUtil
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import cain.tencent.com.androidexercisedemo.databinding.ActivityFrescoBinding
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.common.ImageDecodeOptions
import com.facebook.imagepipeline.common.ImageDecodeOptionsBuilder
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder


class FrescoActivity : AppCompatActivity() {
    lateinit var binding: ActivityFrescoBinding
    var imgUrlStr: String = "https://www.gstatic.com/webp/gallery/1.sm.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_fresco, null, false)
        setContentView(binding.root)
        loadImg()
    }

    private fun loadImg() {
//        var decodeOptions:ImageDecodeOptions = ImageDecodeOptions.newBuilder()
//                .setDecodePreviewFrame(true).build()
//        val request = ImageRequestBuilder
//                .newBuilderWithSource(uri)
//                .setImageDecodeOptions(decodeOptions)
//                .setAutoRotateEnabled(true)
//                .setLocalThumbnailPreviewsEnabled(true)
//                .setLowestPermittedRequestLevel(RequestLevel.FULL_FETCH)
//                .setProgressiveRenderingEnabled(false)
//                .setResizeOptions(ResizeOptions(width, height))
//                .build()
//        val layoutParams = simpleDraweeView.layoutParams

        val controllerListener = object : BaseControllerListener<ImageInfo>() {
            override fun onFinalImageSet(id: String?, @Nullable imageInfo: ImageInfo?, @Nullable anim: Animatable?) {
                if (imageInfo == null) {
                    return
                }
                val height = imageInfo?.getHeight()
                val width = imageInfo?.getWidth()
                Log.d("Jay", "width: $width, height: $height")
            }

            override fun onIntermediateImageSet(id: String?, @Nullable imageInfo: ImageInfo?) {
                Log.d("TAG", "Intermediate image received")
            }

            override fun onFailure(id: String?, throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
        val decodeOptionsBuilder: ImageDecodeOptionsBuilder = ImageDecodeOptions.newBuilder().setDecodePreviewFrame(true)
        val imageRequestBulder: ImageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imgUrlStr)).setResizeOptions(ResizeOptions(640, 428)).setImageDecodeOptions(decodeOptionsBuilder.build())
        val controller = Fresco.newDraweeControllerBuilder().setImageRequest(imageRequestBulder.build()).setOldController(binding.sdvView.controller).setAutoPlayAnimations(true).setControllerListener(controllerListener).build()
        binding.sdvView.controller = controller

        binding.qgsdvView1.setImageURI(imgUrlStr,null)
    }
}
