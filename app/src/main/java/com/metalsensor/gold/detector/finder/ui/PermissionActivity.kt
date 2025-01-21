package com.metalsensor.gold.detector.finder.ui

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.metalsensor.gold.detector.finder.AbsBaseActivity
import com.metalsensor.gold.detector.finder.GradientTextSpan
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.SharedPreferenceUtils
import com.metalsensor.gold.detector.finder.databinding.ActivityPermissionBinding
import com.metalsensor.gold.detector.finder.databinding.CustomDialogPermissionBinding
import com.metalsensor.gold.detector.finder.utils.Const
import com.metalsensor.gold.detector.finder.utils.SystemUtils
import com.metalsensor.gold.detector.finder.utils.onSingleClick

class PermissionActivity : AbsBaseActivity<ActivityPermissionBinding>(false) {
    override fun getFragmentID(): Int = 0

    override fun getLayoutId(): Int = R.layout.activity_permission

    private val REQUEST_CODE_CAMERA = 2
    private val REQUEST_CAMERA_PERMISSION = 200
    private val REQUEST_CODE_NOTIFICATION_POLICY = 3
    lateinit var providerSharedPreference: SharedPreferenceUtils
    private var isFirstTimeRequestCamera = true
    private var isFirstTimeRequestNoty = true
    private var isFirstTimeRequestCamera2 = true
    private var isFirstTimeRequestNoty2 = true
    var firstPick: String? = null
var TAG ="AA"
    override fun init() {
        initView()
        initData()
        initAction()
    }

    private fun initView() {
        providerSharedPreference = SharedPreferenceUtils.getInstance(this@PermissionActivity)
        firstPick = providerSharedPreference.getStringValue("firstPick")
        Log.d("check_firstPick", "initView: " + firstPick)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            binding.lnNotification.visibility = View.VISIBLE
        } else {
            binding.lnNotification.visibility = View.GONE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val text = "${getString(R.string.allow)} Metal Detector + Gold Finder ${getString(R.string.to_access_notification)}"
            val spannable = SpannableString(text)
            val appName = "Metal Detector + Gold Finder"
            val start = text.indexOf(appName)
            val end = start + appName.length
            if (start != -1) {
                binding.textView5.text = text
                val textWidth = binding.textView5.paint.measureText(text, start, end)
                val colors = intArrayOf(
                    Color.parseColor("#2BD9FF"),
                    Color.parseColor("#2BD9FF"),
                    Color.parseColor("#2BD9FF")
                )
                spannable.setSpan(
                    GradientTextSpan(colors, textWidth),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            binding.textView5.text = spannable
        } else {
            val text = "${getString(R.string.allow)} Metal Detector + Gold Finder ${getString(R.string.to_access_photos_media_and_files_on_your_device)}"
            val spannable = SpannableString(text)
            val appName = "Metal Detector + Gold Finder"
            val start = text.indexOf(appName)
            val end = start + appName.length
            if (start != -1) {
                binding.textView5.text = text
                val textWidth = binding.textView5.paint.measureText(text, start, end)
                val colors = intArrayOf(
                    Color.parseColor("#2BD9FF"),
                    Color.parseColor("#2BD9FF"),
                    Color.parseColor("#2BD9FF")
                )
                spannable.setSpan(
                    GradientTextSpan(colors, textWidth),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            binding.textView5.text = spannable
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun initData() {
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("per_state", "true")
        editor.apply()
    }

    private fun initAction() {
        binding.tvContinue.onSingleClick {
            providerSharedPreference.putStringValue("trangthai","1")
                startActivity(Intent(this@PermissionActivity, InteractActivity::class.java))
        }

        binding.lnNotification.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                updateNotyPermissionStatus()
                binding.lnNotification.isClickable = false
            } else {
                requestNotificationPolicyPermission()
            }
        }

        binding.lnCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                updateRecoedCameraPermissionStatus()
                binding.lnCamera.isClickable = false
            } else {
                requestCameraPermission()
            }
        }
    }

    private fun requestNotificationPolicyPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            updateNotyPermissionStatus()
        } else {
            if (isFirstTimeRequestNoty) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_NOTIFICATION_POLICY)
                isFirstTimeRequestNoty = false
            } else {
                if (isFirstTimeRequestNoty2) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_NOTIFICATION_POLICY)
                    isFirstTimeRequestNoty2 = false
                } else {
                    showSettingsDialog()
                }
            }
        }
    }


    private fun updateNotyPermissionStatus() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            binding.imvSwitchOffMedia.visibility = View.GONE
            binding.imvSwitchOnMedia.visibility = View.VISIBLE
        } else {
            binding.imvSwitchOffMedia.visibility = View.VISIBLE
            binding.imvSwitchOnMedia.visibility = View.GONE
        }
    }

    private fun updateRecoedCameraPermissionStatus() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            binding.imvSwitchOffStorage.visibility = View.GONE
            binding.imvSwitchOnStorage.visibility = View.VISIBLE
        } else {
            binding.imvSwitchOffStorage.visibility = View.VISIBLE
            binding.imvSwitchOnStorage.visibility = View.GONE
        }
    }
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            updateRecoedCameraPermissionStatus()
        } else {
            if (isFirstTimeRequestCamera) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA)
                isFirstTimeRequestCamera = false
            } else {
                if (isFirstTimeRequestCamera2) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA)
                    isFirstTimeRequestCamera2 = false
                } else {
                    showSettingsDialog()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_NOTIFICATION_POLICY -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateNotyPermissionStatus()
                } else {
                    showSettingsDialog()
                }
            }

            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateRecoedCameraPermissionStatus()
                } else {
                    showSettingsDialog()
                }
            }
        }
    }

    private fun showSettingsDialog() {
        val dialogBinding = CustomDialogPermissionBinding.inflate(LayoutInflater.from(this))
        val dialog = Dialog(this)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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

    override fun onResume() {
        super.onResume()
        updateRecoedCameraPermissionStatus()
        updateNotyPermissionStatus()
    }
}