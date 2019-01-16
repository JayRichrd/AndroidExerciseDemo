package cain.tencent.com.androidexercisedemo

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.util.Log
import com.facebook.drawee.backends.pipeline.DraweeConfig
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilderSupplier
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerFactory

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val builder = DraweeConfig.newBuilder()
        builder.setDrawDebugOverlay(true)
//        builder.setPipelineDraweeControllerFactory(PipelineDraweeControllerFactory())
        val draweeConfig = builder.build()

        Fresco.initialize(this, null, draweeConfig)
        QGameSimpleDraweeView.initialize(PipelineDraweeControllerBuilderSupplier(this, draweeConfig))
        QGameDraweeView.Companion.initialize(PipelineDraweeControllerBuilderSupplier(this, draweeConfig))
        val maxMemory = Runtime.getRuntime().maxMemory() / 1024
        Log.i("MyApplication","Max memory is = " + maxMemory + "k")
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}