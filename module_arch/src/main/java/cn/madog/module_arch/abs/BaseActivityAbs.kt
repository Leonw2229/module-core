package cn.madog.module_arch.abs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * @description
 *
 * @author leon.w
 * @create 2019/3/27 5:58 PM
 * @update 2019/3/27 5:58 PM
 * @version 1
 */
abstract class BaseActivityAbs : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            supportFinishAfterTransition()
        }
    }


    abstract fun showWarningMessage(message: String)

    abstract fun showSuccessMessage(message: String, confirmText: String = "确认")

    abstract fun showErrorMessage(message: String)

    abstract fun showWarningCancelMessage(message: String, cancelText: String = "取消")


    abstract fun showWarningCancelOrConfirmMessage(message: String, confirmText: String = "确认",cancelText: String = "取消" )

    abstract fun showWarningConfirmMessage(message: String, confirmText: String = "确认")

    abstract fun dismissDialog()

    abstract fun releaseDialog()

    override fun onDestroy() {
        releaseDialog()
        super.onDestroy()
    }

}