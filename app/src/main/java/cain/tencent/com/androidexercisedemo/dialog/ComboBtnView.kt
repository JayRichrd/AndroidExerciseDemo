package cain.tencent.com.androidexercisedemo.dialog

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import cain.tencent.com.androidexercisedemo.utils.ImageUtil
import java.util.*

/**
 * @author cainjiang
 * @date 2019/2/22
 */
class ComboBtnView : View, ImageUtil.DecodeResourceCallback {

    companion object {
        const val COMBO_DURATION = 3000L // ms  连击时间
        const val WAVE_DURATION = 500L // ms 波纹动画时间
    }

    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs)
    constructor(ctx: Context) : this(ctx, null)

    /**
     * 系统参数，屏幕密度
     */
    private var mScreenScale: Float = 3.0F
    private var mDuration: Long = 0L // 整个按钮倒计时动画
    private var mWaveDuration: Long = 0L // 波纹动画时间
    private var mWaveAnimQueue: Queue<WaveAnimHelper> // 波纹动画队列
    private var mWaveAnimQueueCache: Queue<WaveAnimHelper> // wave的缓存
    private var mWaveAnimCache: SparseArray<Paint>// 波纹RadialGradient缓存
    private var mIsAnimStart: Boolean = false

    init {
        mScreenScale = context.resources.displayMetrics.density
        // 动画参数初始化
        mDuration = COMBO_DURATION
        mWaveDuration = WAVE_DURATION
        mWaveAnimQueue = LinkedList()
        mWaveAnimQueueCache = LinkedList()
        mWaveAnimCache = SparseArray()
        mIsAnimStart = false
    }

    override fun onSucess(bitmap: Bitmap, resId: Int) {

    }

    /**
     * 波纹动画帮助类
     */
    data class WaveAnimHelper(var mRadius: Float = 0F) {
        var mPaint: Paint? = null
        var mBeginTime: Long = 0
    }
}