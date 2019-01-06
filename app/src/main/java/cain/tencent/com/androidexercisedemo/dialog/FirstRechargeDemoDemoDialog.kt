package cain.tencent.com.androidexercisedemo.dialog

import android.app.Activity
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import cain.tencent.com.androidexercisedemo.BaseDemoDialog
import cain.tencent.com.androidexercisedemo.R
import cain.tencent.com.androidexercisedemo.databinding.DialogFirstChargeLayoutBinding
import cain.tencent.com.androidexercisedemo.utils.DensityUtil
import com.facebook.drawee.controller.ControllerListener
import com.facebook.imagepipeline.image.ImageInfo

/**
 * 直播间首充活动弹窗
 * 触发时机与 RoomFirstRechargeDialog 不同，业务逻辑也不同
 * @author hexleowang
 * @since 2018/7/24.
 *
 * @param screenType 参考 VideoConstant类
 */
class FirstRechargeDemoDemoDialog(private val activity: Activity,
                                  private val from: Int,
                                  private val screenType: Int,
                                  private val imgUrl:String)
    : BaseDemoDialog(activity), ControllerListener<ImageInfo> {

    companion object {
        private const val TAG = "FirstRechargeDemoDemoDialog"
        private const val PORTRAIT_SIZE = 375f
        private const val LANDSCAPE_SIZE = 300f

        const val FROM_FOLLOW = 1 // 关注主播
        const val FROM_TIMEOUT = 2 // 观看超过10分钟
    }

    lateinit var viewBinding: DialogFirstChargeLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.decorView?.setPadding(0, 0, 0, 0)
        viewBinding = DataBindingUtil.inflate<DialogFirstChargeLayoutBinding>(LayoutInflater.from(context), R.layout.dialog_first_charge_layout, null, false)
        setContentView(viewBinding.getRoot())
        setCanceledOnTouchOutside(false)

        var size = DensityUtil.dp2px(activity, PORTRAIT_SIZE)
        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            size = DensityUtil.dp2px(activity, LANDSCAPE_SIZE)
        }

        viewBinding.img.layoutParams.apply {
            width = size.toInt()
            height = size.toInt()
            viewBinding.img.layoutParams = this
        }

        viewBinding.img.setImageURI("http://imgcache.gtimg.cn/ACT/svip_act/act_img/public/201811/m1541474041_sctc.png")
    }

    private fun initListener(){

    }

    // 只有在加载成功后才弹出对话框
    override fun show() {
        super.show()
    }

    override fun onFailure(id: String?, throwable: Throwable?) {
        Log.e(TAG,"Load image error: ${throwable.toString()}")
    }

    override fun onRelease(id: String?) {
        Log.d(TAG,"onRelease")
    }

    override fun onSubmit(id: String?, callerContext: Any?) {
        Log.d(TAG,"onRelease")
    }

    override fun onIntermediateImageSet(id: String?, imageInfo: ImageInfo?) {
        Log.d(TAG,"onRelease")
    }

    override fun onIntermediateImageFailed(id: String?, throwable: Throwable?) {
        Log.d(TAG,"onRelease")
    }

    // 图片加载成功才显示对话框
    override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
        Log.i(TAG,"ImageInfo: ${imageInfo?.width}, ${imageInfo?.height}")
    }

}