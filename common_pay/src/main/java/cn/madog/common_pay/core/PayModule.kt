package cn.madog.common_pay.core

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import cn.madog.common_pay.entity.PayResult
import com.alipay.sdk.app.PayTask
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.modelpay.PayResp
import com.tencent.mm.opensdk.openapi.WXAPIFactory


/**
 * @description 微信和支付宝支付方式包括回调注册
 *
 * @author leon.w
 * @create 2019/3/15 10:35 AM
 * @update 2019/3/15 10:35 AM
 * @version 1
 */
object PayModule {

    private val callbackWeChatList: MutableMap<Context, PayWeChatListener> = mutableMapOf()
    private val callbackKey: MutableMap<String, Context> = mutableMapOf()

    private val callbackAliList: MutableMap<Context, PayAliPayListener> = mutableMapOf()

    /**
     * 设置微信支付回调监听
     * @param tag 当前类实例或者其他程序内唯一对象
     */
    fun setPayWeChatCallbackListener(tag: Context, payWeChatListener: PayWeChatListener): PayModule {
        if (callbackWeChatList.containsKey(tag)) {
            LPayLog.e("setPayWeChatCallbackListener tag = $tag , exited ")
        }

        if (callbackWeChatList.containsValue(payWeChatListener)) {
            LPayLog.e("setPayWeChatCallbackListener payWeChatListener = $payWeChatListener , exited ")
        }

        callbackWeChatList[tag] = payWeChatListener
        return this
    }

    /**
     * 设置微信支付回调监听
     * @param tag 当前类实例或者其他程序内唯一对象
     */
    fun setPayAliCallbackListener(tag: Context, payAliPayListener: PayAliPayListener): PayModule {
        if (callbackAliList.containsKey(tag)) {
            LPayLog.e("setPayAliCallbackListener tag = $tag , exited ")
        }

        if (callbackAliList.containsValue(payAliPayListener)) {
            LPayLog.e("setPayAliCallbackListener payAliPayListener = $payAliPayListener , exited ")
        }

        callbackAliList[tag] = payAliPayListener
        return this
    }

    /**
     * 通知微信支付结果
     * @param tag 现在只能是prepareId 支付的时候已和回调的tag进行绑定了，如果没有绑定那么就是全部回调接口通知，否则只通知当前回调接口
     */
    internal fun notifyWeCHatPayResult(tag: String?, resultCode: Int, resp: PayResp? = null) {
        // 设置为 internal 是为了在PayHandler里调用该方法进行结果通知
        val context = callbackKey[tag]
        if (context == null) {
            callbackWeChatList.entries.iterator().forEach {
                it.value.onWeChatPayResult(it.key, resultCode, resp)
            }
        } else {
            val payWeChatListener = callbackWeChatList[context]
            payWeChatListener?.onWeChatPayResult(context, resultCode, resp)
        }
    }

    /**
     * 通知支付宝支付结果
     */
    private fun notifyAliPayResult(tag: String?, resultCode: Int, resultInfo: String? = null) {

        val context = callbackKey[tag]
        if (context == null){
            callbackAliList.values.forEach {
                it.onPayAliResult(resultCode, resultInfo)
            }
        }else{
            val aliResult = callbackAliList[context]
            aliResult?.onPayAliResult(resultCode, resultInfo)
        }
    }

    /**
     * 调起微信支付，设置支付回调监听需在调用此方法之前设置，否则可能会出现没有回调结果的情况
     *
     * @param context 当前activity的context
     * @param result 微信支付预支付订单返回的参数，需包含 appid 、partnerid 、prepayid 、package 、noncestr 、timestamp 、sign
     * @param weChatAppId 微信支付的 APP_Id
     * @param tag 绑定标签，微信的必须是prepareId，如果添加了标签，那么只会通知到当前tag的类，否则所有的listener都可以收到
     *
     * 调用示例：
     *  PayModule.setPayWeChatCallbackListener(this, object : PayWeChatListener {
     *       override fun onWeChatPayResult(tag: Context, resultCode: Int, resp: BaseResp?) {
     *              getSweetDialog()?.dismissWithAnimation()
     *              when (resultCode) {
     *                  PayWeChatListener.PAY_STATE_PAY_RESULT -> {
     *                      if (resp!!.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
     *                          toast("取消支付了")
     *                      } else {
     *                          showPayResult(resp.errCode == BaseResp.ErrCode.ERR_OK)
     *                      }
     *                  }
     *                  PayWeChatListener.PAY_STATE_RUN_WECHAT_ERROR -> {
     *                      toast("调起微信支付出现错误")
     *                  }
     *
     *                  PayWeChatListener.PAY_STATE_PARAM_ERROR -> {
     *                      Log.d("lmy", "支付参数有误")
     *                      toast("支付参数有误")
     *                  }
     *              }
     *
     *      }
     *  }).payForWeChat(this, result, VideoUrlConstants.WECHAT_APPID)
     */
    fun payForWeChat(context: Context, result: Map<String, String>, weChatAppId: String, tag: String? = null) {

        try {
            val payReq = PayReq()
            payReq.appId = result["appid"]
            payReq.partnerId = result["partnerid"]
            payReq.prepayId = result["prepayid"]
            payReq.packageValue = result["package"]
            payReq.nonceStr = result["noncestr"]
            payReq.timeStamp = result["timestamp"]
            payReq.sign = result["sign"]

            if (!tag.isNullOrEmpty()) {

                // 使用prepayId绑定回调类
                if (callbackKey.containsKey(tag)) {
                    callbackKey[tag] = context
                }

                if (callbackKey.containsValue(context)) {
                    var key = ""
                    callbackKey.entries.iterator().forEach {
                        if (it.value == context) {
                            key = it.key
                        }
                    }
                    callbackKey[key] = context
                }
            }

            val createWXAPI = WXAPIFactory.createWXAPI(context, null)
            createWXAPI.registerApp(weChatAppId)
            createWXAPI.sendReq(payReq)

            notifyWeCHatPayResult(tag, PayWeChatListener.PAY_STATE_RUN_WECHAT)
        } catch (e: Exception) {
            e.printStackTrace()
            notifyWeCHatPayResult(
                tag,
                PayWeChatListener.PAY_STATE_RUN_WECHAT_ERROR
            )
        }
    }

