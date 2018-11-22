package cain.tencent.com.androidexercisedemo;

import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import cain.tencent.com.androidexercisedemo.databinding.ActivityAnimatorBinding;

public class AnimatorActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAnimatorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_animator, null, false);
        setContentView(binding.getRoot());
        binding.btnStartAnimator.setOnClickListener(this);
        binding.btnStartAnimator1.setOnClickListener(this);
        binding.btnStartAnimator2.setOnClickListener(this);
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
        }
    }

    private void tanslatiionAnimator() {

    }

    private void rotationAnimator() {
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(binding.tvAnimator, "rotation", 0f, 180f);
        rotationAnimator.setDuration(2 * 1000);
        rotationAnimator.start();
    }

    private void alphaAnimator() {

    }
}
