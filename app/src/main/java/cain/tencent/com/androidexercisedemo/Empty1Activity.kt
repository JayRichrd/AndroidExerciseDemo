package cain.tencent.com.androidexercisedemo

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import cain.tencent.com.androidexercisedemo.databinding.ActivityEmpty1Binding

class Empty1Activity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityEmpty1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_empty1, null, false)
        setContentView(binding.root)
        binding.btnNewActivity.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_new_activity -> {
                startActivity(Intent(this, Empty2Activity::class.java))
            }
            else -> {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Jay", "onDestroy")
    }
}
