package com.metalsensor.gold.detector.finder.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewManagerFactory
import com.metalsensor.gold.detector.finder.AbsBaseActivity
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.SharedPreferenceUtils
import com.metalsensor.gold.detector.finder.databinding.ActivityHomeBinding
import com.metalsensor.gold.detector.finder.databinding.CustomDialogNoteBinding
import com.metalsensor.gold.detector.finder.databinding.CustomDialogNotiBinding
import com.metalsensor.gold.detector.finder.databinding.DialogRateBinding
import com.metalsensor.gold.detector.finder.dialog.dialogmang
import com.metalsensor.gold.detector.finder.utils.CoinViewModel
import com.metalsensor.gold.detector.finder.utils.Const
import com.metalsensor.gold.detector.finder.utils.Const._checkFlash
import com.metalsensor.gold.detector.finder.utils.Const._checkFlashGold
import com.metalsensor.gold.detector.finder.utils.Const._checkSound
import com.metalsensor.gold.detector.finder.utils.Const._checkSoundGold
import com.metalsensor.gold.detector.finder.utils.Const._checkVibrate
import com.metalsensor.gold.detector.finder.utils.Const._checkVibrateGold
import com.metalsensor.gold.detector.finder.utils.InternetConnectionChecker
import com.metalsensor.gold.detector.finder.utils.SystemUtils
import com.metalsensor.gold.detector.finder.utils.onSingleClick
import com.metalsensor.gold.detector.finder.utils.tap
import kotlinx.coroutines.launch
import org.opencv.android.OpenCVLoader
import kotlin.system.exitProcess

class HomeActivity : AbsBaseActivity<ActivityHomeBinding>(false) {
    override fun getFragmentID(): Int = 0
    lateinit var providerSharedPreference : SharedPreferenceUtils
    override fun getLayoutId(): Int = R.layout.activity_home
    private val handel by lazy {
        Handler(Looper.getMainLooper())
    }
    private val loadingDialog by lazy {
        dialogmang(this)
    }
    private lateinit var internetChecker: InternetConnectionChecker
    private var isClick = true
    override fun init() {
        initData()
        initAction()
    }

