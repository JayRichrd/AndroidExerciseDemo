package cain.tencent.com.androidexercisedemo

import android.app.Application
import com.facebook.drawee.backends.pipeline.DraweeConfig
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilderSupplier
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerFactory

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val builder = DraweeConfig.newBuilder()
        builder.setDrawDebugOverlay(true)
        val draweeConfig = builder.build()

        builder.setPipelineDraweeControllerFactory(PipelineDraweeControllerFactory())
        Fresco.initialize(this, null, draweeConfig)
        QGameSimpleDraweeView.initialize(PipelineDraweeControllerBuilderSupplier(this, draweeConfig))
    }
}