package cain.tencent.com.androidexercisedemo

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import cain.tencent.com.androidexercisedemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_main, null, false)
        initView()
        setContentView(binding.root)
    }

    private fun initView() {
        binding.btnFresco.setOnClickListener(this)
        binding.btnThread.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_fresco -> {
                startActivity(Intent(this, FrescoActivity::class.java))
            }
            R.id.btn_thread -> {
                startThread()
            }
            else -> {

            }
        }
    }

    private fun startThread() {
        val thread = Thread(Runnable {
            Log.i("Jay", "创建线程了.")
            Thread.sleep(2 * 1000)
        })
        thread.name = "jay-thread"
        thread.start()
    }
}
