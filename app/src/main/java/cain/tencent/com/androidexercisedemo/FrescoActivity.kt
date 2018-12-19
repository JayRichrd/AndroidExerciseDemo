package cain.tencent.com.androidexercisedemo

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.databinding.ObservableField
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import cain.tencent.com.androidexercisedemo.databinding.ActivityFrescoBinding
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.common.ImageDecodeOptions
import com.facebook.imagepipeline.common.ImageDecodeOptionsBuilder
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.postprocessors.RoundAsCirclePostprocessor
import com.facebook.imagepipeline.request.ImageRequestBuilder


@SuppressLint("ActivityRouterAnnotationDetector")
class FrescoActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_query_cache -> onQueryCache()
        }
    }

    @SuppressLint("HardcodedStringDetector")
    private fun onQueryCache() {
        val imagePipeLine = Fresco.getImagePipeline()
//        val uri = Uri.parse("res://cain.tencent.com.androidexercisedemo/" + R.drawable.toutiao_is_live)
        val uri = Uri.parse(imgUrlStr)
        println("图片是否已经在缓存中：${imagePipeLine.isInBitmapMemoryCache(uri)}")
    }

    companion object {
        const val TAG = "FrescoActivity"
    }

    lateinit var binding: ActivityFrescoBinding
    //    var imgUrlStr: String = "https://www.gstatic.com/webp/gallery/1.sm.jpg"
    var imgUrlStr: String = "http://imgcache.gtimg.cn/ACT/svip_act/act_img/public/201811/m1541474041_sctc.png"
    val test = ObservableField<Boolean>(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_fresco, null, false)
        binding.setVariable(BR.frescoActivity, this)
        setContentView(binding.root)
        binding.btnQueryCache.setOnClickListener(this)
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
                Log.d(TAG, "width: $width, height: $height")
            }

            override fun onIntermediateImageSet(id: String?, @Nullable imageInfo: ImageInfo?) {
                Log.d(TAG, "Intermediate image received")
            }

            override fun onFailure(id: String?, throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
        val decodeOptionsBuilder: ImageDecodeOptionsBuilder = ImageDecodeOptions.newBuilder().setDecodePreviewFrame(true)
//        val imageRequestBulder: ImageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imgUrlStr)).setResizeOptions(ResizeOptions(640, 428)).setImageDecodeOptions(decodeOptionsBuilder.build()).setPostprocessor(RoundAsCircleAndOverlayPostProcessor())
        val imageRequestBulder: ImageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imgUrlStr)).setResizeOptions(ResizeOptions(640, 428)).setImageDecodeOptions(decodeOptionsBuilder.build())
        val controller = Fresco.newDraweeControllerBuilder().setImageRequest(imageRequestBulder.build()).setOldController(binding.sdvView.controller).setAutoPlayAnimations(true).setControllerListener(controllerListener).build()
        binding.sdvView.controller = controller
        binding.sdvView1.setImageURI(imgUrlStr)
        binding.sdvView2.setImageURI(imgUrlStr)
        binding.qgsdvView1.setQgSdvImgUrl(imgUrlStr, null)

        val uri = Uri.parse("res://cain.tencent.com.androidexercisedemo/" + R.drawable.toutiao_is_live)
        val webPController = Fresco.newDraweeControllerBuilder().setUri(uri).setAutoPlayAnimations(true).build()
        binding.sdvWebP.controller = webPController
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }
}
