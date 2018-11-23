package cain.tencent.com.androidexercisedemo;

import android.animation.TypeEvaluator;
import android.util.Log;

/**
 * @author cainjiang
 * @date 2018/11/23
 */
public class ColorEvaluator implements TypeEvaluator {
    public static final String TAG = "ColorEvaluator";
    int mCurrentRed = -1;
    int mCurrentGreen = -1;
    int mCurrentBlue = -1;

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        String startColor = (String) startValue;
        String endColor = (String) endValue;
        int startRed = Integer.parseInt(startColor.substring(1, 3), 16);
        int endRed = Integer.parseInt(endColor.substring(1, 3), 16);
        int startGreen = Integer.parseInt(startColor.substring(3, 5), 16);
        int endGreen = Integer.parseInt(endColor.substring(3, 5), 16);
        int startBlue = Integer.parseInt(startColor.substring(5, 7), 16);
        int endBlue = Integer.parseInt(endColor.substring(5, 7), 16);
        // 初始化颜色值的初始值
        if (mCurrentRed == -1) {
            mCurrentRed = startRed;
        }
        if (mCurrentGreen == -1) {
            mCurrentGreen = startGreen;
        }
        if (mCurrentBlue == -1) {
            mCurrentBlue = startBlue;
        }
        // 颜色值的差值
        // 由于有三种颜色，这里用排列组合出有多少种变化
        int redDiff = Math.abs(startRed - endRed);
        int greenDiff = Math.abs(startGreen - endGreen);
        int blueDiff = Math.abs(startBlue - endBlue);
        int colorDiff = redDiff * greenDiff * blueDiff;

        if (mCurrentRed != endRed) {
            mCurrentRed = getCurrentColor(startRed, endRed, colorDiff, 0, fraction);
        }
        if (mCurrentGreen != endGreen) {
            mCurrentGreen = getCurrentColor(startGreen, endGreen, colorDiff, 0, fraction);
        }
        if (mCurrentBlue != endBlue) {
            mCurrentBlue = getCurrentColor(startBlue, endBlue, colorDiff, 0, fraction);
        }
        Log.d(TAG, "mCurrentRed = " + mCurrentRed + ", mCurrentGreen = " + mCurrentGreen + ", mCurrentBlue = " + mCurrentBlue + ", fraction = " + fraction);
        // 将计算出的结果再组装成颜色值返回
        String currentColor = "#" + getHexString(mCurrentRed) + getHexString(mCurrentGreen) + getHexString(mCurrentBlue);

        return currentColor;
    }

    private String getHexString(int currentColor) {
        String hexStr = Integer.toHexString(currentColor);
        // 不足两位用0填充
        if (hexStr.length() == 1) {
            hexStr += "0";
        }
        return hexStr;
    }

    private int getCurrentColor(int startColor, int endColor, int colorDiff, int offset, float fraction) {
        int currentColor;
        if (startColor > endColor) {
            currentColor = (int) (startColor - fraction * (startColor - endColor));
        } else {
            currentColor = (int) (startColor + fraction * (endColor - startColor));
        }
        return currentColor;
    }
}
