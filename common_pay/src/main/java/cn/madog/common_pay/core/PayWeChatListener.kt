package cn.madog.common_pay.core

import android.content.Context
import com.tencent.mm.opensdk.modelbase.BaseResp

/**
 * @description 微信支付回调
 *
 * @author leon.w
 * @create 2019/3/15 11:38 AM
 * @update 2019/3/15 11:38 AM
 * @version 1
 */
interface PayWeChatListener {

    companion object {
        const val PAY_STATE_PARAM_ERROR = 1
        const val PAY_STATE_RUN_WECHAT = 2
        const val PAY_STATE_RUN_WECHAT_ERROR = 4
        const val PAY_STATE_PAY_RESULT = 5
    }

    abstract fun onWeChatPayResult(tag: Any, resultCode: Int, resp: BaseResp?)
}

