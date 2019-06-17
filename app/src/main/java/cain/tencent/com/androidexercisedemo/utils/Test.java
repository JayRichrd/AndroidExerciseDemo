package cain.tencent.com.androidexercisedemo.utils;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author cainjiang
 * @date 2019/4/16
 */
public class Test {
    public static final String TAG = "Test";

    public void test() {
        // 新建一个线程并开启它
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "---Test#testThread#run---");
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void threadPoolTest() {
        // 新建一个线程并开启它
        Runnable command = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "---Test#testThread#run---");
            }
        };

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                10,
                200,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));
        executor.execute(command);
        executor.shutdown();
    }

}