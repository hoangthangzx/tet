package com.metalsensor.gold.detector.finder.ui

import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.metalsensor.gold.detector.finder.AbsBaseActivity
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.SharedPreferenceUtils
import com.metalsensor.gold.detector.finder.adapter.ThemeAdapter
import com.metalsensor.gold.detector.finder.databinding.ActivityInteractBinding
import com.metalsensor.gold.detector.finder.databinding.ActivitySelectThemeBinding
import com.metalsensor.gold.detector.finder.model.ThemeModel
import com.metalsensor.gold.detector.finder.utils.Const
import com.metalsensor.gold.detector.finder.utils.Const.KEY_THEME
import com.metalsensor.gold.detector.finder.utils.Const.KEY_THEME_TYPE
import com.metalsensor.gold.detector.finder.utils.Const.checkPickTheme
import com.metalsensor.gold.detector.finder.utils.Const.themeIndex
import com.metalsensor.gold.detector.finder.utils.onSingleClick
import com.metalsensor.gold.detector.finder.utils.TapListener
import com.metalsensor.gold.detector.finder.utils.invisible
import com.metalsensor.gold.detector.finder.utils.visible
import kotlin.math.abs

class InteractActivity : AbsBaseActivity<ActivityInteractBinding>(false) {
    override fun getFragmentID(): Int = 0
    lateinit var shareData: SharedPreferenceUtils
    private lateinit var themeAdapter: ThemeAdapter
    lateinit var providerSharedPreference: SharedPreferenceUtils
    private val themes = listOf(
        ThemeModel("0", R.drawable.detecter1),
        ThemeModel("1", R.drawable.detecter2),
        ThemeModel("2", R.drawable.detecter3),
        ThemeModel("3", R.drawable.detecter4),
        ThemeModel("4", R.drawable.detecter5),
        ThemeModel("5", R.drawable.detecter6),
        ThemeModel("6", R.drawable.detecter7),
        ThemeModel("7", R.drawable.detecter8),
        ThemeModel("8", R.drawable.detecter9),
        ThemeModel("9", R.drawable.detecter10)
    )

    override fun getLayoutId(): Int = R.layout.activity_interact

    override fun init() {
        initData()
        initView()
        initAction()
    }

    private fun initView() {
        // Initialize adapter with CompositePageTransformer
        providerSharedPreference = SharedPreferenceUtils.getInstance(this@InteractActivity)
        themeAdapter = ThemeAdapter(themes, themeIndex)
        binding.viewPager.apply {
            adapter = themeAdapter
            offscreenPageLimit = 3
            themeAdapter.updateSelectedPosition(0)
            // Set spacing and scaling
            val transformer = CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(0)) // Margin between items
                addTransformer { page, position ->
                    val r = 1 - abs(position)
                    page.scaleX = 0.7f + r * 0.14f
                    val scaleFactor = 0.7f + r * 0.14f
                    val width = page.width
                    val translateValue = width * (1 - scaleFactor) / 2f
                    page.translationX = -position * translateValue
                }
            }
            setPageTransformer(transformer)

            setCurrentItem(themeIndex, false)
        }

        // Handle page change
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                themeIndex = position
                themeAdapter.updateSelectedPosition(position)
                checkPickTheme = true
            }
        })
    }

    private fun initData() {
        shareData = SharedPreferenceUtils.getInstance(this@InteractActivity)
        themeIndex = shareData.getNumberCheck("keyTheme")
        checkPickTheme = shareData.getBooleanValue(KEY_THEME)
    }

    private fun initAction() {
        binding.ctApply.onSingleClick {
            if (checkPickTheme) {
                if (themeIndex >= 0 && themeIndex <= 4) {
                    shareData.putNumber("keyTheme", themeIndex);
                    shareData.putBooleanValue(KEY_THEME, true);
                } else if (themeIndex >= 5 && themeIndex <= 9) {
                    shareData.putNumber("keyTheme2", themeIndex);
                    shareData.putBooleanValue(KEY_THEME, true);
                }
                providerSharedPreference.putStringValue("trangthai","1")
                startActivity(Intent(this@InteractActivity, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this@InteractActivity,
                    R.string.please_choose_a_theme,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (shareData.getBooleanValue(KEY_THEME_TYPE)) {
            checkPickTheme = false
            finish()
        } else {
            checkPickTheme = false
            finishAffinity()
        }
    }
}
