package cn.madog.module_manager.core

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import org.json.JSONObject
import java.io.*
import java.util.regex.Pattern

/**
 * @description
 *
 * @author leon.w
 * @create 2019/3/14 10:40 AM
 * @update 2019/3/14 10:40 AM
 * @version 1
 */
object ModuleM {
    private var application: Application? = null

    fun getApplication(): Application? {
        return application
    }

    fun attachBaseContext(base: Context?) {

        // 简单的初始化分发
        modules.values.forEach {
            it.attachBaseContext(base)
        }
    }

    fun onCreate() {
        // 简单的初始化分发
        modules.values.forEach {
            it.onCreate()
        }
    }

    fun onConfigurationChanged(newConfig: Configuration) {
        // 简单的初始化分发
        modules.values.forEach {
            it.onConfigurationChanged(newConfig)
        }
    }

    fun onLowMemory() {
        // 简单的初始化分发
        modules.values.forEach {
            it.onLowMemory()
        }
    }

    fun onTrimMemory(level: Int) {
        // 简单的初始化分发
        modules.values.forEach {
            it.onTrimMemory(level)
        }
    }

    private fun build(application: Application) {
        this.application = application

        initSubModule(application)
    }


    fun init(application: Application) {
        loadAssets(application)
        build(application)
    }


    private fun loadAssets(application: Application) {
        val assets = application.assets
        val list = assets.list("")
        if (list.isNullOrEmpty()) {
            Log.d("madog", "module is not config")
            return
        }

        //  moduleConfigs = [module_m_app.json, module_m_app_admin.json, module_m_app_student.json, module_m_module_lbs_checkin.json]
        val moduleConfigs = list.filter { Pattern.matches("^module_m_.*\\.json", it) }

        val moduleConfigContent: MutableList<String> = mutableListOf()

        moduleConfigs.forEach {
            val inputStream = assets.open(it)
            moduleConfigContent.add(inputStream.bufferedReader().readText())
        }

        val configs = handleModuleConfigs(moduleConfigContent)

        configs.forEach {
            val clazz = Class.forName(it.moduleClass)
            if (clazz.interfaces.contains(ModuleMApplication::class.java)) {
                val newInstance = clazz.newInstance()
                registerModule(it.moduleName, newInstance as ModuleMApplication)
            }
        }
    }

    private fun handleModuleConfigs(moduleConfigs: List<String>): List<ModuleConfig> {
        val moduleList: MutableList<ModuleConfig> = mutableListOf()

        moduleConfigs.forEach {

            val moduleConfig: ModuleConfig? = handleModuleConfig(it)

            if (moduleConfig != null && !moduleList.contains(moduleConfig)) {
                moduleList.add(moduleConfig)
            }
        }
        return moduleList
    }

    /**
     * @param moduleConfig 是文件的内容不是文件的名称或路径
     */
    private fun handleModuleConfig(moduleConfig: String): ModuleConfig? {
        var moduleConfigBean: ModuleConfig? = null

        try {

            val jsonObject = JSONObject(moduleConfig)
            val moduleName = jsonObject.getString("moduleName")
            val moduleClass = jsonObject.getString("moduleClass")
            val moduleHandleRequestClass = jsonObject.optString("moduleHandleRequestClass")
            val moduleWeight = jsonObject.optInt("moduleWeight", 1)

            moduleConfigBean = ModuleConfig(moduleName, moduleClass, moduleHandleRequestClass, moduleWeight)

        } catch (e: Exception) {
            Log.e("lmy","load module config fail")
        }

        return moduleConfigBean
    }


    private var modules: MutableMap<String, ModuleMApplication> = mutableMapOf()

    private fun <T : ModuleMApplication> registerModule(moduleName: String, moduleMApplication: T): ModuleM {

        if (modules.containsKey(moduleName)) {
            throw IllegalArgumentException("moduleName existed")
        }

        if (modules.containsValue(moduleMApplication)) {
            throw IllegalArgumentException("${moduleMApplication.javaClass.simpleName}, ModuleMApplication existed ")
        }

        modules[moduleName] = moduleMApplication

        return this
    }


    private fun initSubModule(application: Application) {
        // 简单的初始化分发
        modules.values.forEach {
            it.onModuleInit(application)
        }
    }

}