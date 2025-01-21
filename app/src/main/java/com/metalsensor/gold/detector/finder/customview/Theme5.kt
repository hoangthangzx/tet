package com.metalsensor.gold.detector.finder.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class Theme5 : View {

    private val mainPaint = Paint(Paint.ANTI_ALIAS_FLAG)

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
        mainPaint.strokeWidth = 150f

    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val centerX = w / 2f
        val centerY = h * 0.6f
        val radius = w * 0.45f

        val shader = RadialGradient(
            centerX,
            centerY,
            radius,
            intArrayOf(
                Color.parseColor("#E7711D"),
                Color.parseColor("#592B0A")

            ),
            floatArrayOf(0.8f, 1f),
            Shader.TileMode.CLAMP
        )

        mainPaint.shader = shader
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
