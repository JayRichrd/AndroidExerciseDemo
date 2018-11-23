package cain.tencent.com.androidexercisedemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cain.tencent.com.androidexercisedemo.databinding.ActivityAnimatorBinding;

@SuppressLint("ActivityRouterAnnotationDetector")
public class AnimatorActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAnimatorBinding binding;
    public static final String TAG = "AnimatorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_animator, null, false);
        setContentView(binding.getRoot());
        binding.btnStartAnimator.setOnClickListener(this);
        binding.btnStartAnimator1.setOnClickListener(this);
        binding.btnStartAnimator2.setOnClickListener(this);
        binding.btnStartAnimator3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_animator:
                rotationAnimator();
                break;
            case R.id.btn_start_animator1:
                alphaAnimator();
                break;
            case R.id.btn_start_animator2:
                tanslatiionAnimator();
                break;
            case R.id.btn_start_animator3:
                mergeAnimator();
                break;
        }
    }

    private void mergeAnimator() {
        float currentTranslationX = binding.tvAnimator.getTranslationX();
        float left = binding.tvAnimator.getLeft();
        Log.d(TAG, "current translationx = " + currentTranslationX + ", left = " + left);
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(binding.tvAnimator, "translationX", currentTranslationX, -left, currentTranslationX);
        translationAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.i(TAG, "translationAnimator#onAnimationStart: " + animation.getDuration());
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i(TAG, "translationAnimator#onAnimationEnd: " + animation.getDuration());
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i(TAG, "translationAnimator#onAnimationCancel: " + animation.getDuration());
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.i(TAG, "translationAnimator#onAnimationRepeat: " + animation.getDuration());
            }
        });

        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(binding.tvAnimator, "rotation", 0f, 180f);
        rotationAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                Log.d(TAG, "rotationAnimator#onAnimationStart: " + animation.getDuration());
            }
        });


        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(binding.tvAnimator, "alpha", 1f, 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alphaAnimator).with(rotationAnimator).after(translationAnimator);
        animatorSet.setDuration(5 * 1000);
        animatorSet.start();
    }

    private void tanslatiionAnimator() {
        float currentTranslationX = binding.tvAnimator.getTranslationX();
        float left = binding.tvAnimator.getLeft();
        Log.d(TAG, "current translationx = " + currentTranslationX + ", left = " + left);
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(binding.tvAnimator, "translationX", currentTranslationX, -left, currentTranslationX);
        translationAnimator.setDuration(5 * 1000);
        translationAnimator.start();
    }

    private void rotationAnimator() {
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(binding.tvAnimator, "rotation", 0f, 180f);
        rotationAnimator.setDuration(2 * 1000);
        rotationAnimator.start();
    }

    private void alphaAnimator() {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(binding.tvAnimator, "alpha", 1f, 0f, 1f);
        alphaAnimator.setDuration(3 * 1000);
        alphaAnimator.start();
    }
}
