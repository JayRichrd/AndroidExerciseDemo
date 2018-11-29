package cain.tencent.com.androidexercisedemo

import android.content.Context
import android.graphics.PointF
import android.net.Uri
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import com.facebook.common.internal.Preconditions
import com.facebook.common.internal.Supplier
import com.facebook.common.util.UriUtil
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.interfaces.SimpleDraweeControllerBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder

/**
 * 重写simpleDraweeView，自动适配resizeOption
 * Created by luciolong on 2018/11/26.
 */
class QGameDraweeView: SimpleDraweeView {
    var isAllowDraw = false
    var imageUri : Uri? = null
    private val mSimpleDraweeControllerBuilder: SimpleDraweeControllerBuilder

    companion object {
        private var sDraweeControllerBuilderSupplier: Supplier<out SimpleDraweeControllerBuilder>? = null

        fun initialize(draweeControllerBuilderSupplier: Supplier<out SimpleDraweeControllerBuilder>) {
            sDraweeControllerBuilderSupplier = draweeControllerBuilderSupplier
        }

        fun shutDown() {
            sDraweeControllerBuilderSupplier = null
        }
    }

    constructor(context: Context?, hierarchy: GenericDraweeHierarchy?) : super(context, hierarchy)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        Preconditions.checkNotNull<Supplier<out SimpleDraweeControllerBuilder>>(sDraweeControllerBuilderSupplier, "SimpleDraweeView was not initialized!")
        mSimpleDraweeControllerBuilder = sDraweeControllerBuilderSupplier!!.get()
    }

    override fun setImageURI(uri: Uri?) {
        setImageURI(uri, null)
    }

    override fun setImageURI(uriString: String?) {
        setImageURI(uriString, null)
    }

    override fun setImageURI(uri: Uri?, callerContext: Any?) {
        if (isAllowDraw) {
            val resizeWidth = measuredWidth
            val resizeHeight = measuredHeight
            if (resizeWidth > 0 && resizeHeight > 0) {
                var hierarchy = hierarchy
                hierarchy.setActualImageFocusPoint(PointF(0.5f, 0.5f))
                setHierarchy(hierarchy)
                val imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(ResizeOptions(resizeWidth, resizeHeight)).build()
                controller = Fresco.newDraweeControllerBuilder().setCallerContext(callerContext).setImageRequest(imageRequest).setOldController(controller).setAutoPlayAnimations(true).build()
                val controller = mSimpleDraweeControllerBuilder
                        .setCallerContext(callerContext)
                        .setUri(uri)
                        .setOldController(controller)
                        .build()
                setController(controller)
            }
        } else {
            imageUri = uri
        }
    }

    override fun setImageURI(uriString: String?, callerContext: Any?) {
        val uri = if (uriString != null) Uri.parse(uriString) else null
        setImageURI(uri, callerContext)
    }

    override fun setActualImageResource(@DrawableRes resourceId: Int) {
        setActualImageResource(resourceId, null)
    }

    override fun setActualImageResource(@DrawableRes resourceId: Int, callerContext: Any?) {
        setImageURI(UriUtil.getUriForResourceId(resourceId), callerContext)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (measuredWidth > 0 && measuredHeight > 0) {
            val curUri = imageUri
            val oldIsAllowDraw = isAllowDraw
            isAllowDraw = true
            if (!oldIsAllowDraw && curUri != null) {
                setImageURI(curUri)
            }
        }
    }
}