package cain.tencent.com.androidexercisedemo.utils;

import android.util.Log;

/**
 * @author cainjiang
 * @date 2019/4/16
 */
public class Test {
    public static final String TAG = "Test";

    public void test() {
        // 新建一个线程并开启它
        Runnable runnable =  new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "---Test#testThread#run---");
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

}