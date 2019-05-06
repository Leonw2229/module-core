package cn.madog.module_arch.entity

/**
 * @description 默认实现的ViewModel和View之间的弹框消息事件类
 *
 * @author leon.w
 * @create 2019/4/4 10:36 AM
 * @update 2019/4/4 10:36 AM
 * @version 1
 */
open class TipsStateEvent(
        var state: Int = UI_STATE_NORMAL,
        val message: String = "",
        val code: String? = "",
        val title: String = "提示",
        val cancelText: String = "取消",
        val confirmText: String = "确认"
) : ModelActionEvent() {
    companion object {
        const val UI_STATE_PROCESSED = 101
        const val UI_STATE_ERROR = 102
        const val UI_STATE_SUCCESS = 103
        const val UI_STATE_DISMISS = 104
        const val UI_STATE_WARNING = 105
        const val UI_STATE_DISMISS_SHOW_TOAST = 106
        const val UI_STATE_SHOW_TOAST = 107
        const val UI_STATE_NORMAL = 0
    }
}