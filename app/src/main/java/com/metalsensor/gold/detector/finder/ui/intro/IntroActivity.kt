package com.metalsensor.gold.detector.finder.ui.intro

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.metalsensor.gold.detector.finder.AbsBaseActivity
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.SharedPreferenceUtils
import com.metalsensor.gold.detector.finder.databinding.ActivityIntroBinding
import com.metalsensor.gold.detector.finder.model.TutorialModel
import com.metalsensor.gold.detector.finder.ui.HomeActivity
import com.metalsensor.gold.detector.finder.ui.InteractActivity
import com.metalsensor.gold.detector.finder.ui.PermissionActivity
import com.metalsensor.gold.detector.finder.utils.Const
import com.metalsensor.gold.detector.finder.utils.Const.getSystemCountry
import com.metalsensor.gold.detector.finder.utils.SystemUtils
import com.metalsensor.gold.detector.finder.utils.dpToPx
import com.metalsensor.gold.detector.finder.utils.onSingleClick
import com.metalsensor.gold.detector.finder.utils.showSystemUI


class IntroActivity : AbsBaseActivity<ActivityIntroBinding>(false) {
    override fun getFragmentID(): Int = 0

    override fun getLayoutId(): Int = R.layout.activity_intro

    lateinit var checkState : SharedPreferenceUtils
    private lateinit var dots: Array<ImageView>
    private var listFragment = 3
    var codeLang : String? =  null
    //    var checkFirst = false
    private var viewPagerAdapter: ViewPagerAdapter? = null
    var data = arrayListOf<TutorialModel>()

    override fun init() {
        initView()
        SystemUtils.setPreLanguage(this,codeLang)
        data = arrayListOf(
            TutorialModel(
                R.drawable.intro1,
                getString(R.string.detect_all_metals_gold_silver)
            ),
            TutorialModel(
                R.drawable.intro2,
                getString(R.string.alert_when_metal_is_detected)
            ),
            TutorialModel(
                R.drawable.intro3,
                getString(R.string.sound_sensor)
            )
        )
        viewPagerAdapter = ViewPagerAdapter()
        viewPagerAdapter!!.getData(data)
//        binding.viewPager2.isUserInputEnabled = false

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(100))
        compositePageTransformer.addTransformer { page: View, position: Float ->
            val r = 1 - Math.abs(position)
            page.scaleY = 0.8f + r * 0.2f
            val absPosition = Math.abs(position)
            page.alpha = 1.0f - (1.0f - 0.3f) * absPosition
        }
        binding.viewPager2.setPageTransformer(compositePageTransformer)
        bindViewModel()
        binding.viewPager2.setCurrentItem(1)
        binding.viewPager2.setCurrentItem(0)
        applyGradienttt(binding.tvTitle)
        binding.tvTitle.isSelected = true

    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onResume() {
        super.onResume()
        showSystemUI(true)
    }

    private fun
            initView() {
//        binding.tvNext.applyGradient_1(this)
        val paint = binding.tvNext.paint
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 1.3f
    }

    fun Int.dpToPx(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    private fun bindViewModel() {
        binding.viewPager2.adapter = viewPagerAdapter
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                addBottomDots(position)
            }
        })
        binding.imvClick.onSingleClick {
            val currentItem = binding.viewPager2.currentItem
            val itemCount = viewPagerAdapter?.itemCount ?:0
            if(currentItem < itemCount -1){
                binding.viewPager2.currentItem = currentItem + 1
            }else{
                val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                checkState = SharedPreferenceUtils.getInstance(this@IntroActivity)
                val state = checkState.getStringValue("firstPick")
                if (state.equals("false")){
                    startActivity(Intent(this@IntroActivity, HomeActivity::class.java))
                }else{
                when (Const.trangthai) {
                        0 ->  startActivity(Intent(this@IntroActivity, PermissionActivity::class.java))
                        1 ->  startActivity(Intent(this@IntroActivity, InteractActivity::class.java))
                        2 ->    startActivity(Intent(this@IntroActivity, HomeActivity::class.java))
                        else -> startActivity(Intent(this@IntroActivity, PermissionActivity::class.java))
                    }
//                    if( SharedPreferenceUtils.getInstance(this@IntroActivity).getBooleanValue(Const.Interact)){
//                        Log.d("TAG", "bindViewModel: 2")
//                        startActivity(Intent(this@IntroActivity, HomeActivity::class.java))
//                    }else if( SharedPreferenceUtils.getInstance(this@IntroActivity).getBooleanValue(Const.PERMISSION ) ) {
//                        Log.d("TAG", "bindViewModel: 3")
//                        startActivity(Intent(this@IntroActivity, InteractActivity::class.java))
//                    }else{
//                        Log.d("TAG", "bindViewModel: 4")
//                        startActivity(Intent(this@IntroActivity, PermissionActivity::class.java))
//                    }
                }
                finish()
            }
        }
    }
    private fun addBottomDots(position: Int) {
        val deviceLanguage = getSystemCountry()
        Log.d("DeviceLanguage", "Current device language: $deviceLanguage")
        binding.apply {
//            binding.tvTitle.applyGradient_1(this@IntroActivity)
            val paint = binding.tvTitle.paint
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.strokeWidth = 1f
            tvTitle.text = data[position].title

            lnDots.removeAllViews()
            dots = Array(3) { ImageView(applicationContext) }
            for (i in 0..listFragment - 1) {
                dots[i] = ImageView(applicationContext)
                if (i == position) {
                    dots[i]
                        .setImageDrawable(resources.getDrawable(R.drawable.gradientdot))
                    val params = LinearLayout.LayoutParams(
                        dpToPx(20,applicationContext).toInt(),
                        dpToPx(8,applicationContext).toInt()
                    )
                    params.setMargins(8, 0, 8, 0)
                    lnDots.addView(dots[i], params)
                } else {
                    dots[i]
                        .setImageDrawable(
                            resources.getDrawable(R.drawable.dot_not_pick)
                        )
                    val params = LinearLayout.LayoutParams(
                        dpToPx(8,applicationContext).toInt(),
                        dpToPx(8,applicationContext).toInt()
                    )
                    params.setMargins(8, 0, 8, 0)
                    lnDots.addView(dots[i], params)
                }
            }
        }
    }
    private fun applyGradienttt(textView: TextView) {
        textView.post {
            val textShader: Shader = LinearGradient(
                0f, 0f,
                0f, textView.height.toFloat(),
                intArrayOf(
                    Color.parseColor("#FFF6AE"),
                    Color.parseColor("#B49636")
                ),
                floatArrayOf(0.3f, 1.0f),
                Shader.TileMode.CLAMP
            )
            textView.paint.shader = textShader
        }
    }

    override fun onRestart() {
        super.onRestart()

    }
}