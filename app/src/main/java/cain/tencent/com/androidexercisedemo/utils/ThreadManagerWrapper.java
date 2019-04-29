package cain.tencent.com.androidexercisedemo.utils;

import android.util.Log;

import com.tencent.qgame.component.utils.thread.IThreadListener;
import com.tencent.qgame.component.utils.thread.ThreadManager;
import com.tencent.qgame.component.utils.thread.ThreadPriority;

public class ThreadManagerWrapper {
    public static final String TAG = "ThreadManagerWrapper";

    // 线程添加监听
    static IThreadListener mThreadListener = new IThreadListener() {
        @Override
        public void onAdded() {

        }

        @Override
        public void onPreRun() {

        }

        @Override
        public void onPostRun() {

        }
    };

    /**
     * 反射Thread的相关配置
     */
    public static final String THREAD = "java.lang.Thread";
    public static final String THREAD_FIELD_TARGET = "target";

    /**
     * 使用反射的方式获取到Thread的Runnable
     * 然后将这个Runnable添加到线程池中
     *
     * @param thread 待收归的线程
     */
//    public static void start(Thread thread) {
//        try {
//            Class clazz = Class.forName(THREAD);
//            Field field = clazz.getDeclaredField(THREAD_FIELD_TARGET);
//            field.setAccessible(true);
//            Object runnableObj = field.get(thread);
//            if (runnableObj instanceof Runnable) {
//                Runnable runnable = (Runnable) runnableObj;
//                Log.i(TAG, "---thread pool start---");
//                ThreadManager.post(runnable, ThreadPriority.NORMAL, mThreadListener, true);
//            }
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            Log.e(TAG, "reflect Thread error: " + e.toString());
//            thread.start();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            Log.e(TAG, "reflect Thread error: " + e.toString());
//            thread.start();
//
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//            Log.e(TAG, "reflect Thread error: " + e.toString());
//            thread.start();
//        } catch (Throwable e) {
//            Log.e(TAG, "reflect Thread error: " + e.toString());
//        }
//    }
    public static void start(Runnable thread) {
        Log.i(TAG, "---thread pool start---");
        ThreadManager.post(thread, ThreadPriority.NORMAL, mThreadListener, true);
    }
}
