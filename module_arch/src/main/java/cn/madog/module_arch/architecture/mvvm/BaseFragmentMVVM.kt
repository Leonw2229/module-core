package cn.madog.module_arch.architecture.mvvm

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.madog.module_arch.entity.ModelActionEvent
import cn.madog.module_arch.entity.TipsStateEvent
import cn.madog.module_arch.extend.toast
import cn.madog.module_arch.ui.BaseFragment

/**
 * @description
 *
 * @author leon.w
 * @create 2019/4/3 5:03 PM
 * @update 2019/4/3 5:03 PM
 * @version 1
 */
open class BaseFragmentMVVM: BaseFragment(){

    /**
     * 设置当前页面的ViewModel,可以调用多次，不负责保存 ViewModel实例，需自己保存，只是处理了常用的事件，比如弹框
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
                context?.toast(event.message)
            }
            TipsStateEvent.UI_STATE_SHOW_TOAST -> {
                context?.toast(event.message)
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