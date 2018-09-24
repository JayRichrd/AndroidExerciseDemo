package cain.tencent.com.androidexercisedemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.SimpleDraweeControllerBuilder;
import com.facebook.drawee.view.GenericDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * @author cainjiang
 * @date 2018/9/10
 */
public class QGameSimpleDraweeView extends GenericDraweeView {
    public static final String TAG = "QGameSimpleDraweeView";

    private static Supplier<? extends SimpleDraweeControllerBuilder> sDraweeControllerBuilderSupplier;
    private SimpleDraweeControllerBuilder mSimpleDraweeControllerBuilder;
    /**
     * resize的宽度，单位像素(px)
     */
    private int mResizeWidth;
    /**
     * resize的宽度，单位像素(px)
     */
    private int mResizeHeight;
    /**
     * 图片的URL地址
     */
    private String mImageUrl;
    /**
     * 图片的资源地址
     */
    private int mResId = NO_ID;

    public static void initialize(Supplier<? extends SimpleDraweeControllerBuilder> draweeControllerBuilderSupplier) {
        sDraweeControllerBuilderSupplier = draweeControllerBuilderSupplier;
    }

    public static void shutDown() {
        sDraweeControllerBuilderSupplier = null;
    }

    public QGameSimpleDraweeView(Context context) {
        super(context);
    }

