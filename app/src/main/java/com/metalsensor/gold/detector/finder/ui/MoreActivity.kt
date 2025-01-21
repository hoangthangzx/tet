package com.metalsensor.gold.detector.finder.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.metalsensor.gold.detector.finder.AbsBaseActivity
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.SharedPreferenceUtils
import com.metalsensor.gold.detector.finder.databinding.ActivityMoreBinding
import com.metalsensor.gold.detector.finder.databinding.DialogRateBinding
import com.metalsensor.gold.detector.finder.ui.language.LanguageActivity
import com.metalsensor.gold.detector.finder.utils.SystemUtils
import com.metalsensor.gold.detector.finder.utils.onSingleClick
import com.metalsensor.gold.detector.finder.utils.openPolicy
import kotlin.system.exitProcess

class MoreActivity : AbsBaseActivity<ActivityMoreBinding>(false) {
    override fun getFragmentID(): Int = 0
    lateinit var providerSharedPreference: SharedPreferenceUtils

    override fun getLayoutId(): Int = R.layout.activity_more

    override fun init() {
        initData()
        initView()
        initAction()
    }

    private fun initView() {
        if (providerSharedPreference.getBooleanValue("booleanRate")
        ) {
            binding.lnRate.visibility = View.GONE
        }
    }

    private fun initData() {
        providerSharedPreference = SharedPreferenceUtils.getInstance(this)
    }

    private fun initAction() {
        binding.lnLanguage.onSingleClick {
            startActivity(Intent(this@MoreActivity, LanguageActivity::class.java))
        }

        binding.imvBack.onSingleClick {
            finish()
        }

        binding.lnRate.onSingleClick {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val bindingDialog = DialogRateBinding.inflate(layoutInflater)
            dialog.setContentView(bindingDialog.root)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            val window = dialog.window ?: return@onSingleClick
            window.setGravity(Gravity.CENTER)
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
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
                            imvAvtRate.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@MoreActivity,
                                    R.drawable.ic_rate_rero
                                )
                            )
                        }

                        1 -> {
                            imvAvtRate.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@MoreActivity,
                                    R.drawable.ic_rate_one
                                )
                            )
                        }

                        2 -> {
                            imvAvtRate.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@MoreActivity,
                                    R.drawable.ic_rate_two
                                )
                            )
                        }

                        3 -> {
                            imvAvtRate.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@MoreActivity,
                                    R.drawable.ic_rate_three
                                )
                            )
                        }

                        4 -> {
                            imvAvtRate.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@MoreActivity,
                                    R.drawable.ic_rate_four
                                )
                            )
                        }

                        5 -> {
                            imvAvtRate.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@MoreActivity,
                                    R.drawable.ic_rate_five
                                )
                            )
                        }
                    }
                }
                btnVote.onSingleClick {
                    Toast.makeText(this@MoreActivity, R.string.successful, Toast.LENGTH_SHORT)
                        .show()
                    if (ll1.rating.toInt() >= 3) {
                        rateApp(this@MoreActivity, false)
                        providerSharedPreference.putBooleanValue("booleanRate", true)
                        binding.lnRate.visibility = View.GONE
                        dialog.dismiss()
                    } else if (ll1.rating.toInt() == 0) {
                        Toast.makeText(
                            this@MoreActivity,
                            R.string.please_give_a_review,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        providerSharedPreference.putBooleanValue("booleanRate", true)
                        dialog.dismiss()
                        binding.lnRate.visibility = View.GONE
                    }
                }
                btnCancal.onSingleClick {
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
        binding.lnPolicy.onSingleClick {
            openPolicy(
                this,
                "https://sites.google.com/andesgroup.app/privacy-policy-gold-finder/"
            )
        }


        binding.lnAbout.onSingleClick {
            startActivity(Intent(this@MoreActivity, AboutActivity::class.java))
        }
        binding.lnShareApp.onSingleClick {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                var shareMessage = "Download application: "
                shareMessage =
                    (shareMessage + "https://play.google.com/store/apps/details?id=" + this@MoreActivity.packageName)
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "Share with"))
            } catch (e: Exception) {
            }
        }
    }
    fun rateApp(context: Context, isBackPress: Boolean) {
        val manager: ReviewManager = ReviewManagerFactory.create(context)
        val request: Task<ReviewInfo> = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                Log.e("ReviewInfo", "" + reviewInfo.toString())
                val flow: Task<Void> = manager.launchReviewFlow(context as Activity, reviewInfo)
                flow.addOnCompleteListener { task2 ->
                    Log.e("ReviewSucces", "" + task2.toString())
                    if (isBackPress){
                        System.exit(0)
                    }
                }
            } else {
                if (isBackPress){
                    System.exit(0)
                }
            }
        }
    }

}