package com.metalsensor.gold.detector.finder.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
class Theme7 : View {
    private val mainPaint = Paint(Paint.ANTI_ALIAS_FLAG)
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
        mainPaint.strokeWidth = 100f

        // Create a linear gradient
        val shader = LinearGradient(
            0f, 0f,
            0f, 100f,
            intArrayOf(
                Color.parseColor("#C932FF"),
                Color.parseColor("#FFE300")
            ),
            null,
            Shader.TileMode.CLAMP
        )
        mainPaint.shader = shader
        setBackgroundColor(Color.TRANSPARENT)
    }

    fun setMaxProgress(max: Float) {
        maxProgress = max
        invalidate()
    }

    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, maxProgress)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height * 1f
        val radius = width * 0.45f

        frame.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        val progressRatio = progress / maxProgress
        canvas.drawArc(frame, 180f, progressRatio * 180f, false, mainPaint)
    }
}