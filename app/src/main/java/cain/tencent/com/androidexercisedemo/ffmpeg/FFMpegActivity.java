package cain.tencent.com.androidexercisedemo.ffmpeg;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cain.tencent.com.androidexercisedemo.R;
import cain.tencent.com.androidexercisedemo.databinding.ActivityFfmpegBinding;

@SuppressLint("ActivityRouterAnnotationDetector")
public class FFMpegActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityFfmpegBinding binding;
    String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_ffmpeg, null, false);
        setContentView(binding.getRoot());
        binding.btnCodec.setOnClickListener(this);
        binding.btnFilter.setOnClickListener(this);
        binding.btnFormat.setOnClickListener(this);
        binding.btnProtocol.setOnClickListener(this);
        binding.btnPlayer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_codec:
                binding.tvInfo.setText(avCodecInfo());
                break;
            case R.id.btn_filter:
                binding.tvInfo.setText(avFilterInfo());
                break;
            case R.id.btn_format:
                binding.tvInfo.setText(avFormatInfo());
                break;
            case R.id.btn_protocol:
                binding.tvInfo.setText(urlProtocolInfo());
                break;
            case R.id.btn_player:
                binding.ffVideoPlayer.play(url);
                break;
        }
    }

    public native String urlProtocolInfo();

    public native String avFormatInfo();

    public native String avCodecInfo();

    public native String avFilterInfo();
}
