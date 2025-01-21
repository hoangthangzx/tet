package com.metalsensor.gold.detector.finder.utils

import android.view.View
import androidx.core.content.ContextCompat
import android.content.Context
import android.widget.Toast
import android.view.inputmethod.InputMethodManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.metalsensor.gold.detector.finder.R

import android.content.Intent

inline fun <reified T> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}

fun TextView.setTextColorRes(colorResId: Int) {
    setTextColor(ContextCompat.getColor(context, colorResId))
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}
    fun View.visible() {
        visibility = View.VISIBLE
    }

    fun View.invisible() {
        visibility = View.INVISIBLE
    }

    fun View.gone() {
        visibility = View.GONE
    }

    fun View.enable() {
        isEnabled = true
    }

    fun View.disable() {
        isEnabled = false
    }
fun TextView.setTextRes(textResId: Int) {
    text = context.getString(textResId)
}
fun setViewsVisible(vararg views: View) {
    views.forEach { it.visibility = View.VISIBLE }
}

fun setViewsGone(vararg views: View) {
    views.forEach { it.visibility = View.GONE }
}

fun setViewsInvisible(vararg views: View) {
    views.forEach { it.visibility = View.INVISIBLE }
}

    fun View.setBackgroundColorRes(colorResId: Int) {
        setBackgroundColor(ContextCompat.getColor(context, colorResId))
    }

fun View.tap(action: (view: View?) -> Unit) {
    setOnClickListener(object : TapListener() {
        override fun onTap(v: View?) {
            action(v)
        }
    })
}