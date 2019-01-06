package cain.tencent.com.androidexercisedemo.utils;

import android.content.Context;
import android.util.Log;

/**
 * @author cainjiang
 * @date 2018/9/5
 */
public class DensityUtil {
    public DensityUtil() {
    }

    public static float dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5F;
    }

    public static float px2dp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return px / scale + 0.5F;
    }

    public static int dp2pxInt(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5F);
    }
}
