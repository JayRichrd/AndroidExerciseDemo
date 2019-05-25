package cain.tencent.com.androidexercisedemo.utils

import io.reactivex.ObservableTransformer
import java.util.concurrent.TimeUnit

/**
 * @author cainjiang
 * @date 2019-05-25
 */
object RxUtils {
    @JvmStatic
    fun <T> preventDuplicateClicksTransformer(interval: Long): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.throttleFirst(interval, TimeUnit.MILLISECONDS)
        }
    }
}