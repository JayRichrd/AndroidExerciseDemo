package cain.tencent.com.androidexercisedemo.bitmap;

import android.databinding.BindingAdapter;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;

import cain.tencent.com.androidexercisedemo.QGameSimpleDraweeView;

/**
 * @author cainjiang
 * @date 2018/11/22
 */
public class ViewModelMethod {
    /**
     * 设置Fresco的imageUrl和前景图
     *
     * @param view       Fresco的ImageView
     * @param overlayImg 前景图
     */
    @BindingAdapter({"imageOverlay"})
    public static void loadImage(final QGameSimpleDraweeView view, Drawable overlayImg) {
        GenericDraweeHierarchy hierarchy = view.getHierarchy();
        hierarchy.setActualImageFocusPoint(new PointF(0.5f, 0.5f));
        if (overlayImg != null) {
            hierarchy.setOverlayImage(overlayImg);
        }
    }
}
