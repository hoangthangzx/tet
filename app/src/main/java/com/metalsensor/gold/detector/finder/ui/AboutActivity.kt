package com.metalsensor.gold.detector.finder.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.metalsensor.gold.detector.finder.AbsBaseActivity
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.SharedPreferenceUtils
import com.metalsensor.gold.detector.finder.databinding.ActivityAboutBinding
import com.metalsensor.gold.detector.finder.databinding.ActivitySplashBinding
import com.metalsensor.gold.detector.finder.ui.intro.IntroActivity
import com.metalsensor.gold.detector.finder.ui.language.LanguageActivity
import com.metalsensor.gold.detector.finder.utils.CoinViewModel
import com.metalsensor.gold.detector.finder.utils.Const.SPLASH_DELAY
import com.metalsensor.gold.detector.finder.utils.tap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AboutActivity :AbsBaseActivity <ActivityAboutBinding>(false) {
    override fun getFragmentID(): Int = 0
    var versionName =""
    override fun getLayoutId(): Int = R.layout.activity_about
    override fun init() {

        try {
            val packageManager: PackageManager = this.packageManager
            val packageInfo: PackageInfo = packageManager.getPackageInfo(this.packageName, 0)
            versionName = packageInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        binding.tvVersion.text = "${getString(R.string.Version)} $versionName"
        binding.imvBack.tap { finish() }

    }
    private fun applyGradientToText(textView: TextView) {
        textView.post {
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
        }
    }
}