package cain.tencent.com.androidexercisedemo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.util.Log
import cain.tencent.com.androidexercisedemo.utils.Test
import com.facebook.drawee.backends.pipeline.DraweeConfig
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilderSupplier
import com.tencent.base.Global
import com.tencent.qgame.component.wns.WnsManager

class MyApplication : Application() {
    @SuppressLint("HardcodedStringDetector")
    override fun onCreate() {
        super.onCreate()
        Test().test()
        val builder = DraweeConfig.newBuilder()
        builder.setDrawDebugOverlay(true)
//        builder.setPipelineDraweeControllerFactory(PipelineDraweeControllerFactory())
        val draweeConfig = builder.build()

        Fresco.initialize(this, null, draweeConfig)
        QGameSimpleDraweeView.initialize(PipelineDraweeControllerBuilderSupplier(this, draweeConfig))
        QGameDraweeView.Companion.initialize(PipelineDraweeControllerBuilderSupplier(this, draweeConfig))
        val maxMemory = Runtime.getRuntime().maxMemory() / 1024
        Log.i("MyApplication", "Max memory is = " + maxMemory + "k")
        val ndkBridge = NDKBridge()
        Log.d("NDK", "从c++层获取的数据：${ndkBridge.str}")
        Log.d("NDK", "lame版本：${ndkBridge.lameVersion}")
        WnsManager.getInstance().initWnsAndVolley(this, 202610, "")
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Global.init(this)
        MultiDex.install(this)
    }
}