    /**
     * 支付宝支付
     * @param payInfo 支付宝支付预支付订单返回的参数，请保证是创建于支付订单返回的参数，设置支付回调监听需在调用此方法之前设置，否则可能会出现没有回调结果的情况
     *
     * 调用示例:
     *   PayModule.setPayAliCallbackListener(this,object : PayAliPayListener{
     *      override fun onPayAliResult(resultCode: Int, resultInfo: String?) {
     *          getSweetDialog()?.dismissWithAnimation()
     *          when(resultCode){
     *
     *              PayAliPayListener.PAY_STATE_ALI_CANCEL -> {
     *                  toast("取消了支付")
     *              }
     *
     *              PayAliPayListener.PAY_STATE_ALI_RUN_ERROR -> {
     *                  toast("启动支付宝失败")
     *              }
     *
     *              PayAliPayListener.PAY_STATE_ALI_RUN -> {
     *                  getSweetDialog()?.dismissWithAnimation()
     *              }
     *
     *              else -> {
     *                  showPayResult(PayAliPayListener.PAY_STATE_ALI_SUCCESS == resultCode)
     *              }
     *          }
     *      }
     *
     *   }).payFormAli(this,payInfo)
     *
     */
    fun payFormAli(context: Activity, payInfo: String,tag: String? = null) {

        if (!tag.isNullOrEmpty()) {

            // 使用prepayId绑定回调类
            if (callbackKey.containsKey(tag)) {
                callbackKey[tag] = context
            }

            if (callbackKey.containsValue(context)) {
                var key = ""
                callbackKey.entries.iterator().forEach {
                    if (it.value == context) {
                        key = it.key
                    }
                }
                callbackKey[key] = context
            }
        }

        // 必须异步调用
        val payThread = Thread {
            val aliPay = PayTask(context)

            try {
                val result = aliPay.payV2(payInfo, true)

                if (Looper.myLooper() != Looper.getMainLooper()) {
                    val mainThread = Handler(Looper.getMainLooper())
                    mainThread.post {
                        val payResult = PayResult(result)
                        val resultInfo = payResult.result // 同步返回需要验证的信息
                        val resultStatus = payResult.resultStatus

                        val resultState = resultStatus.toInt() // 强转，可能出现错误

                        if (resultState == 6004 || resultState == 8000 || resultState == 9000) {
                            notifyAliPayResult(tag, PayAliPayListener.PAY_STATE_ALI_SUCCESS, resultInfo)
                        } else if (resultState == 6001) {
                            notifyAliPayResult(tag, PayAliPayListener.PAY_STATE_ALI_CANCEL, resultInfo)
                        } else {
                            notifyAliPayResult(tag, PayAliPayListener.PAY_STATE_ALI_FAILURE, resultInfo)
                        }
                    }
                }
            } catch (e: Exception) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    val mainThread = Handler(Looper.getMainLooper())
                    mainThread.post {
                        notifyAliPayResult(tag, PayAliPayListener.PAY_STATE_ALI_RUN_ERROR)
                    }
                }
            }

        }
        payThread.start()
    }


    /**
     * 删除微信回调监听
     */
    fun removeWeChatCallback(tag: Context) {

        if (!callbackWeChatList.containsKey(tag)) {
            LPayLog.e("removeWeChatCallback tag = $tag , does not exist ")
        } else {
            callbackWeChatList.remove(tag)
        }
    }


    /**
     * 删除支付宝回调监听
     */
    fun removeAliCallback(tag: Context) {
        if (!callbackAliList.containsKey(tag)) {
            LPayLog.e("removeAliCallback tag = $tag , does not exist ")
        } else {
            callbackAliList.remove(tag)
        }
    }

}