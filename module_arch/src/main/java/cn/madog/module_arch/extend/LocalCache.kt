package cn.madog.module_arch.extend

import android.content.Context
import android.content.SharedPreferences

internal fun Context.getSp(sp_name: String? = null): SharedPreferences {
    return getSharedPreferences(if (sp_name.isNullOrEmpty()) "app" else sp_name, Context.MODE_PRIVATE)
}

fun Context.saveLocal(key: String, value: Int, sp_name: String? = null) {
    getSp(sp_name).apply {
        val edit = edit()
        edit.putInt(key, value)
        edit.apply()
    }
}

fun Context.saveLocal(key: String, value: String?, sp_name: String? = null) {
    getSp(sp_name).apply {
        val edit = edit()
        edit.putString(key, value)
        edit.apply()
    }
}

fun Context.saveLocal(key: String, value: Double, sp_name: String? = null) {
    getSp(sp_name).apply {
        val edit = edit()
        edit.putString(key, value.toString())
        edit.apply()
    }
}

fun Context.saveLocal(key: String, value: Float, sp_name: String? = null) {
    getSp(sp_name).apply {
        val edit = edit()
        edit.putFloat(key, value)
        edit.apply()
    }
}

fun Context.saveLocal(key: String, value: Boolean, sp_name: String? = null) {
    getSp(sp_name).apply {
        val edit = edit()
        edit.putBoolean(key, value)
        edit.apply()
    }
}

fun Context.getStringForLocal(key: String, defaultValue: String?, sp_name: String? = null): String? {
    if (getSp(sp_name) != null) {
        return getSp(sp_name).getString(key, defaultValue)
    }
    return defaultValue
}

fun Context.getIntForLocal(key: String, defaultValue: Int, sp_name: String? = null): Int {
    if (getSp(sp_name) != null) {
        return getSp(sp_name).getInt(key, defaultValue)
    }

    return defaultValue
}

fun Context.getDoubleForLocal(key: String, defaultValue: Double, sp_name: String? = null): Double {
    if (getSp(sp_name) != null) {
        val string = getSp(sp_name).getString(key, defaultValue.toString())
        return try {
            string.toDouble()
        } catch (e: Exception) {
            defaultValue
        }
    }

    return defaultValue
}

fun Context.getBooleanForLocal(key: String, defaultValue: Boolean, sp_name: String? = null): Boolean {
    if (getSp(sp_name) != null) {
        return getSp(sp_name).getBoolean(key, defaultValue)
    }

    return defaultValue
}

fun Context.getFloatForLocal(key: String, defaultValue: Float, sp_name: String? = null): Float {
    if (getSp(sp_name) != null) {
        return getSp(sp_name).getFloat(key, defaultValue)
    }

    return defaultValue
}