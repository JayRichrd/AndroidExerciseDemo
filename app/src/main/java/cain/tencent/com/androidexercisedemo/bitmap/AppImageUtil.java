package cain.tencent.com.androidexercisedemo.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;


import java.io.IOException;
import java.io.InputStream;

/**
 * App Module内的图片工具类，使用到App内的功能，不能独立到Util中，与ImageUtil相区别
 * Created by hexleowang on 17/1/11.
 */
public class AppImageUtil {
	public static final String TAG = "AppImageUtil";

	/**
	 * 直接根据资源id获取一张缩放后的图，和BitmapFactory的根据已有图片获取一张新的缩放后的Bitmap不一样，这里直接将需要的大小的图解码到内存中
	 * 普通的方式缩放是根据存放的文件夹位置和当前屏幕的像素密度计算出来的缩放比例，这里是根据最后真正想展示的大小和原图的尺寸计算出来的缩放比例
	 */
	public static @Nullable
    Bitmap getScaledBitmap(@NonNull Resources res, int id, float expertHeight) {
		Bitmap bm = null;
		InputStream is = null;

		BitmapFactory.Options opts = new BitmapFactory.Options();
		try {
			final TypedValue value = new TypedValue();
			is = res.openRawResource(id, value);

			opts.inDensity = 0;
			opts.inJustDecodeBounds = true;
			bm = BitmapFactory.decodeResource(res, id, opts);

			int width = opts.outWidth;
			int height = opts.outHeight;
			if (width == 0 || height == 0) {
				return null;
			}
			Log.d(TAG, "width = " + width + ", height = " + height);

			final int density = value.density;
			opts.inJustDecodeBounds = false;
			if (density == TypedValue.DENSITY_DEFAULT) {
				opts.inDensity = DisplayMetrics.DENSITY_DEFAULT;
			} else if (density != TypedValue.DENSITY_NONE) {
				opts.inDensity = density;
			}
			float scale = expertHeight/height;
			Log.d(TAG, "scale is " + scale);
			opts.inTargetDensity = (int) (opts.inDensity*scale);
			bm = BitmapFactory.decodeStream(is, null, opts);
		} catch (Exception e) {
            /*  do nothing.
                If the exception happened on open, bm will be null.
                If it happened on close, bm is still valid.
            */
		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException e) {
				// Ignore
			}
		}

		if (bm == null && opts != null && opts.inBitmap != null) {
			throw new IllegalArgumentException("Problem decoding into existing bitmap");
		}

		return bm;

	}

}
