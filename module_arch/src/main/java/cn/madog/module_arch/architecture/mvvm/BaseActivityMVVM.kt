package cn.madog.module_arch.architecture.mvvm

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.madog.module_arch.entity.ModelActionEvent
import cn.madog.module_arch.entity.TipsStateEvent
import cn.madog.module_arch.extend.toast
import cn.madog.module_arch.ui.BaseActivity

/**
 * @description
 *
 * @author leon.w
 * @create 2019/4/3 10:30 AM
 * @update 2019/4/3 10:30 AM
 * @version 1
 */
abstract class BaseActivityMVVM : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    /**
     * 设置当前页面的ViewModel
     * @param viewModel 继承至 {@link(BaseViewModel)}
     */
    protected fun <VM : BaseViewModel> setViewModel(viewModel: Class<VM>): VM {
        val vm = ViewModelProviders.of(this).get(viewModel)
        handleActionEvent(vm)
        return vm
    }

    private fun <VM : BaseViewModel> handleActionEvent(vm: VM) {
        vm.getUiState().observe(this, Observer {

            when (it) {
                is TipsStateEvent -> {
                    handleTipsActionEvent(it)
                }
                else -> handleOtherActionEvent(it)
            }
        })
    }

    private fun handleTipsActionEvent(event: TipsStateEvent) {
        when (event.state) {
            TipsStateEvent.UI_STATE_PROCESSED -> {
                showWarningMessage(event.message)
            }

            TipsStateEvent.UI_STATE_SUCCESS -> {
                showSuccessMessage(event.message,event.confirmText)
            }

            TipsStateEvent.UI_STATE_ERROR -> {
                showErrorMessage(event.message)
            }
            TipsStateEvent.UI_STATE_WARNING -> {
                showWarningCancelMessage(event.message,event.cancelText)
            }
            TipsStateEvent.UI_STATE_DISMISS_SHOW_TOAST -> {
                dismissDialog()
                toast(event.message)
            }
            TipsStateEvent.UI_STATE_SHOW_TOAST -> {
                toast(event.message)
            }
            else -> {
                dismissDialog()
            }
        }
    }

    /**
     * 处理其他没有预处理的动作事件
     */
    protected fun handleOtherActionEvent(actionEvent: ModelActionEvent) {

    }

}