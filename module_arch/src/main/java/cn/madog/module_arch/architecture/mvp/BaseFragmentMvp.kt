package cn.madog.module_arch.architecture.mvp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.madog.module_arch.extend.toast
import cn.madog.module_arch.ui.BaseFragment

@Suppress("UNCHECKED_CAST")
abstract class BaseFragmentMvp<V : IContractMvp.IView, out P : IContractMvp.IPresenter<V>> : BaseFragment(), IContractMvp.IView {

    private var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = initPresenter()
        mPresenter?.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mPresenter?.attachView(this as V)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        activity?.toast(message.orEmpty())
    }

    override fun getViewContext(): Context {
        return this.context!!
    }

    override fun onDestroy() {
        getPresenter()?.destroy()
        super.onDestroy()
    }
}