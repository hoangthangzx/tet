package com.metalsensor.gold.detector.finder.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.metalsensor.gold.detector.finder.R

class Kim10 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val frame = RectF()
    private var progress = 0f
    private var maxProgress = 1000f
    private val handBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.kim10)

    init {
        arcPaint.style = Paint.Style.STROKE
        arcPaint.strokeWidth = 150f
        setBackgroundColor(Color.TRANSPARENT)
    }

    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
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

        // Update gradient shader based on actual view size
        val shader = LinearGradient(
            0f,
            0f,
            0f,
            height.toFloat(),
            intArrayOf(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ),
            null,
            Shader.TileMode.CLAMP
        )
        arcPaint.shader = shader
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw arc
        val centerX = width / 2f
        val centerY = height * 0.8f
        val radius = width * 0.5f

        frame.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        val progressRatio = progress / maxProgress
        canvas.drawArc(frame, 150f, progressRatio * 240f, false, arcPaint)

        // Draw hand
        val angle = progressRatio * 240f + 150f // Align with arc rotation
        val matrix = Matrix()

        val handWidth = dpToPx(120f)
        val handHeight = dpToPx(18f)

        val scaledBitmap = Bitmap.createScaledBitmap(
            handBitmap,
            handWidth.toInt(),
            handHeight.toInt(),
            true
        )

        matrix.postTranslate(
            centerX - handWidth / 2f,
            centerY - handHeight / 2f
        )

        matrix.postRotate(angle, centerX, centerY)

        canvas.drawBitmap(scaledBitmap, matrix, null)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handBitmap.recycle()
    }
}