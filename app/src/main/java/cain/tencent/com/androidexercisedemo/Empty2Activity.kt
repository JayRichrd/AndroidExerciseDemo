package cain.tencent.com.androidexercisedemo

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import cain.tencent.com.androidexercisedemo.databinding.ActivityEmpty2Binding

class Empty2Activity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityEmpty2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_empty2, null, false)
        setContentView(binding.root)
    }

    override fun onClick(v: View?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Jay", "onDestroy")
    }
}
