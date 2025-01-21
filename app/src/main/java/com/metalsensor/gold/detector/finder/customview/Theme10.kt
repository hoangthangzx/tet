package com.metalsensor.gold.detector.finder.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class Theme10 : View {
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
        mainPaint.strokeWidth = 130f

        // Create a linear gradient
        val shader = LinearGradient(
            0f, 0f,
            0f, 100f,
            intArrayOf(
                Color.parseColor("#FF4805"),
                Color.parseColor("#FDDF0C")
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

        // Adjust the position and size of the arc
        val centerX = width / 2f
        val centerY = height * 0.8f // Move the arc higher by reducing the Y position
        val radius = width * 0.5f   // Increase the radius to make the arc larger

        frame.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        val progressRatio = progress / maxProgress
        canvas.drawArc(frame, 160f, progressRatio * 220f, false, mainPaint)
    }
}
