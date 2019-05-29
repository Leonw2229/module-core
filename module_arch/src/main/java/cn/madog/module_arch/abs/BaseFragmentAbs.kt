package cn.madog.module_arch.abs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gyf.barlibrary.ImmersionBar

/**
 * @description
 *
 * @author leon.w
 * @create 2019/3/27 5:58 PM
 * @update 2019/3/27 5:58 PM
 * @version 1
 */
abstract class BaseFragmentAbs: Fragment(){

    abstract fun showWarningMessage(message: String)

    abstract fun showSuccessMessage(message: String, confirmText: String = "确认")

    abstract fun showErrorMessage(message: String)

    abstract fun showWarningCancelMessage(message: String, cancelText: String = "取消")


    abstract fun showWarningCancelOrConfirmMessage(message: String, confirmText: String = "确认",cancelText: String = "取消" )

    abstract fun showWarningConfirmMessage(message: String, confirmText: String = "确认")

    abstract fun dismissDialog()

    abstract fun releaseDialog()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ImmersionBar.with(this).init()
    }

    override fun onDestroyView() {
        try {
            ImmersionBar.with(this).destroy()
        } catch (e: Exception) {
        }
        releaseDialog()
        super.onDestroyView()
    }
}