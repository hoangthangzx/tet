package com.metalsensor.gold.detector.finder

import android.graphics.LinearGradient
import android.graphics.Shader
import android.text.TextPaint
import android.text.style.CharacterStyle

class GradientTextSpan(private val colors: IntArray, private val textWidth: Float) : CharacterStyle() {
    override fun updateDrawState(paint: TextPaint) {
        val shader = LinearGradient(
            0f, 0f, textWidth, 0f,
            colors, null, Shader.TileMode.CLAMP
        )
        paint.shader = shader
    }
}