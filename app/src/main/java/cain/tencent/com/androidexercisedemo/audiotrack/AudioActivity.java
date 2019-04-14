package cain.tencent.com.androidexercisedemo.audiotrack;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cain.tencent.com.androidexercisedemo.R;
import cain.tencent.com.androidexercisedemo.databinding.ActivityAudioTrackBinding;

import static cain.tencent.com.androidexercisedemo.audiotrack.GlobalConfig.AUDIO_FORMAT;
import static cain.tencent.com.androidexercisedemo.audiotrack.GlobalConfig.CHANNEL_CONFIG;
import static cain.tencent.com.androidexercisedemo.audiotrack.GlobalConfig.SAMPLE_RATE_INHZ;

public class AudioActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAudioTrackBinding binding;
    public static final String RECORD_PCM_FILE_NAME = "recored_test.pcm";
    public static final String RECORD_OPENSL_PCM_FILE_NAME = "record_opensl_test.pcm";
    public static final String RECORD_WAV_FILE_NAME = "recored_test.wav";
    public static final String TAG = "AudioActivity";
    private static final int MY_PERMISSIONS_REQUEST = 1001;

    private volatile boolean isRecording;
    private AudioRecord audioRecord;
    private AudioTrack audioTrack;

    /**
     * 需要申请的运行时权限
     */
    private String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /**
     * 被用户拒绝的权限列表
     */
    private List<String> mPermissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_audio_track, null, false);
        setContentView(binding.getRoot());
        binding.btnRecord.setOnClickListener(this);
        binding.btnOpenslRecord.setOnClickListener(this);
        binding.btnTransform.setOnClickListener(this);
        binding.btnPlay.setOnClickListener(this);
        binding.btnOpenslPlay.setOnClickListener(this);

        getPermissions();
    }

    /**
     * 检查并申请权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_DENIED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            if (!mPermissionList.isEmpty()) {
                String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
                ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_record:
                Button recordBtn = (Button) v;
                if (recordBtn.getText().equals("录音")) {
                    recordBtn.setText("停止");
                    recordAudio();
                } else {
                    recordBtn.setText("录音");
                    stopRecord();
                }
                break;
            case R.id.btn_opensl_record:
                Button recordOpenslBtn = (Button) v;
                if (recordOpenslBtn.getText().equals("录音")) {
                    recordOpenslBtn.setText("停止");
                    openslRecordAudio();
                } else {
                    recordOpenslBtn.setText("录音");
                    stopOpenslRecord();
                }
                break;
            case R.id.btn_transform:
                transform();
                break;
            case R.id.btn_play:
                Button playBtn = (Button) v;
                if (playBtn.getText().equals("播放")) {
                    playBtn.setText("停止");
                    play();
                } else {
                    playBtn.setText("播放");
                    stopPlay();
                }
                break;
            case R.id.btn_opensl_play:

                break;
        }
    }

    private void stopOpenslRecord() {
        isRecording = false;
        nativeStopOpenslRecord();
    }

    private void openslRecordAudio() {
        if (isRecording){
            return;
        }
        isRecording = true;
        final File file = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), RECORD_OPENSL_PCM_FILE_NAME);
        if (!file.mkdirs()) {
            Log.e(TAG, "directory not created!");
            return;
        }
        if (file.exists()) {
            file.delete();
        }
        nativeOpenslRecord(file.getAbsolutePath());
    }

    /**
     * 停止播放
     * 释放资源
     */
    private void stopPlay() {
        if (audioTrack != null && audioTrack.getState() != AudioTrack.STATE_UNINITIALIZED) {
            // 4.停止播放释放资源
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }
    }

    /**
     * 停止录音
     * 释放资源
     */
    private void stopRecord() {
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    /**
     * 使用AudioTrack播放录音数据
     * 使用stream模式
     */
    private void play() {
        // 声道，与录音时候的对应
        int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        // 最小缓存大小
        final int minBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE_INHZ, channelConfig, AUDIO_FORMAT);
        // 1.构建一个AudioTrack实例
        audioTrack = new AudioTrack(
                new AudioAttributes.Builder().
                        setUsage(AudioAttributes.USAGE_MEDIA).
                        setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).
                        build(),
                new AudioFormat.Builder().
                        setSampleRate(SAMPLE_RATE_INHZ).
                        setEncoding(AUDIO_FORMAT).
                        setChannelMask(channelConfig).
                        build(),
                minBufferSize, AudioTrack.MODE_STREAM, AudioManager.AUDIO_SESSION_ID_GENERATE);

        if (audioTrack.getState() != AudioTrack.STATE_UNINITIALIZED) {
            // 2.开始播放
            audioTrack.play();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), RECORD_PCM_FILE_NAME);
                    FileInputStream fin = new FileInputStream(file);
                    while (fin.available() > 0) { // 3.使用循环不断向AudioTrack缓冲区写入数据
                        byte[] tempBuffer = new byte[minBufferSize];
                        int readCount = fin.read(tempBuffer);
                        if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack.ERROR_BAD_VALUE) {
                            continue;
                        }
                        if (readCount != 0 && readCount != -1) {
                            audioTrack.write(tempBuffer, 0, readCount);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG,"play error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        },"PlayThread").start();
    }

    private void transform() {
        PcmToWavUtil pcmToWavUtil = new PcmToWavUtil(SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT);
        File pcmFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), RECORD_PCM_FILE_NAME);
        File wavFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), RECORD_WAV_FILE_NAME);
        if (!wavFile.mkdirs()) {
            Log.e(TAG, "directory not created!");
            return;
        }
        if (wavFile.exists()) {
            wavFile.delete();
        }
        pcmToWavUtil.pcmToWav(pcmFile.getAbsolutePath(), wavFile.getAbsolutePath());
    }

    /**
     * 录音
     */
    private void recordAudio() {
        // 获取最新缓冲大小
        final int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT);
        // 1.构建一个录音实例
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT, minBufferSize);
        // 录音数据
        final byte data[] = new byte[minBufferSize];
        // 录音文件
        final File file = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), RECORD_PCM_FILE_NAME);
        if (!file.mkdirs()) {
            Log.e(TAG, "directory not created!");
            return;
        }
        if (file.exists()) {
            file.delete();
        }
        // 2.开始录音
        audioRecord.startRecording();
        isRecording = true;
        // 3.开启一个新线程将数据写入录音文件
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    if (fos != null) {
                        while (isRecording) { // 使用一个循环不断将录音数据写入文件
                            int read = audioRecord.read(data, 0, minBufferSize);
                            if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                                fos.write(data);
                            }
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG,"record error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        },"RecordThread").start();
    }

    native int nativeOpenslRecord(String filePath);
    native int nativeStopOpenslRecord();
}
