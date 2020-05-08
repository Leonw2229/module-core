package cn.madog.mc_module1

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import cn.madog.mc_core.ModuleApp
import cn.madog.module_manager.core.ModuleMApplication

@ModuleApp
class app: ModuleMApplication{
    override fun onModuleInit(application: Application) {
        Log.d("lmy","moduleInit")
    }

    override fun attachBaseContext(base: Context?) {
    }

    override fun onCreate() {
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
    }

    override fun onLowMemory() {
    }

    override fun onTrimMemory(level: Int) {
    }

}