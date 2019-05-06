package cn.madog.module_arch.architecture.mvp

import android.content.Context
import android.os.Bundle
import cn.madog.module_arch.extend.toast
import cn.madog.module_arch.ui.BaseActivity

/**
 * 基础的MVP架构简单封装，
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseActivityMvp<V : IContractMvp.IView, P : IContractMvp.IPresenter<V>> : BaseActivity(), IContractMvp.IView {

    private var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = initPresenter()
        mPresenter?.create()
        mPresenter?.attachView(this as V)
    }

    fun getPresenter(): P? {
        if (mPresenter == null) {
            mPresenter = initPresenter()
        }
        return mPresenter
    }

    abstract fun initPresenter(): P?

    override fun showError(code: String?, message: String?) {
        toast(message.orEmpty())
    }

    override fun getViewContext(): Context {
        return this
    }

    override fun onDestroy() {
        getPresenter()?.destroy()
        super.onDestroy()
    }
}
