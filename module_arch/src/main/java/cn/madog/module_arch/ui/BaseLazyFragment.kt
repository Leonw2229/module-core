package cn.madog.module_arch.ui

import android.os.Bundle
import android.view.View

/**
 * 数据懒加载，用于 viewPager 里的 Fragment 数据加载
 */
abstract class BaseLazyFragment : BaseFragment() {

    abstract fun lazyLoadData()

    private var isCreateView: Boolean = false
    private var isVisibleToUserState: Boolean = false
    private var isLoadData: Boolean = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        isVisibleToUserState = isVisibleToUser
        if (isVisibleToUser) {
            loadData()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCreateView = true

    }


    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun loadData() {
        if (isVisibleToUserState and isCreateView and !isLoadData) {
            isLoadData = true
            lazyLoadData()
        }
    }
}