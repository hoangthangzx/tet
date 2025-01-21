package com.metalsensor.gold.detector.finder.utils

import android.app.Activity
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.metalsensor.gold.detector.finder.R

fun Activity.showSystemUI(white: Boolean) {
    if (white) {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    } else {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    } else {
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }
}

var lastClickTime = 0L
fun View.onSingleClick(action: () -> Unit){
    this.setOnClickListener {
        if (System.currentTimeMillis() - lastClickTime >= 500) {
            action()
            lastClickTime = System.currentTimeMillis()
        }
    }
}

fun dpToPx(dp: Int, context: Context): Float {
    val density = context.resources.displayMetrics.density
    return (dp * density)
}

fun Context.startVibration() {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (vibrator.hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pattern = longArrayOf(0, 1000, 1000) // rung 1 giây, nghỉ 1 giây
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0)) // lặp lại từ đầu
        } else {
            val pattern = longArrayOf(0, 1000, 1000) // rung 1 giây, nghỉ 1 giây
            vibrator.vibrate(pattern, 0) // lặp lại từ đầu
        }
    }
}


fun Context.stopVibration() {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    vibrator.cancel()
}


fun TextView.applyGradient_10(context: Context) {
    this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val colors = intArrayOf(
                ContextCompat.getColor(context, R.color.theme10_gr4),  // Màu đầu tiên
                ContextCompat.getColor(context, R.color.theme10_gr3),
                ContextCompat.getColor(context, R.color.theme10_gr2),
                ContextCompat.getColor(context, R.color.theme10_gr1),// Màu thứ hai
                    // Màu cuối cùng
            )
            val paint = this@applyGradient_10.paint
            val textHeight = this@applyGradient_10.height.toFloat()  // Lấy chiều cao của TextView

            if (textHeight > 0) {
                // Gradient theo chiều dọc từ dưới lên trên
                val shader = LinearGradient(
                    0f, textHeight, 0f, 0f,  // Thay đổi từ (0f, 0f, textWidth, textSize) thành chiều dọc
                    colors,
                    null, Shader.TileMode.CLAMP
                )
                paint.shader = shader
                // Xóa listener để tránh việc gọi lại nhiều lần
                this@applyGradient_10.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}

fun TextView.applyGradient_9(context: Context) {
    this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val colors = intArrayOf(
                ContextCompat.getColor(context, R.color.theme9_gr1),
                ContextCompat.getColor(context, R.color.theme9_gr2),
                ContextCompat.getColor(context, R.color.theme9_gr3),// Màu thứ hai
                // Màu cuối cùng
            )
            val paint = this@applyGradient_9.paint
            val textHeight = this@applyGradient_9.height.toFloat()  // Lấy chiều cao của TextView

            if (textHeight > 0) {
                // Gradient theo chiều dọc từ dưới lên trên
                val shader = LinearGradient(
                    0f, textHeight, 0f, 0f,  // Thay đổi từ (0f, 0f, textWidth, textSize) thành chiều dọc
                    colors,
                    null, Shader.TileMode.CLAMP
                )
                paint.shader = shader
                // Xóa listener để tránh việc gọi lại nhiều lần
                this@applyGradient_9.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}


fun TextView.applyGradient_2(context: Context) {
    this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val colors = intArrayOf(
                ContextCompat.getColor(context, R.color.theme2_gr4),  // Màu đầu tiên
                ContextCompat.getColor(context, R.color.theme2_gr3),
                ContextCompat.getColor(context, R.color.theme2_gr2),
                ContextCompat.getColor(context, R.color.theme2_gr1),// Màu thứ hai
                // Màu cuối cùng
            )
            val paint = this@applyGradient_2.paint
            val textHeight = this@applyGradient_2.height.toFloat()  // Lấy chiều cao của TextView

            if (textHeight > 0) {
                // Gradient theo chiều dọc từ dưới lên trên
                val shader = LinearGradient(
                    0f, textHeight, 0f, 0f,  // Thay đổi từ (0f, 0f, textWidth, textSize) thành chiều dọc
                    colors,
                    null, Shader.TileMode.CLAMP
                )
                paint.shader = shader
                // Xóa listener để tránh việc gọi lại nhiều lần
                this@applyGradient_2.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}
