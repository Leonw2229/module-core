package cn.madog.module_arch.architecture.mvvm

import androidx.lifecycle.MutableLiveData
import cn.madog.module_arch.entity.ModelActionEvent
import cn.madog.module_arch.entity.TipsStateEvent

/**
 * @description 简单的页面弹框的数据
 *
 * @author leon.w
 * @create 2019/4/4 8:52 AM
 * @update 2019/4/4 8:52 AM
 * @version 1
 */
class UiStateResource {

    private val state: MutableLiveData<ModelActionEvent> = MutableLiveData()

    fun getUIState(): MutableLiveData<ModelActionEvent> = state

    init {
        state.value = TipsStateEvent(TipsStateEvent.UI_STATE_NORMAL)
    }

    fun showError(code: String, message: String) {
        val tipsStateEvent = TipsStateEvent(TipsStateEvent.UI_STATE_ERROR, message, code)
        state.value = tipsStateEvent
    }

    fun showSuccess(message: String) {

        val tipsStateEvent = TipsStateEvent(TipsStateEvent.UI_STATE_SUCCESS, message)
        state.value = tipsStateEvent
    }

    fun showWarning(message: String, cancelText: String = "确认") {
        val tipsStateEvent = TipsStateEvent(TipsStateEvent.UI_STATE_WARNING, message, "", cancelText)
        state.value = tipsStateEvent
    }

    fun dismissTips() {
        val tipsStateEvent = TipsStateEvent(TipsStateEvent.UI_STATE_DISMISS)
        state.value = tipsStateEvent
    }

    fun dismissTips(message: String) {
        val tipsStateEvent = TipsStateEvent(TipsStateEvent.UI_STATE_DISMISS_SHOW_TOAST,message)
        state.value = tipsStateEvent
    }

    fun showToast(message: String){
        val tipsStateEvent = TipsStateEvent(TipsStateEvent.UI_STATE_SHOW_TOAST,message)
        state.value = tipsStateEvent
    }

    fun showProcessed(message: String, title: String = "") {
        val tipsStateEvent = TipsStateEvent(TipsStateEvent.UI_STATE_PROCESSED, message, "", title)
        state.value = tipsStateEvent
    }
}