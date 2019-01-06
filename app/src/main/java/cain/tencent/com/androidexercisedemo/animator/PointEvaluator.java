package cain.tencent.com.androidexercisedemo.animator;

import android.animation.TypeEvaluator;

import cain.tencent.com.androidexercisedemo.animator.Point;

/**
 * @author cainjiang
 * @date 2018/11/23
 */
public class PointEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;
        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
        return new Point(x, y);
    }
}