    private fun initData() {
        providerSharedPreference = SharedPreferenceUtils.getInstance(this@HomeActivity)
        providerSharedPreference.putStringValue("firstPick","false")
    }
    private fun initAction() {

        binding.imvSt.onSingleClick {
            startActivity(Intent(this@HomeActivity,MoreActivity::class.java))
        }
        if (OpenCVLoader.initDebug()){
            Log.d("TAG", "onCreate: oke")
        }else{
            Log.d("TAG", "onCreate: no")
        }
        binding.ctlMetalDector.onSingleClick {
            Const.thememe=1
            val packageManager = this.packageManager
            val hasMagnetometer = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS)
            if (hasMagnetometer) {
                _checkSound = true
                _checkVibrate = true
                _checkFlash = true
                Const.TYPE_METAL = "METAL"
                Const.themeIndex = providerSharedPreference.getNumber("keyTheme")
                startActivity(Intent(this@HomeActivity,MetalDetectorActivity::class.java))
            } else {
                showDialogNoti()
            }
        }
        binding.ctlGoldDector.onSingleClick {
            Const.thememe=2
            val packageManager = this.packageManager
            val hasMagnetometer = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS)
            if (hasMagnetometer) {
                _checkSoundGold = true
                _checkVibrateGold = true
                _checkFlashGold = true
                Const.themeIndex2 = providerSharedPreference.getNumber("keyTheme2")
                Const.TYPE_METAL = "GOLD"
                startActivity(Intent(this@HomeActivity,GoldDetecterActivity::class.java))
            }else{
                showDialogNoti()
            }
        }
        binding.ctcoin.tap {
            internetChecker.unregisterNetworkCallback()
            internetChecker = InternetConnectionChecker(this)
            check()
            check()
            coin() }

    }

    private fun showDialogNoti() {
        val dialogBinding  = CustomDialogNotiBinding.inflate(LayoutInflater.from(this))
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(getDrawable(R.color.transparent))
        dialog.setCancelable(false)

        dialogBinding.lnOkBtn.onSingleClick {
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        var inAppCount = providerSharedPreference.getNumberRate("RateNumber")
        inAppCount ++
        providerSharedPreference.putNumber("RateNumber",inAppCount)
        Log.d("check_rate", "onBackPressed: "+ inAppCount + providerSharedPreference.getBooleanValue("booleanRate"))
        if(!providerSharedPreference.getBooleanValue("booleanRate")){
            if( inAppCount % 2 != 0 ){
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                val bindingDialog = DialogRateBinding.inflate(layoutInflater)
                dialog.setContentView(bindingDialog.root)
                dialog.setCanceledOnTouchOutside(false)
                dialog.setCancelable(false)
                val window = dialog.window ?: return
                window.setGravity(Gravity.CENTER)
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                bindingDialog.apply {
                    ll1.rating = 0f
                    ll1.setOnRatingChangeListener { _, p1, _ ->
                        if (p1.toInt() == 0) {
                            tv1.text = getString(R.string.zero_start_title)
                            tv2.text = getString(R.string.zero_start)
                        } else if (p1.toInt() in 1..3) {
                            tv1.text = getString(R.string.one_start_title)
                            tv2.text = getString(R.string.one_start)
                        } else {
                            tv1.text = getString(R.string.four_start_title)
                            tv2.text = getString(R.string.four_start)
                        }
                        when (p1.toInt()) {
                            0 -> {
                                imvAvtRate.setImageDrawable(ContextCompat.getDrawable(this@HomeActivity, R.drawable.ic_rate_rero))
                            }
                            1 -> {
                                imvAvtRate.setImageDrawable(ContextCompat.getDrawable(this@HomeActivity, R.drawable.ic_rate_one))
                            }
                            2 -> {
                                imvAvtRate.setImageDrawable(ContextCompat.getDrawable(this@HomeActivity, R.drawable.ic_rate_two))
                            }
                            3 -> {
                                imvAvtRate.setImageDrawable(ContextCompat.getDrawable(this@HomeActivity, R.drawable.ic_rate_three))
                            }
                            4 -> {
                                imvAvtRate.setImageDrawable(ContextCompat.getDrawable(this@HomeActivity, R.drawable.ic_rate_four))
                            }
                            5 -> {
                                imvAvtRate.setImageDrawable(ContextCompat.getDrawable(this@HomeActivity, R.drawable.ic_rate_five))
                            }
                        }
                    }
                    btnVote.onSingleClick {
                        providerSharedPreference.putBooleanValue("booleanRate", true)
                        if (ll1.rating.toInt() >= 3) {
                            Toast.makeText(this@HomeActivity, R.string.successful, Toast.LENGTH_SHORT).show()
                            reviewApp(this@HomeActivity, true)
                            dialog.dismiss()
                            finishAffinity()
                        } else if(ll1.rating.toInt() == 0){
                            Toast.makeText(this@HomeActivity, R.string.please_give_a_review, Toast.LENGTH_SHORT).show()
                        }else{
                            dialog.dismiss()
                            finishAffinity()
                        }
                    }
                    btnCancal.onSingleClick {
                        inAppCount += 1
                        providerSharedPreference.putBooleanValue("booleanRate",false)
                        dialog.dismiss()
                        finishAffinity()
                    }
                }
                dialog.show()
            }else{
                finishAffinity()
            }
        }else{
            finishAffinity()
        }
    }
    private fun reviewApp(context: Context, isBackPress: Boolean) {
        val manager = ReviewManagerFactory.create(context)
        val request = manager.requestReviewFlow();
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                Log.e("ReviewInfo", "" + reviewInfo.toString())
                val flow = (context as Activity?)?.let { manager.launchReviewFlow(it, reviewInfo) }
                flow?.addOnCompleteListener { task2: Task<Void> ->
                    Log.e("ReviewSuccess", task2.toString())
//                    finishAffinity()
                    if (isBackPress) {
                        exitProcess(0)
                    }
                }
            } else {
                Log.e("ReviewError", task.exception.toString());
//                finishAffinity()
                if (isBackPress) {
                    exitProcess(0)
                }
            }
        }
    }
    private fun coin() {
        check()
        check()
        handel.postDelayed({
            if (a == 0) {
                isClick = true
                intent = Intent(this@HomeActivity, CoinrecognitionActivity::class.java)
                startActivity(intent)
                handel.postDelayed({ isClick = true }, 500)
            }else{
                loadingDialog.show()
            }
        }, 500)

    }
    private var a=0
    private fun check() {
        lifecycleScope.launch {
            internetChecker.connectionState.collect { state ->
                when (state) {
                    is InternetConnectionChecker.ConnectionState.Unknown -> {
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
                Log.d("TAG", "check: "+a)
            }
        }

        internetChecker.startPeriodicCheck()
    }
    override fun onPause() {
        super.onPause()
        internetChecker.unregisterNetworkCallback()
    }

    override fun onResume() {
        super.onResume()
        internetChecker = InternetConnectionChecker(this)
        check()
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "onDestroy: ")
        internetChecker.unregisterNetworkCallback()
    }
}