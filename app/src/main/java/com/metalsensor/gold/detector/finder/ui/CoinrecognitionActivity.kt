package com.metalsensor.gold.detector.finder.ui

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.databinding.ActivityCoinrecognitionBinding
import com.metalsensor.gold.detector.finder.adapter.ViewPagerAdapter
import com.metalsensor.gold.detector.finder.base.BaseActivity2
import com.metalsensor.gold.detector.finder.databinding.CustomDialogPermissionBinding
import com.metalsensor.gold.detector.finder.dialog.dialogmang
import com.metalsensor.gold.detector.finder.model.EventMessage2
import com.metalsensor.gold.detector.finder.utils.InternetConnectionChecker
import com.metalsensor.gold.detector.finder.utils.tap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.opencv.core.*

@AndroidEntryPoint
class CoinrecognitionActivity : BaseActivity2<ActivityCoinrecognitionBinding>() {
    override fun setViewBinding(): ActivityCoinrecognitionBinding {
        return ActivityCoinrecognitionBinding.inflate(layoutInflater)
    }

    private lateinit var internetChecker: InternetConnectionChecker
    override fun setData() {
        EventBus.getDefault().register(this)
    }
    private val handel by lazy {
        Handler(Looper.getMainLooper())
    }
    override fun initView() {

    }

    private val loadingDialog by lazy {
        dialogmang(this)
    }

    private fun applyGradientToText(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
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

    override fun initListener() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinrecognitionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showSystemUI(this, true)

        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab: TabLayout.Tab?, position: Int -> }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateTabUI(position)
            }
        })
        binding.library.setOnClickListener { v ->
            Log.d(TAG, "initView: ")
            binding.viewPager.currentItem = 0
            updateTabUI(0)
        }

        binding.scan.setOnClickListener { v ->
            itn()
            handel.postDelayed({
                if (a == 0) {
                    if (checkPermissionCamera()) {
                        Log.d(TAG, "initView:2 ")
                        binding.viewPager.currentItem = 1
                        updateTabUI(1)
                    } else {
                        requestAppPermissioncamera(REQUEST_CODE_CAMERA)
                    }
                } else {
                    loadingDialog.show()
                }
            }, 500)

        }
        binding.imvBack.tap {
            Log.d(TAG, "finish")
            finish()
        }
        applyGradientToText(binding.tvTitle)
    }

    private fun checkPermissionCamera(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestAppPermissioncamera(requestCode: Int) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.CAMERA
            ),
            requestCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    binding.viewPager.currentItem = 1
                    updateTabUI(1)
                } else {
                    showSettingsDialog()
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_CAMERA = 100
    }

    private fun updateTabUI(position: Int) {
        when (position) {
            0 -> {
                binding.linearLayout5.setBackgroundResource(R.drawable.coinbutton)
                binding.imv1.setImageResource(R.drawable.imagelbrr)
                binding.tv1.setTextColor(Color.parseColor("#402B02")) // Red color
                binding.tv2.setTextColor(Color.parseColor("#A9A5A5"))
                binding.imv2.setImageResource(R.drawable.imagescan)
            }

            1 -> {
                binding.linearLayout5.setBackgroundResource(R.drawable.coinbt2)
                binding.imv1.setImageResource(R.drawable.library2)
                binding.tv1.setTextColor(Color.parseColor("#A9A5A5")) // Red color
                binding.tv2.setTextColor(Color.parseColor("#402B02"))
                binding.imv2.setImageResource(R.drawable.scan2)
            }
        }
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

    private var a = 0
    private fun check() {
        lifecycleScope.launch {
            internetChecker.connectionState.collect { state ->
                when (state) {
                    is InternetConnectionChecker.ConnectionState.Unknown -> {
                        a = 1
                    }

                    is InternetConnectionChecker.ConnectionState.NoConnection -> {
                        a = 1
                    }

                    is InternetConnectionChecker.ConnectionState.HasConnection -> {
                        a = 1
                    }

                    is InternetConnectionChecker.ConnectionState.HasInternet -> {
                        a = 0
                    }

                    else -> {}
                }
                Log.d("TAG", "check: " + a)
            }
        }

        internetChecker.startPeriodicCheck()
    }

    override fun onPause() {
        super.onPause()
        internetChecker.unregisterNetworkCallback()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        internetChecker = InternetConnectionChecker(this)
        check()
    }

    override fun onRestart() {
        super.onRestart()
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "onDestroy: ")
        EventBus.getDefault().unregister(this)
        internetChecker.unregisterNetworkCallback()
    }

    private fun showSettingsDialog() {
        val dialogBinding = CustomDialogPermissionBinding.inflate(LayoutInflater.from(this))
        val dialog = Dialog(this)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(getDrawable(R.color.transparent))
        dialog.setContentView(dialogBinding.root)
        dialog.show()

        dialogBinding.tvYes.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
            dialog.dismiss()
        }
        dialogBinding.tvNo.setOnClickListener {
            dialog.dismiss() // Đóng dialog
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: EventMessage2) {
        binding.viewPager.currentItem = 0
        updateTabUI(0)
        loadingDialog.show()
    }

    fun itn() {
        internetChecker.unregisterNetworkCallback()
        internetChecker = InternetConnectionChecker(this)
        check()
    }
}