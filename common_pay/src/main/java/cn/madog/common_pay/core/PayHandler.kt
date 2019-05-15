package cn.madog.common_pay.core

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.lang.ref.SoftReference

/**
 * @description
 *
 * @author leon.w
 * @create 2019/3/15 11:47 AM
 * @update 2019/3/15 11:47 AM
 * @version 1
 */
object PayHandler : IWXAPIEventHandler {

    private var api: IWXAPI? = null
    private var context: SoftReference<Context>? = null
    /**
     * 创建微信支付回调处理工具类
     * @param context 最好是Activity的context
     * @param appId 微信支付的appId
     */
    fun createHandler(context: Context, appId: String) {
        api = WXAPIFactory.createWXAPI(context, appId)
        this.context = SoftReference(context)
    }

    fun handleIntent(intent: Intent) {
        api!!.handleIntent(intent, this)
    }

    override fun onResp(resp: BaseResp) {
        if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            resp as PayResp
            PayModule.notifyWeCHatPayResult(resp.prepayId, PayWeChatListener.PAY_STATE_PAY_RESULT, resp)
        }
    }

    override fun onReq(req: BaseReq) {
        if (context != null) {
            Toast.makeText(context!!.get(),req.transaction,Toast.LENGTH_LONG).show()
        }

    }
}