package cain.tencent.com.androidexercisedemo

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import cain.tencent.com.androidexercisedemo.databinding.ActivityActionBarBinding

@SuppressLint("ActivityRouterAnnotationDetector")
class ComboActivity : AppCompatActivity() {
    lateinit var binding: ActivityActionBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_action_bar, null, false)
        setContentView(binding.root)
    }
}
