package com.metalsensor.gold.detector.finder.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.metalsensor.gold.detector.finder.R

class Kim3 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val handBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.kim3)
    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val frame = RectF()
    private var progress = 0f
    private var maxProgress = 1000f
    private val margin = 30f

    init {
        arcPaint.style = Paint.Style.STROKE
        arcPaint.strokeWidth = 100f
        arcPaint.color = Color.TRANSPARENT
    }

    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, maxProgress)
        invalidate()
    }

    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val centerX = width / 2f
        val centerY = height / 2f

        // Draw arc
        frame.set(
            margin,
            margin,
            width - margin,
            height - margin
        )
        val progressRatio = progress / maxProgress
        canvas.drawArc(frame, 180f, progressRatio * 180f, false, arcPaint)

        // Draw hand
        val angle = progressRatio * 180f + 180f // Align with arc rotation
        val matrix = Matrix()

        val handWidth = dpToPx(160f)
        val handHeight = dpToPx(15f)

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