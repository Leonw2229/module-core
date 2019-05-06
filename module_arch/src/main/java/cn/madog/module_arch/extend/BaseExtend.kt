package cn.madog.module_arch.extend

import android.content.Context
import com.blankj.utilcode.util.ToastUtils
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


fun Context.toast(message: CharSequence) = ToastUtils.showShort(message)
fun Context.toast(message: String) = ToastUtils.showShort(message)


fun <T> Observable<T>.schedulers(): Observable<T> {
    return subscribeOn(io.reactivex.schedulers.Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
}

/**
 * 内部调用了线程转换
 */
fun <T> Observable<T>.subscribeResult(observer: Observer<T>) {
    this.schedulers().subscribe(observer)
}

fun <T> Flowable<T>.schedulers(): Flowable<T> {
    return subscribeOn(io.reactivex.schedulers.Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.schedulers(): Single<T> {
    return subscribeOn(io.reactivex.schedulers.Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
}
