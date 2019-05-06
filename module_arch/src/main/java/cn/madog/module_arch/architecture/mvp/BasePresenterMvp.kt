package cn.madog.module_arch.architecture.mvp

import cn.madog.module_arch.architecture.data.BaseDataSource
import java.lang.ref.WeakReference

abstract class BasePresenterMvp< V : IContractMvp.IView> : IContractMvp.IPresenter<V> {

    private var mView: WeakReference<V>? = null

    private var repositories: MutableList<BaseDataSource>? = mutableListOf()

    fun <RS : BaseDataSource> setRepository(repository: RS): RS {
        repositories?.add(repository)
        return repository
    }

    override fun create() {
    }

    override fun getView(): V? {
        return mView?.get()
    }

    override fun attachView(view: V) {
        mView = WeakReference(view)
    }

    override fun detachView() {
        mView?.clear()
        mView = null
    }

    override fun destroy() {
        detachView()
        repositories?.forEach {
            it.onDestroy()
        }
        repositories?.clear()
        repositories = null
    }
}