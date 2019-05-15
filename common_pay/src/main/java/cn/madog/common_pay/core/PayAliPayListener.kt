package cn.madog.common_pay.core

/**
 * @description 支付宝支付回调
 *
 * @author leon.w
 * @create 2019/3/15 3:27 PM
 * @update 2019/3/15 3:27 PM
 * @version 1
 */
interface PayAliPayListener {
    companion object {
        const val PAY_STATE_ALI_RUN = 2
        const val PAY_STATE_ALI_RUN_ERROR = 4

        const val PAY_STATE_ALI_CANCEL = 6001
        const val PAY_STATE_ALI_SUCCESS = 9000
        const val PAY_STATE_ALI_FAILURE = 4000
    }


    fun onPayAliResult(resultCode: Int, resultInfo: String? = null)
}