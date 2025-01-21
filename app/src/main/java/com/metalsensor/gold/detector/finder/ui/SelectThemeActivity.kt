package com.metalsensor.gold.detector.finder.ui

import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.metalsensor.gold.detector.finder.utils.Const.themeIndex2
import com.metalsensor.gold.detector.finder.utils.onSingleClick
import com.metalsensor.gold.detector.finder.utils.TapListener
import com.metalsensor.gold.detector.finder.utils.dpToPx
import com.metalsensor.gold.detector.finder.utils.invisible
import com.metalsensor.gold.detector.finder.utils.tap
import com.metalsensor.gold.detector.finder.utils.visible
import kotlin.math.abs

class SelectThemeActivity : AbsBaseActivity<ActivitySelectThemeBinding>(false) {
    override fun getFragmentID(): Int = 0
    lateinit var shareData: SharedPreferenceUtils
    private lateinit var dots: Array<ImageView>
    private lateinit var themeAdapter: ThemeAdapter
    private var c = 0
    private val themes = listOf(
        ThemeModel("0", R.drawable.detecter1),
        ThemeModel("1", R.drawable.detecter2),
        ThemeModel("2", R.drawable.detecter3),
        ThemeModel("3", R.drawable.detecter4),
        ThemeModel("4", R.drawable.detecter5),
    )
    private val themes2 = listOf(
        ThemeModel("5", R.drawable.detecter6),
        ThemeModel("6", R.drawable.detecter7),
        ThemeModel("7", R.drawable.detecter8),
        ThemeModel("8", R.drawable.detecter9),
        ThemeModel("9", R.drawable.detecter10)
    )
    private var a = 0
    override fun getLayoutId(): Int = R.layout.activity_select_theme

    override fun init() {
        initData()
        initView()
        initAction()
    }

    private fun initView() {
        binding.imvBack.tap { finish() }
        if (Const.thememe == 1) {
            if (themeIndex>0){
                c=1
            }
            themeAdapter = ThemeAdapter(themes, themeIndex)
            Log.d("TAG", "initView: vao 2")
            addBottomDots(themeIndex)

        } else {

            var b = themeIndex2 - 5
            if (b>0){
                c=1
            }
            Log.d("TAG", "initView: " + b + "the" + themeIndex2)
            themeAdapter = ThemeAdapter(themes2, b)
            Log.d("TAG", "initView: vao 4")
            addBottomDots(themeIndex2 - 5)
        }

        binding.viewPager.apply {
            adapter = themeAdapter
            offscreenPageLimit = 3
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
            if (Const.thememe == 1) {
                setCurrentItem(themeIndex, false)
            } else {
                setCurrentItem(themeIndex2 - 5, false)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (Const.thememe == 1) {
                    val currentThemes = if (Const.thememe == 1) themes else themes2
                    a = currentThemes[position].title.toInt()
                } else {
                    val currentThemes = if (Const.thememe == 1) themes else themes2
                    a = currentThemes[position].title.toInt()
                }
                c++

                themeAdapter.updateSelectedPosition(position)
                checkPickTheme = true
                addBottomDots(position)
            }
        })
    }

    private fun initData() {
        shareData = SharedPreferenceUtils.getInstance(this@SelectThemeActivity)
        if (Const.thememe == 1) {
            themeIndex = shareData.getNumberCheck("keyTheme")
        } else {
            themeIndex2 = shareData.getNumberCheck2("keyTheme2")
        }
        Log.d("TAG", "initDatat: " + themeIndex)
        Log.d("TAG", "initDatat: " + themeIndex2)
        checkPickTheme = shareData.getBooleanValue(KEY_THEME)
    }

    private fun initAction() {
        binding.ctApply.onSingleClick {
            if (checkPickTheme) {

                if (Const.thememe == 1) {
                    themeIndex = a
                    shareData.putNumber("keyTheme", a);
                    shareData.putBooleanValue(KEY_THEME, true);
                } else {
                    themeIndex2 = a
                    Log.d("TAG", "initAction: " + themeIndex2)
                    shareData.putNumber("keyTheme2", a);
                    shareData.putBooleanValue(KEY_THEME, true);
                }
                if (Const.thememe == 1) {
                    when (Const.themeIndex) {
                        0 -> Const.theme = "Theme1"
                        1 -> Const.theme = "Theme2"
                        2 -> Const.theme = "Theme3"
                        3 -> Const.theme = "Theme4"
                        4 -> Const.theme = "Theme5"
                    }
                    startActivity(
                        Intent(
                            this@SelectThemeActivity,
                            MetalDetectorActivity::class.java
                        )
                    )
                } else {
                    when (a) {
                        0 -> Const.theme2 = "Theme6"
                        1 -> Const.theme2 = "Theme7"
                        2 -> Const.theme2 = "Theme8"
                        3 -> Const.theme2 = "Theme9"
                        4 -> Const.theme2 = "Theme10"
                    }
                    startActivity(
                        Intent(
                            this@SelectThemeActivity,
                            GoldDetecterActivity::class.java
                        )
                    )
                }
                finish()
            } else {
                Toast.makeText(
                    this@SelectThemeActivity,
                    R.string.please_choose_a_theme,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addBottomDots(position: Int) {
        Log.d("TAG", "addBottomDots: c " + c)
        binding.apply {
            if (c>1) {
                lnDots.removeAllViews()
            }
            val validPosition = position.coerceIn(0, 4)

            dots = Array(5) { ImageView(applicationContext) }

            for (i in 0..4) {
                dots[i] = ImageView(applicationContext)
                val params = if (i == validPosition) {
                    // Selected dot
                    LinearLayout.LayoutParams(
                        dpToPx(20, applicationContext).toInt(),
                        dpToPx(8, applicationContext).toInt()
                    ).apply {
                        setMargins(8, 0, 8, 0)
                    }
                } else {
                    // Unselected dot
                    LinearLayout.LayoutParams(
                        dpToPx(8, applicationContext).toInt(),
                        dpToPx(8, applicationContext).toInt()
                    ).apply {
                        setMargins(8, 0, 8, 0)
                    }
                }

                // Set dot image
                dots[i].setImageResource(
                    if (i == validPosition) R.drawable.gradientdot
                    else R.drawable.dot_not_pick2
                )

                // Add to container
                lnDots.addView(dots[i], params)
            }
            // Force redraw
            lnDots.requestLayout()
        }
    }
}