    public QGameSimpleDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init(context, null);
    }

    public QGameSimpleDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public QGameSimpleDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public QGameSimpleDraweeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        if (!this.isInEditMode()) {
            Preconditions.checkNotNull(sDraweeControllerBuilderSupplier, "SimpleDraweeView was not initialized!");
            mSimpleDraweeControllerBuilder = sDraweeControllerBuilderSupplier.get();
            if (attrs != null) {
                TypedArray gdhAttrs = context.obtainStyledAttributes(attrs, R.styleable.QGameSimpleDraweeView);
                try {
                    if (gdhAttrs.hasValue(R.styleable.QGameSimpleDraweeView_qgSdvResizeWidth)) {
                        mResizeWidth = gdhAttrs.getInt(R.styleable.QGameSimpleDraweeView_qgSdvResizeWidth, 0);
                    }
                    if (gdhAttrs.hasValue(R.styleable.QGameSimpleDraweeView_qgSdvResizeHeight)) {
                        mResizeHeight = gdhAttrs.getInt(R.styleable.QGameSimpleDraweeView_qgSdvResizeHeight, 0);
                    }

                    if (gdhAttrs.hasValue(R.styleable.QGameSimpleDraweeView_qgSdvImgUrl)) {
                        mImageUrl = gdhAttrs.getString(R.styleable.QGameSimpleDraweeView_qgSdvImgUrl);
                        setImageURI(mImageUrl, null);
                    } else if (gdhAttrs.hasValue(R.styleable.QGameSimpleDraweeView_qgSdvImgResource)) {
                        mResId = gdhAttrs.getResourceId(R.styleable.QGameSimpleDraweeView_qgSdvImgResource, NO_ID);
                        if (mResId != NO_ID) {
                            setActualImageResource(mResId, null);
                        }
                    }
                } finally {
                    gdhAttrs.recycle();
                }
            }

        }
    }

    /**
     * 在xml中使用datading时需要设置自定义属性的setter方法
     * @param resizeWidth
     */
    public void setQgSdvResizeWidth(int resizeWidth) {
        if (resizeWidth > 0) {
            mResizeWidth = resizeWidth;
            setImage();
        }
    }

    public void setQgSdvResizeHeight(int resizeHeight) {
        if (resizeHeight > 0) {
            mResizeHeight = resizeHeight;
            setImage();
        }
    }

    public void setQgSdvImgUrl(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            mImageUrl = imageUrl;
            setImage();
        }
    }

    public void setQgSdvImgResource(int resId) {
        if (resId != NO_ID) {
            mResId = resId;
            setImage();
        }
    }

    /**
     * Displays an image given by the uri.
     *
     * @param uri           uri of the image
     * @param callerContext caller context
     */
    private void setImageURI(Uri uri, @Nullable Object callerContext) {
        DraweeController controller;
        if (mResizeWidth > 0 && mResizeHeight > 0) {
            Log.i(TAG, "resizeWidth: " + mResizeWidth + ", resizeHeight: " + mResizeHeight);
            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).
                    setResizeOptions(new ResizeOptions(mResizeWidth, mResizeHeight)).
                    build();
            controller = Fresco.newDraweeControllerBuilder().
                    setImageRequest(imageRequest).
                    setOldController(getController()).
                    setAutoPlayAnimations(true).
                    build();
        } else {
            Log.e(TAG, "You haven't set resizeWidth and resizeHeight!");
//            if (AppSetting.isDebugVersion) {
//                QQToast.makeText(getContext(), "图片未设置resize参数", Toast.LENGTH_LONG).show();
//            }
            controller = mSimpleDraweeControllerBuilder.setCallerContext(callerContext).
                    setUri(uri).
                    setOldController(getController()).
                    build();
        }
        setController(controller);
    }

    private void setImage() {
        if (!TextUtils.isEmpty(mImageUrl)) {
            setImageURI(mImageUrl, null);
        } else if (mResId != NO_ID) {
            setActualImageResource(mResId, null);
        }
    }

    /**
     * Displays an image given by the uriString.
     *
     * @param uriString
     * @param callerContext
     */
    public void setImageURI(@Nullable String uriString, @Nullable Object callerContext) {
        Uri uri = uriString != null ? Uri.parse(uriString) : null;
        setImageURI(uri, callerContext);
    }

    /**
     * 设置resize的宽度
     *
     * @param resizeWidth
     * @return
     */
    public GenericDraweeView setResizeWidth(int resizeWidth) {
        mResizeWidth = resizeWidth;
        return this;
    }

    /**
     * 设置resize的高度
     *
     * @param resizeHeight
     * @return
     */
    public GenericDraweeView setResizeHeight(int resizeHeight) {
        mResizeHeight = resizeHeight;
        return this;
    }

    /**
     * 设置资源ID
     * @param resId
     * @return
     */
    public GenericDraweeView setResId(int resId){
        mResId = resId;
        return this;
    }

    /**
     * Sets the actual image resource to the given resource ID.
     * <p>
     * Similar to {@link #setImageResource(int)}, this sets the displayed image to the given resource.
     * However, {@link #setImageResource(int)} bypasses all Drawee functionality and makes the view
     * act as a normal {@link android.widget.ImageView}, whereas this method keeps all of the
     * Drawee functionality, including the {@link com.facebook.drawee.interfaces.DraweeHierarchy}.
     *
     * @param resourceId    the resource ID to use.
     * @param callerContext caller context
     */
    public void setActualImageResource(@DrawableRes int resourceId, @Nullable Object callerContext) {
        setImageURI(UriUtil.getUriForResourceId(resourceId), callerContext);
    }

    /**
     * Sets the actual image resource to the given resource ID.
     * <p>
     * <p>Similar to {@link #setImageResource(int)}, this sets the displayed image to the given
     * resource. However, {@link #setImageResource(int)} bypasses all Drawee functionality and makes
     * the view act as a normal {@link android.widget.ImageView}, whereas this method keeps all of the
     * Drawee functionality, including the {@link com.facebook.drawee.interfaces.DraweeHierarchy}.
     *
     * @param resourceId the resource ID to use.
     */
    public void setActualImageResource(@DrawableRes int resourceId) {
        setActualImageResource(resourceId, null);
    }

    /**
     * This method will bypass all Drawee-related functionality.
     * If you want to keep this functionality, take a look at {@link #setActualImageResource(int)}
     * and {@link #setActualImageResource(int, Object)}}.
     *
     * @param resId the resource ID
     */
    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

    protected SimpleDraweeControllerBuilder getControllerBuilder() {
        return mSimpleDraweeControllerBuilder;
    }

}
