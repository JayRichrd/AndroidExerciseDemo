@file:JvmName("CommonUtils")

package cain.tencent.com.androidexercisedemo.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * @author cainjiang
 * @date 2018/11/3
 */
var longToast: Toast? = null
var shortToast: Toast? = null

fun showLongToast(contex: Context, content: String) {
    if (longToast == null) {
        longToast = Toast.makeText(contex, content, Toast.LENGTH_LONG)
    } else {
        longToast?.setText(content)
    }
    longToast?.show()
}

fun showShortToast(contex: Context, content: String) {
    if (shortToast == null) {
        shortToast = Toast.makeText(contex, content, Toast.LENGTH_SHORT)
    } else {
        shortToast?.setText(content)
    }
    shortToast?.show()
}



inline fun <reified T : Activity> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}
