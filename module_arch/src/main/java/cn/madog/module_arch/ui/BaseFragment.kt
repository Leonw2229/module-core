package cn.madog.module_arch.ui

import cn.madog.SweetAlertDialog.SweetAlertDialog
import cn.madog.module_arch.abs.BaseFragmentAbs

/**
 * @description
 *
 * @author leon.w
 * @create 2019/3/27 5:58 PM
 * @update 2019/3/27 5:58 PM
 * @version 1
 */
abstract class BaseFragment : BaseFragmentAbs() {

    override fun showWarningMessage(message: String) {
        showMessage(message, SweetAlertDialog.PROGRESS_TYPE).apply {
            showCancelButton(false)
            hideConfirmButton()
            setCancelable(false)
        }
    }

    override fun showSuccessMessage(message: String, confirmText: String) {
        showMessage(message, SweetAlertDialog.SUCCESS_TYPE).apply {
            this.confirmText = confirmText
            showCancelButton(false)
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        }
    }

    override fun showErrorMessage(message: String) {
        showMessage(message, SweetAlertDialog.ERROR_TYPE).apply {
            showCancelButton(false)
        }
    }

    override fun showWarningCancelMessage(message: String, cancelText: String) {
        showMessage(message, SweetAlertDialog.WARNING_TYPE).apply {
            this.cancelText = cancelText
            showCancelButton(true)
        }
    }


    override fun showWarningConfirmMessage(message: String, confirmText: String) {
        showMessage(message, SweetAlertDialog.WARNING_TYPE)
                .apply {
                    this.confirmText = confirmText
                    showCancelButton(false)
                    setCanceledOnTouchOutside(false)
                }
    }

    override fun showWarningCancelOrConfirmMessage(message: String, confirmText: String, cancelText: String) {
        showMessage(message, SweetAlertDialog.WARNING_TYPE)
                .apply {
                    this.cancelText = cancelText
                    this.confirmText = confirmText
                    showCancelButton(true)
                    setCanceledOnTouchOutside(true)
                }
    }

    private fun showMessage(message: String, type: Int): SweetAlertDialog {
        if (dialog == null) {
            dialog = SweetAlertDialog(context, type)
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog!!.changeAlertType(type)
        dialog!!.titleText = "提示"
        dialog!!.contentText = message
        if (!dialog!!.isShowing) {
            dialog!!.show()
        }
        return dialog!!
    }

    override fun releaseDialog() {
        dialog?.dismissWithAnimation()
        dialog = null
    }

    private var dialog: SweetAlertDialog? = null
    fun getSweetDialog() = dialog

    override fun dismissDialog() {
        dialog?.dismissWithAnimation()
    }

}