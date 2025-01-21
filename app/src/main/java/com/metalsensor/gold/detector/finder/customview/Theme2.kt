package com.metalsensor.gold.detector.finder.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class Theme2 : View {
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
        mainPaint.strokeWidth = 120f
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
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val centerX = w / 2f
        val centerY = h * 0.65f
        val radius = w * 0.45f

        val shader = RadialGradient(
            centerX,
            centerY,
            radius,
            intArrayOf(
                Color.parseColor("#F0D672"),
                Color.parseColor("#C83871")
            ),
            floatArrayOf(0.8f, 1f),
            Shader.TileMode.CLAMP
        )

        mainPaint.shader = shader
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height * 0.7f
        val radius = width * 0.45f

        frame.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        val progressRatio = progress / maxProgress
        canvas.drawArc(frame, 150f, progressRatio * 235f, false, mainPaint)
    }
}
