package cn.madog.module_manager.core

/**
 * @description 模块bean，自动注册用
 *
 * @author leon.w
 * @create 2019/3/28 2:09 PM
 * @update 2019/3/28 2:09 PM
 * @version 1
 */
data class ModuleConfig(
    val moduleName: String,
    val moduleClass: String,
    val moduleHandleRequestClass: String?,
    val moduleWeight: Int
)