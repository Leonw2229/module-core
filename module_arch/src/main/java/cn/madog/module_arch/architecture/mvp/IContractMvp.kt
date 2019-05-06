package cn.madog.module_arch.architecture.mvp

import android.content.Context

interface IContractMvp {

    interface IView {
        fun getViewContext(): Context
        fun showError(code: String?,message: String?)
        fun showWarningMessage(message: String)
    }

    interface IPresenter<V : IView> {
        fun create()
        fun attachView(view: V)
        fun getView():V?
        fun detachView()
        fun destroy()
    }

}