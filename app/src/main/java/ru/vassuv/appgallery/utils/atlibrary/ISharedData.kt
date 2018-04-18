package ru.vassuv.appgallery.utils.atlibrary

import android.content.SharedPreferences
import android.preference.PreferenceManager
import ru.vassuv.appgallery.App

interface ISharedData {
    companion object {
        internal val instance: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(App.context) }
    }

    val name: String
}


fun ISharedData.getString() : String? = ISharedData.instance.getString(name, null)
fun ISharedData.getInt() = ISharedData.instance.getInt(name, 0)
fun ISharedData.getBoolean() = ISharedData.instance.getBoolean(name, false)
fun ISharedData.getLong() = ISharedData.instance.getLong(name, 0)

fun ISharedData.saveString(value: String) = ISharedData.instance.edit().putString(name, value).apply()
fun ISharedData.saveInt(value: Int) = ISharedData.instance.edit().putInt(name, value).apply()
fun ISharedData.saveBoolean(value: Boolean) = ISharedData.instance.edit().putBoolean(name, value).apply()
fun ISharedData.saveLong(value: Long) = ISharedData.instance.edit().putLong(name, value).apply()

fun ISharedData.remove() = ISharedData.instance.edit().remove(name).apply()