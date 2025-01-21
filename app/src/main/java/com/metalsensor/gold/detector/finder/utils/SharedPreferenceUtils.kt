package com.metalsensor.gold.detector.finder

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.metalsensor.gold.detector.finder.utils.APPLICATION
import com.pianokeyboard.playpiano.learnpiano.SingletonHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceUtils @Inject constructor(context: Context) {
    private val MYAPPLICATION = "MY_APPLICATION"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(MYAPPLICATION, Context.MODE_PRIVATE)

    private var mSharePref: SharedPreferences? = context.getSharedPreferences(APPLICATION, Context.MODE_PRIVATE)
    inline fun <reified T> getObjModel(): T? {
        val value = getStringValue("Key_Obj_${T::class.java.name}")
        return if (value != null && value.isNotEmpty()) {
            Gson().fromJson(value, T::class.java)
        } else {
            null
        }
    }
    inline fun <reified T> setObjModel(value: T?) {
        if (value != null) {
            putStringValue("Key_Obj_${T::class.java.name}", Gson().toJson(value))
        }
    }
    fun forceRated(context: Context) {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = pre.edit()
        editor.putBoolean("rated", true)
        editor.apply()
    }
    fun putBoolean(str: String?, bl: Boolean?) {
        val edit = mSharePref!!.edit()
        bl?.let { edit.putBoolean(str, it) }
        edit.apply()
    }
    fun isRated(context: Context): Boolean {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        return pre.getBoolean("rated", false)
    }
    fun putStringValue(key: String?, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value).apply()
    }

    fun getStringValue(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    fun getStringTriggerValue(key: String?): String? {
        return sharedPreferences.getString(key, "60")
    }

    fun putNumber(key: String, value : Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value).apply()
    }
    fun getNumber(key: String): Int {
        return sharedPreferences.getInt(key,0)
    }

    fun getNumberCheck(key: String): Int {
        return sharedPreferences.getInt(key,0)
    }
    fun getNumberCheck2(key: String): Int {
        return sharedPreferences.getInt(key,5)
    }

    fun getNumberSen(key: String): Int {
        return sharedPreferences.getInt(key,5)
    }

    fun getNumberRate(key: String): Int {
        return sharedPreferences.getInt(key,1)
    }
    fun getBooleanValue(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getBooleanValueSetting(key: String): Boolean {
        return sharedPreferences.getBoolean(key, true)
    }
    fun putBooleanValue(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value).apply()
    }
    fun putBooleanMute(key: String, value: Boolean) {
        val editor = mSharePref!!.edit()
        editor.putBoolean(key, value)
        editor.apply() // Lưu thay đổi
    }
    fun getBooleanMute(key: String, defaultValue: Boolean): Boolean {
        return mSharePref!!.getBoolean(key, defaultValue)
    }
    fun putBooleanflass(key: String, value: Boolean) {
        val editor = mSharePref!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
    fun getBooleanflass(key: String, defaultValue: Boolean): Boolean {
        return mSharePref!!.getBoolean(key, defaultValue)
    }
    fun putBooleanvibration(key: String, value: Boolean) {
        val editor = mSharePref!!.edit()
        editor.putBoolean(key, value)
        editor.apply() // Lưu thay đổi
    }
    fun getBooleanvibration(key: String, defaultValue: Boolean): Boolean {
        return mSharePref!!.getBoolean(key, defaultValue)
    }


    companion object : SingletonHolder<SharedPreferenceUtils, Context>(::SharedPreferenceUtils)



}
