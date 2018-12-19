package cain.tencent.com.androidexercisedemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.jetbrains.anko.find

class Anko1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anko1)
        val text = find<TextView>(R.id.tv_text)
        text.text = "Hello World"
    }
}
