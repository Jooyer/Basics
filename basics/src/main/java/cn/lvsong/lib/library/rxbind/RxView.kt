package cn.lvsong.lib.library.rxbind

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.CheckResult
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


/**
 * Desc: https://www.jianshu.com/p/01aa6e93c98c
 * Author: Jooyer
 * Date: 2018-09-21
 * Time: 15:22
 */
object RxView {

    private val sCompositeDisposable = CompositeDisposable()

    /**
     * 防止重复点击
     *
     * @param skipDuration --> 过滤间隔时间
     * @param action       监听器
     * @param targets      目标view
     */
    @SuppressLint("CheckResult")
    fun setOnClickListeners(
        action: OnFilterClick, vararg targets: View,skipDuration: Long = 1000) {
        for (view in targets) {
            onClick(view)
                .throttleFirst(skipDuration, TimeUnit.MILLISECONDS)
                .subscribe { v -> action.onClick(v) }
        }
    }


    /**
     * 监听onclick事件防抖动
     *
     * @param view
     * @return
     */
    @CheckResult
    private fun onClick(view: View): Observable<View> {
        Preconditions.checkNotNull<Any>(view, "view == null")
        return Observable.create(ViewClickOnSubscribe(view))
    }


    /**
     * onclick事件防抖动
     * 返回view
     */
    private class ViewClickOnSubscribe(private val view: View) : ObservableOnSubscribe<View> {
        @Throws(Exception::class)
        override fun subscribe(emitter: ObservableEmitter<View>) {
            Preconditions.checkUiThread()
            view.setOnClickListener {
                if (!emitter.isDisposed) {
                    emitter.onNext(view)
                }
            }
        }
    }


    /**
     * 点击事件转发接口
     *
     * @param <T> View 类型参数
    </T> */
    interface OnFilterClick {
        fun onClick(view: View)
    }


}
