package com.metalsensor.gold.detector.finder.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.databinding.DialogNoCoinBinding
import com.metalsensor.gold.detector.finder.databinding.NointernetBinding
import com.metalsensor.gold.detector.finder.utils.tap


import javax.inject.Inject

class dialogCoin(private val context: Context) : Dialog(context) {
    var rating: Float = 0F
    private val bindingload by lazy {
        DialogNoCoinBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindingload.root)
        bindingload.imageView6.tap { dismiss() }
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        val drawable = GradientDrawable()
        drawable.cornerRadius = 16f
        window?.setBackgroundDrawable(drawable)
        window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        window?.attributes?.gravity = Gravity.CENTER
    }

    override fun show() {
        super.show()

    }

    fun hideSystemUI() {
        window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                )
    }
    override fun dismiss(){
        super.dismiss()
        hideSystemUI()

    }

    override fun onBackPressed() {

    }
}
