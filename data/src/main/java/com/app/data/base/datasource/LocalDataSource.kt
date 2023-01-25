package com.app.data.base.datasource

import android.content.SharedPreferences
import com.app.data.base.util.Constants
import javax.inject.Inject

class LocalDataSource @Inject constructor(val prefs: SharedPreferences?) {


    fun <T> set(item: String, value: T) {
        val editor = prefs?.edit()

        when (value) {
            is Int -> {
                editor?.putInt(item, value)
            }
            is Long -> {
                editor?.putLong(item, value)
            }
            is String -> {
                editor?.putString(item, value)
            }
            is Boolean -> {
                editor?.putBoolean(item, value)
            }
            else -> {
                editor?.putString(item, value as String)
            }
        }
        editor?.apply()
    }

    inline fun <reified T> get(item: String, default: T? = null): T? {
        return when (T::class) {
            Int::class -> {
                prefs?.getInt(item, default as Int) as T?
            }
            Long::class -> {
                prefs?.getLong(item, default as Long) as T?
            }
            String::class -> {
                prefs?.getString(item, default as String?) as T?
            }
            Boolean::class -> {
                prefs?.getBoolean(item, default as Boolean) as T?
            }
            else -> {
                prefs?.getString(item, default as String?) as T?
            }
        }
    }


    fun contains(prName: String) = try {
        prefs!!.contains(prName)
    } catch (e: Exception) {
        false
    }

    fun clearSharedPrefs() {
        val editor = prefs?.edit()
        editor?.putBoolean(Constants.CLEARED_KEY, true)
        editor?.clear()
        editor?.apply()
    }

    fun removeSharedPrefs(prName: String) {
        val editor = prefs?.edit()
        editor?.remove(prName)
        editor?.apply()
    }
}