package cn.madog.common_pay.core

import android.util.Log

/**
 * @description
 *
 * @author leon.w
 * @create 2019-08-15 17:15
 * @update 2019-08-15 17:15
 * @version 1
 */
object LPayLog{
    private var debugModel: Boolean = false

    fun setDebugModel(debug: Boolean){
        debugModel = debug
    }

    fun d(message: String,tag: String = "lmy"){
        if (debugModel){
            Log.d(tag,message)
        }
    }
    fun e(message: String,error: Throwable? = null,tag: String = "lmy"){
        if (debugModel){
            Log.e(tag,message,error)
        }
    }
}