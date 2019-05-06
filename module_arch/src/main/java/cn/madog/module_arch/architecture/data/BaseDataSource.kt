package cn.madog.module_arch.architecture.data

/**
 * 基础的Model层定义，可做mvp的m层定义，mvvm的m层定义
 * @author leon.w
 */
interface BaseDataSource {
    fun onCreate()
    fun onDestroy()
}