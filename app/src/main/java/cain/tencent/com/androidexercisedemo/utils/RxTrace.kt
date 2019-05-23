package cain.tencent.com.androidexercisedemo.utils

import android.util.Log
import io.reactivex.ObservableTransformer
import java.util.function.Function

/**
 * @author cainjiang
 * @date 2019-05-23
 */
object RxTrace {
    @JvmField
    val LOG_NEXT_DATA = 1
    @JvmField
    val LOG_NEXT_EVENT = 2
    @JvmField
    val LOG_ERROR = 4
    @JvmField
    val LOG_COMPLETE = 8
    @JvmField
    val LOG_SUBSCRIBE = 16
    @JvmField
    val LOG_TERMINATE = 32
    @JvmField
    val LOG_DISPOSE = 64

    @JvmStatic
    fun <T> logObservable(tag: String, bitMask: Int): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->

            var upstream = upstream
            if (bitMask and LOG_SUBSCRIBE > 0) {
                upstream = upstream.compose(oLogSubscribe(tag))
            }
            if (bitMask and LOG_TERMINATE > 0) {
                upstream = upstream.compose(oLogTerminate(tag))
            }
            if (bitMask and LOG_ERROR > 0) {
                upstream = upstream.compose(oLogError(tag))
            }
            if (bitMask and LOG_COMPLETE > 0) {
                upstream = upstream.compose(oLogComplete(tag))
            }
            if (bitMask and LOG_DISPOSE > 0) {
                upstream = upstream.compose(oLogDispose(tag))
            }
            if (bitMask and LOG_NEXT_DATA > 0) {
                upstream = upstream.compose(oLogNext(tag))
            }
            if (bitMask and LOG_NEXT_EVENT > 0) {
                upstream = upstream.compose(oLogNextEvent(tag))
            }
            upstream
        }
    }

    @JvmStatic
    fun <T> log(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.compose(oLogAll(tag)).compose(oLogNext(tag))
        }
    }

    private fun <T> oLogAll(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.compose(oLogError(tag)).compose(oLogComplete(tag)).compose(oLogSubscribe(tag)).compose(oLogTerminate(tag)).compose(oLogDispose(tag))
        }
    }

    private fun <T> oLogNext(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.doOnNext { onNext ->
                Log.i(tag, String.format("[onNext] -> %s", onNext))
            }
        }
    }

    private fun <T> oLogNextEvent(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.doOnNext { onNext ->
                Log.i(tag, String.format("[onNext]"))
            }
        }
    }

    private fun <T> oLogError(tag: String): ObservableTransformer<T, T> {
        val message = Function<Throwable, String> { throwable ->
            if (throwable.message != null) throwable.message!! else throwable.javaClass.simpleName
        }
        return ObservableTransformer { upstream ->
            upstream.doOnError { onError ->
                Log.i(tag, String.format("[onNext] -> %s", message.apply(onError)))
            }
        }
    }

    private fun <T> oLogComplete(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.doOnComplete {
                Log.i(tag, String.format("[onComplete]"))
            }
        }
    }

    private fun <T> oLogSubscribe(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.doOnSubscribe {
                Log.i(tag, String.format("[subscribe]"))
            }
        }
    }

    private fun <T> oLogTerminate(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.doOnTerminate {
                Log.i(tag, String.format("[terminate]"))
            }
        }
    }

    private fun <T> oLogDispose(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.doOnDispose {
                Log.i(tag, String.format("[dispose]"))
            }
        }
    }
}