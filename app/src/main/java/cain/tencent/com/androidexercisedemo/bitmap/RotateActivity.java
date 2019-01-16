package cain.tencent.com.androidexercisedemo.bitmap;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import cain.tencent.com.androidexercisedemo.R;
import cain.tencent.com.androidexercisedemo.animator.PictureAdapter;
import cain.tencent.com.androidexercisedemo.animator.Rotate3dAnimation;
import cain.tencent.com.androidexercisedemo.animator.data.Picture;
import cain.tencent.com.androidexercisedemo.databinding.ActivityRotateBinding;

public class RotateActivity extends AppCompatActivity {
    ActivityRotateBinding binding;
    /**
     * 存放所有图片的集合
     */
    private List<Picture> picList = new ArrayList<Picture>();
    /**
     * 图片列表的适配器
     */
    private PictureAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_rotate, null, false);
        setContentView(binding.getRoot());
        // 对图片列表数据进行初始化操作
        initPics();
        adapter = new PictureAdapter(this, 0, picList.toArray(new Picture[picList.size()]));
        binding.picListView.setAdapter(adapter);
        binding.picListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.picture.setImageResource(picList.get(position).getResource());
                float centerX = binding.layout.getWidth() / 2;
                float centerY = binding.layout.getHeight() / 2;
                // 构建3D旋转动画对象，旋转角度为0到90度，这使得ListView将会从可见变为不可见
                Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(0, 90, centerX, centerY, 0f, true);
                rotate3dAnimation.setDuration(5000);
                rotate3dAnimation.setFillAfter(true);
                rotate3dAnimation.setInterpolator(new AccelerateInterpolator());
                rotate3dAnimation.setAnimationListener(new TurnToImageView());
                binding.layout.startAnimation(rotate3dAnimation);
            }
        });

        binding.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取布局的中心点位置，作为旋转的中心点
                float centerX = binding.layout.getWidth() / 2f;
                float centerY = binding.layout.getHeight() / 2f;
                // 构建3D旋转动画对象，旋转角度为360到270度，这使得ImageView将会从可见变为不可见，并且旋转的方向是相反的
                final Rotate3dAnimation rotation = new Rotate3dAnimation(360, 270, centerX, centerY, 310.0f, true);
                // 动画持续时间500毫秒
                rotation.setDuration(5000);
                // 动画完成后保持完成的状态
                rotation.setFillAfter(true);
                rotation.setInterpolator(new AccelerateInterpolator());
                // 设置动画的监听器
                rotation.setAnimationListener(new TurnToListView());
                binding.layout.startAnimation(rotation);
            }
        });

    }

    /**
     * 初始化图片列表数据。
     */
    private void initPics() {
        Picture spring = new Picture("spring", R.drawable.spring);
        picList.add(spring);
        Picture summer = new Picture("summer", R.drawable.summer);
        picList.add(summer);
        Picture autumn = new Picture("Autumn", R.drawable.fall);
        picList.add(autumn);
        Picture winter = new Picture("winter", R.drawable.winter);
        picList.add(winter);
    }

    class TurnToImageView implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // 获取布局的中心点位置，作为旋转的中心点
            float centerX = binding.layout.getWidth() / 2f;
            float centerY = binding.layout.getHeight() / 2f;
            binding.picListView.setVisibility(View.GONE);
            // 将ImageView显示
            binding.picture.setVisibility(View.VISIBLE);
            binding.picture.requestFocus();
            // 构建3D旋转动画对象，旋转角度为270到360度，这使得ImageView将会从不可见变为可见
            final Rotate3dAnimation rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 310.0f, false);
            // 动画持续时间500毫秒
            rotation.setDuration(5000);
            // 动画完成后保持完成的状态
            rotation.setFillAfter(true);
            rotation.setInterpolator(new AccelerateInterpolator());
            // 设置动画的监听器
            binding.layout.startAnimation(rotation);


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    /**
     * 注册在ImageView点击动画中的动画监听器，用于完成ImageView的后续动画。
     *
     * @author guolin
     */
    class TurnToListView implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        /**
         * 当ImageView的动画完成后，还需要再启动ListView的动画，让ListView从不可见变为可见
         */
        @Override
        public void onAnimationEnd(Animation animation) {
            // 获取布局的中心点位置，作为旋转的中心点
            float centerX = binding.layout.getWidth() / 2f;
            float centerY = binding.layout.getHeight() / 2f;
            // 将ImageView隐藏
            binding.picture.setVisibility(View.GONE);
            // 将ListView显示
            binding.picListView.setVisibility(View.VISIBLE);
            binding.picListView.requestFocus();
            // 构建3D旋转动画对象，旋转角度为90到0度，这使得ListView将会从不可见变为可见，从而回到原点
            final Rotate3dAnimation rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false);
            // 动画持续时间500毫秒
            rotation.setDuration(5000);
            // 动画完成后保持完成的状态
            rotation.setFillAfter(true);
            rotation.setInterpolator(new AccelerateInterpolator());
            binding.layout.startAnimation(rotation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    }

}
