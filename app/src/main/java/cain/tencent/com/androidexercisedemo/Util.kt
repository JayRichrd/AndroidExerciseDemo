@file:JvmName("CommonUtils")

package cain.tencent.com.androidexercisedemo

import android.content.Context
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
