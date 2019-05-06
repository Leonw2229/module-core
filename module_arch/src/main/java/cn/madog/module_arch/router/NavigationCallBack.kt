package cn.madog.module_arch.router

import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback

/**
 * @description
 *
 * @author leon.w
 * @create 2019/4/2 11:36 AM
 * @update 2019/4/2 11:36 AM
 * @version 1
 */
open class NavigationCallBack: NavigationCallback{
    override fun onLost(postcard: Postcard) {
    }

    override fun onFound(postcard: Postcard) {
    }

    override fun onInterrupt(postcard: Postcard) {
    }

    override fun onArrival(postcard: Postcard) {
    }

}