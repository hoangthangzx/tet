package com.metalsensor.gold.detector.finder.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSeekBar
import com.metalsensor.gold.detector.finder.R

class SeekBarSetSizeThumb2 : AppCompatSeekBar {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        // Call method to set the thumb size
        setThumbSize(R.drawable.elip, dpToPx(18f, context), dpToPx(18f, context))
    }

    private fun setThumbSize(resourceId: Int, width: Int, height: Int) {
        val bitmap = BitmapFactory.decodeResource(resources, resourceId)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
        val resizedDrawable = BitmapDrawable(resources, resizedBitmap)
        thumb = resizedDrawable
    }

    // Method to convert dp to pixels
    private fun dpToPx(dp: Float, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return Math.round(dp * density)
    }
}