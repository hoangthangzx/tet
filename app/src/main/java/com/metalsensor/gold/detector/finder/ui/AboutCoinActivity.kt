package com.metalsensor.gold.detector.finder.ui

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.TextView
import com.bumptech.glide.Glide
import com.metalsensor.gold.detector.finder.AbsBaseActivity
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.base.BaseActivity2
import com.metalsensor.gold.detector.finder.databinding.ActivityAboutCoinBinding
import com.metalsensor.gold.detector.finder.databinding.ActivityCoinrecognitionBinding
import com.metalsensor.gold.detector.finder.model.EventMessage
import com.metalsensor.gold.detector.finder.utils.Const
import com.metalsensor.gold.detector.finder.utils.gone
import com.metalsensor.gold.detector.finder.utils.onSingleClick
import com.metalsensor.gold.detector.finder.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@AndroidEntryPoint
class AboutCoinActivity : BaseActivity2<ActivityAboutCoinBinding>() {
    override fun setViewBinding(): ActivityAboutCoinBinding {
        return ActivityAboutCoinBinding.inflate(layoutInflater)
    }

    override fun setData() {
        EventBus.getDefault().register(this)
    }

    override fun initView() {
        Log.d("CoinViewModel", """
                                    Title: ${Const.Title ?: "Unknown"}
                                    Metal: ${Const.Metal}
                                    Rarity Index: ${Const.Rarity}
                                    Shape: ${Const.Shape}
                                    Value: ${Const.Value}
                                    Weight: ${Const.Weight}
                                    Years Range: ${Const.Years}
                                """.trimIndent())
        Glide.with(this)
            .load(Const.url)
            .into(binding.imvcoin)
        Glide.with(this)
            .load(Const.url2)
            .into(binding.imvcoin2)
        binding.tvname.text=Const.Title
        binding.tvinfo.text=Const.Metal
        binding.tvyear.text=Const.Years
        binding.tvdiametter.text=Const.Value
        binding.tvwei.text=Const.Weight

        if (Const.id==0){
            binding.noitem.visible()
            binding.lnAbout.gone()
        }else{
            binding.noitem.gone()
            binding.lnAbout.visible()
        }
        applyGradientToText(binding.tvTitle)
    }

    override fun initListener() {
        binding.apply {
            imvBack.onSingleClick { finish() }
        }
    }
    private fun applyGradientToText(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#DDA600"),
                            Color.parseColor("#D9AC50")
                        ),
                        floatArrayOf(0.0f, 0.9f),
                        Shader.TileMode.CLAMP
                    )
                    textView.paint.shader = textShader
                    textView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        Log.d("CoinViewModel", """
                                    Title: ${Const.Title ?: "Unknown"}
                                    Metal: ${Const.Metal}
                                    Rarity Index: ${Const.Rarity}
                                    Shape: ${Const.Shape}
                                    Value: ${Const.Value}
                                    Weight: ${Const.Weight}
                                    Years Range: ${Const.Years}
                                """.trimIndent())
    }
    override fun initWindow() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: EventMessage) {
        binding.tvname.text=Const.Title
        binding.tvinfo.text=Const.Metal
        binding.tvyear.text=Const.Years
        binding.tvdiametter.text=Const.Value
        binding.tvwei.text=Const.Weight

    }
    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        Const.resetValues()
    }
}