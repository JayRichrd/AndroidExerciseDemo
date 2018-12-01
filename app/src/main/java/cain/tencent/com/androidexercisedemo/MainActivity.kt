package cain.tencent.com.androidexercisedemo

import android.annotation.SuppressLint
import android.content.ComponentCallbacks2
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import cain.tencent.com.androidexercisedemo.databinding.ActivityMainBinding

@SuppressLint("ActivityRouterAnnotationDetector")
class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding

    companion object {
        const val TAG = "MainActivity"
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
    }

    @SuppressLint("HardcodedStringDetector")
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_fresco -> {
                startActivity(Intent(this, FrescoActivity::class.java))
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
            else -> {

            }
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
