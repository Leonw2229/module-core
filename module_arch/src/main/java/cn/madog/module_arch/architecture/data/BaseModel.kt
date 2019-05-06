package cn.madog.module_arch.architecture.data

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * 基础的Model抽象，内部暂时只处理了RxJava的取消订阅
 * @author leon.w
 */
open class BaseModel : BaseDataSource {

    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    fun getCompositeDisposable(): CompositeDisposable? {
        return compositeDisposable
    }
    override fun onCreate() {

    }

    fun <DS : Disposable> addToComposites(disposable: DS): DS {
        compositeDisposable?.add(disposable)
        return disposable
    }

    override fun onDestroy() {
        compositeDisposable?.dispose()
        compositeDisposable?.clear()
        compositeDisposable = null
    }
}