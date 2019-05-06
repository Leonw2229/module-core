package cn.madog.module_manager.core

import android.app.Application
import android.content.Context
import android.content.res.Configuration

/**
 * @description 各模块需实现的契约类，用于同一注册、分发application生命周期
 *
 * @author leon.w
 * @create 2019/3/14 10:48 AM
 * @update 2019/3/14 10:48 AM
 * @version 1
 */
interface ModuleMApplication {
    fun onModuleInit(application: Application)
    fun attachBaseContext(base: Context?)
    fun onCreate()
    fun onConfigurationChanged(newConfig: Configuration)
    fun onLowMemory()
    fun onTrimMemory(level: Int)
}