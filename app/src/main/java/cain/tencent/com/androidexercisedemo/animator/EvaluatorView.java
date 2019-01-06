package cain.tencent.com.androidexercisedemo.animator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * @author cainjiang
 * @date 2018/11/23
 */
public class EvaluatorView extends View {
    public static final String TAG = "EvaluatorView";
    public static final float RADIUS = 50F;
    public static final String START_COLOR = "#FF0000";
    public static final String END_COLOR = "#00FFFF";
    Point currentPoint;
    Paint paint;
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        if (paint != null) {
            paint.setColor(Color.parseColor(color));
//            invalidate();
        }
    }

    public EvaluatorView(Context context) {
        super(context);
    }

    public EvaluatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor(START_COLOR));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (currentPoint == null) {
            currentPoint = new Point(RADIUS, RADIUS);
            drawCircle(canvas);
            startAnimation();
        } else {
            drawCircle(canvas);
        }
    }

    private void startAnimation() {
        Point startPoint = new Point(getWidth() /2, RADIUS);
        Point endPoint = new Point(getWidth()/2, getHeight() - RADIUS);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
//        valueAnimator.setInterpolator(new AccelerateInterpolator(2F));
        valueAnimator.setInterpolator(new BounceInterpolator());

        ObjectAnimator colorAnimator = ObjectAnimator.ofObject(this, "color", new ColorEvaluator(), START_COLOR, END_COLOR);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(valueAnimator).with(colorAnimator);
        animatorSet.setDuration(5 * 1000);
        animatorSet.start();
    }

    void drawCircle(Canvas canvas) {
        float x = currentPoint.getX();
        float y = currentPoint.getY();
        Log.d(TAG,"Paint color: " + paint.getColor());
        canvas.drawCircle(x, y, RADIUS, paint);
    }
}
