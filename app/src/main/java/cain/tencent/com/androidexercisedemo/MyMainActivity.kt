package cain.tencent.com.androidexercisedemo

import android.annotation.SuppressLint
import android.content.ComponentCallbacks2
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import cain.tencent.com.androidexercisedemo.animator.AnimatorActivity
import cain.tencent.com.androidexercisedemo.audiotrack.AudioActivity
import cain.tencent.com.androidexercisedemo.bitmap.*
import cain.tencent.com.androidexercisedemo.cache.CacheActivity
import cain.tencent.com.androidexercisedemo.databinding.ActivityMainBinding
import cain.tencent.com.androidexercisedemo.ffmpeg.FFMpegActivity
import com.alibaba.fastjson.JSON
import java.net.URLDecoder
import cain.tencent.com.androidexercisedemo.utils.startActivity
import java.io.File

@SuppressLint("ActivityRouterAnnotationDetector")
class MyMainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding

    companion object {
        const val TAG = "MyMainActivity"
        const val source_path = "/test.wav"
        const val target_path = "/test.mp3"
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN -> {
                Log.d(TAG, "leave the app.")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_main, null, false)
        initView()
        setContentView(binding.root)
        val urlStr = "%7b%22hv%5fdirection%22%3a0%2c%22provider%22%3a0%2c%22player%5ftype%22%3a0%2c%22play%5furl%22%3a%22%22%2c%22h265%5fplay%5furl%22%3a%22%22%2c%22channel%5fid%22%3a%22%22%2c%22source%22%3a%22%22%2c%22h265%5fdecode%5ftype%22%3a0%2c%22use%5fp2p%22%3afalse%2c%22live%5froom%5fstyle%22%3a0%7d"
        val urlTarget = URLDecoder.decode(urlStr, "utf-8")
        Log.d(TAG, "urlTarget: $urlTarget")
        val prePlayInfo = JSON.parseObject(urlTarget.trim())
        Log.d(TAG, "prePlayInfo: $prePlayInfo")
        if (prePlayInfo.containsKey("live_room_style")) {
            Log.d(TAG, "live_room_style: ${prePlayInfo.getIntValue("live_room_style")}")
        }
    }

    private fun initView() {
        binding.btnFresco.setOnClickListener(this)
        binding.btnThread.setOnClickListener(this)
        binding.btnBitmap.setOnClickListener(this)
        binding.btnTips.setOnClickListener(this)
        binding.btnRxjava.setOnClickListener(this)
        binding.btnGc.setOnClickListener(this)
        binding.btnGetSysInform.setOnClickListener(this)
        binding.btnNewActivity.setOnClickListener(this)
        binding.btnAnimatorDemo.setOnClickListener(this)
        binding.btnOptimizeLayoutDemo.setOnClickListener(this)
        binding.btnCache.setOnClickListener(this)
        binding.btnAnko1.setOnClickListener(this)
        binding.btnFrescoCache.setOnClickListener(this)
        binding.btnFrescoWebpCache.setOnClickListener(this)
        binding.btnActionBar.setOnClickListener(this)
        binding.btnRotate.setOnClickListener(this)
        binding.btnConvert.setOnClickListener(this)
        binding.etSourcePath.setText(Environment.getExternalStorageDirectory().absolutePath + source_path)
        binding.etTargetPath.setText(Environment.getExternalStorageDirectory().absolutePath + target_path)
        binding.btnFfmpeg.setOnClickListener(this)
        binding.btnAudioTrack.setOnClickListener(this)
    }

    @SuppressLint("HardcodedStringDetector")
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_fresco -> {
                startActivity<FrescoActivity>()
            }
            R.id.btn_thread -> {
                startThread()
            }
            R.id.btn_bitmap -> {
                startActivity(Intent(this, BitmapActivity::class.java))
            }
            R.id.btn_tips -> {
                startActivity(Intent(this, TipsActivity::class.java))
            }
            R.id.btn_rxjava -> {
                startActivity(Intent(this, RxJavaActivity::class.java))
            }
            R.id.btn_gc -> {
                Log.d(TAG, "Call gc")
                Runtime.getRuntime().gc()
            }
            R.id.btn_get_sys_inform -> {
                val sysInfo = android.os.Build.MODEL
                val sdkVersion = android.os.Build.VERSION.SDK
                val sysVersion = android.os.Build.VERSION.RELEASE
                val buildVersion = Build.VERSION.SDK_INT
                Log.d(TAG, "机型: ${sysInfo}, sdk版本：${sdkVersion}, 系统版本：${sysVersion}, buildVersion:${buildVersion}")
            }
            R.id.btn_new_activity -> {
                startActivity(Intent(this, Empty1Activity::class.java))
            }
            R.id.btn_animator_demo -> {
                startActivity(Intent(this, AnimatorActivity::class.java))
            }
            R.id.btn_optimize_layout_demo -> {
                startActivity(Intent(this, OptimizeLayoutActivity::class.java))
            }
            R.id.btn_cache -> {
                startActivity(Intent(this, CacheActivity::class.java))
            }
            R.id.btn_anko1 -> {
                startActivity(Intent(this, Anko1Activity::class.java))
            }
            R.id.btn_fresco_cache -> {
                startActivity(Intent(this, FrescoCacheActivity::class.java))
            }
            R.id.btn_fresco_webp_cache -> {
                startActivity(Intent(this, WebPCacheActivity::class.java))
            }
            R.id.btn_action_bar -> {
                startActivity(Intent(this, ComboActivity::class.java))
            }
            R.id.btn_rotate -> {
                startActivity(Intent(this, RotateActivity::class.java))
            }
            R.id.btn_ffmpeg -> {
                startActivity(Intent(this, FFMpegActivity::class.java))
            }
            R.id.btn_audio_track -> {
                startActivity(Intent(this, AudioActivity::class.java))
            }
            R.id.btn_convert -> {
                wav2mp3()
            }
            else -> {

            }
        }
    }

    private fun wav2mp3() {
        if (!TextUtils.isEmpty(binding.etSourcePath.text) && !TextUtils.isEmpty(binding.etTargetPath.text)) {
            Thread {
                // Wav文件头
                val wavFileHeader = WavFileReader()
                if (wavFileHeader.openFile(binding.etSourcePath.text.toString())) {
                    val wavFileHeader = wavFileHeader.getmWavFileHeader()
                    val duration = System.currentTimeMillis();
                    NDKBridge().wav2Mp3(binding.etSourcePath.text.toString(), binding.etTargetPath.text.toString(), wavFileHeader.getmSampleRate())
                    Log.d(TAG, "Wav to mp3 duration: " + (System.currentTimeMillis() - duration))
                }
                val mp3File = File(binding.etTargetPath.text.toString())
                if (mp3File.exists()) {
                    runOnUiThread {
                        Toast.makeText(MyMainActivity@ this, "转码成功" + mp3File.absolutePath, Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }

    @SuppressLint("HardcodedStringDetector")
    private fun startThread() {
        val thread = Thread(Runnable {
            Log.i(TAG, "创建线程了.")
            Thread.sleep(2 * 1000)
        })
        thread.name = "jay-thread"
        thread.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }
}
