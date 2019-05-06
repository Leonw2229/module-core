package com.hdfjy.module_network

import android.util.MalformedJsonException
import com.google.gson.JsonSyntaxException
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.OnErrorNotImplementedException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * @description
 *
 * @author leon.w
 * @create 2019/4/9 10:39 AM
 * @update 2019/4/9 10:39 AM
 * @version 1
 */

open class ObserverResult<T>() : Observer<T> {

    protected var success: ((T) -> Unit)? = null
    protected var error: ((code: String, message: String) -> Unit)? = null
    protected var compositeDisposable: CompositeDisposable? = null

    constructor(success: (T) -> Unit, error: (code: String, message: String) -> Unit, compositeDisposable: CompositeDisposable? = null) : this() {
        this.success = success
        this.error = error
        this.compositeDisposable
    }

    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
        compositeDisposable?.add(d)
    }

    override fun onNext(t: T) {
        success?.invoke(t)
    }

    override fun onError(e: Throwable) {
        val code: String
        val message: String

        if (e is HttpException) {
            code = "-${e.code()}"
            message = handle(code, e.message())
        } else {

            message = when (e) {
                is IllegalArgumentException -> "参数异常 \nException:IllegalArgumentException"
                is ClassNotFoundException -> "找不到类异常 \nException:ClassNotFoundException"
                is NoClassDefFoundError -> "未找到类定义异常 \nException:NoClassDefFoundError"
                is NoSuchMethodError -> "未找到方法定义异常 \nException:NoSuchMethodError"
                is OutOfMemoryError -> "内存不足错误 \nException:OutOfMemoryError"
                is StackOverflowError -> "堆栈溢出错误 \nException:StackOverflowError"
                is UnknownError -> "未知错误 \nException:UnknownError"
                is ArrayIndexOutOfBoundsException -> "数组索引越界 \nException:ArrayIndexOutOfBoundsException"
                is ClassCastException -> "类型转换异常 \nException:ClassCastException"
                is IndexOutOfBoundsException -> "下标越界异常 \nException:IndexOutOfBoundsException"
                is NullPointerException -> "空指针异常 \nException:NullPointerException"
                is NumberFormatException -> "数字转换异常 \nException:NumberFormatException"
                is SecurityException -> "安全异常 \nException:SecurityException"
                is JSONException -> "解析结果异常 \nException:JSONException"
                is JsonSyntaxException -> "解析结果异常 \nException:JsonSyntaxException"
                is IllegalStateException -> "非法状态异常 \nException:IllegalStateException"
                is MalformedJsonException -> "服务器错误或返回数据错误 \nException:MalformedJsonException"
                is SocketTimeoutException, is ConnectException -> "服务器正在升级中，稍后重试 \nException:ConnectException"
                is OnErrorNotImplementedException -> "未知错误"
                else -> if (e.localizedMessage.isNullOrEmpty()) e.message.orEmpty() else e.localizedMessage.orEmpty()
            }
            code = "-1"
        }
        error?.invoke(code, message)
        e.printStackTrace()
    }

    /**
     * 处理http请求错误提示信息
     *
     * @param code
     * @param msg
     */
    private fun handle(code: String, msg: String): String {

        var message = "请求异常,"

        when (code) {

            302.toString() -> message = "请求被重定向, Code:302"

            400.toString() -> message = "请求参语法或者报文错误, Code:400"

            401.toString() -> message = "请求没有认证信息或认证失败, Code:401"

            403.toString() -> message = "请求被服务器拒绝, Code:403"

            404.toString() -> message = "请求地址错误，未找到资源, Code:404"

            500.toString() -> message = "服务器异常, Code:500"

            503.toString() -> message = "服务器停机维护无法处理请求, Code:503"

            else -> message += msg
        }

        return message
    }

}