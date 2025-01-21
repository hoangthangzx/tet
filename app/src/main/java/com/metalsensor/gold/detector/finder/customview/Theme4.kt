package com.metalsensor.gold.detector.finder.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class Theme4 : View {

    private val mainPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val singleColor = Color.parseColor("#381E05") // Màu duy nhất
    private val margin = 16f
    private val frame = RectF()
    private var progress = 0f
    private var maxProgress = 1000f

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        mainPaint.style = Paint.Style.STROKE
        mainPaint.strokeWidth = 40f

        // Sử dụng màu duy nhất thay vì gradient
        mainPaint.color = singleColor
    }

    fun setMaxProgress(max: Float) {
        maxProgress = max
    }

    fun setProgress(value: Float) {
        progress = when {
            value < 0 -> 0f
            value > maxProgress -> maxProgress
            else -> value
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()

        frame.set(
            margin,
            margin,
            width - margin,
            height - margin
        )
        val progressRatio = progress / maxProgress
        canvas.drawArc(frame, 125f, progressRatio * 290f, false, mainPaint)
    }
}
