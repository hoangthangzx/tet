package com.metalsensor.gold.detector.finder.base
import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.utils.SystemUtils

//import com.textled.scroller.ledbanner.screencolor.utils.NetworkChangeReceiver

abstract class BaseActivity2<T : ViewBinding> : AppCompatActivity() {
    var TAG="TAG"
    val PERMISSION_REQUEST_CODE = 112
    protected lateinit var binding:T
    protected abstract fun setViewBinding(): T
    private var noInternetDialog: AlertDialog? = null
    protected abstract fun setData()

    protected abstract fun initView()

    protected abstract fun initListener()
//    private lateinit var networkChangeReceiver: NetworkChangeReceiver
lateinit var systemUtils: SystemUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        systemUtils  = SystemUtils()
        SystemUtils.setLocale(this)
        showSystemUI(this,true)
        initWindow()

        binding = setViewBinding()
        setContentView(binding!!.root)
        clearFocusFromClickEditTexts(binding!!.root)
        setData()
        initView()
        initListener()
    }
    private fun handleNetworkChange(isConnected: Boolean) {
        if (!isConnected) {

        } else {
            noInternetDialog?.dismiss()
        }
    }


    fun showSystemUI(activity: AppCompatActivity, white: Boolean) {
        if (white) {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        } else {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
    }
    open fun initWindow() {
        if (Build.VERSION.SDK_INT >= 30) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            window.decorView.windowInsetsController!!.setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)

        }else{
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }


    override fun onResume() {
        super.onResume()
        initWindow()

    }


    private fun showNoNetworkMessage() {

    }
    val notificationPermission: Unit
        get() {
            try {
                if (Build.VERSION.SDK_INT > 32) {
                    Log.d("TAG", "getNotificationPermission: request")
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        PERMISSION_REQUEST_CODE
                    )
                }
            } catch (e: Exception) {
            }
        }


    open fun showKeyboard(view: View?) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    open fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
        clearFocusFromAllEditTexts(binding!!.getRoot())
    }

    open fun hideNavigation(){
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    open fun clearFocusFromAllEditTexts(view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                if (child is EditText) {
                    child.clearFocus()
                    initWindow()
                } else if (child is ViewGroup) {
                    clearFocusFromAllEditTexts(child)
                }
            }
        }
    }
    open fun clearFocusFromClickEditTexts(view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                if (child is EditText) {
                    child.setOnEditorActionListener { v, actionId, event ->
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            child.clearFocus()
                            initWindow()
                            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
                            return@setOnEditorActionListener true
                        }
                        false
                    }
                } else if (child is ViewGroup) {
                    clearFocusFromClickEditTexts(child)
                }
            }
        }
    }

    fun handleBackpress() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent);
        }
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }

    open fun addFragment(fragment: Fragment, viewId: Int = android.R.id.content, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(viewId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }


    open fun replaceBackStackFragment(fragment: Fragment, viewId: Int = android.R.id.content, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(viewId, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        transaction.commit()
    }

    fun replaceFragment(fragment: Fragment, viewId: Int = android.R.id.content,tag : String ?= null) {
        val transaction = supportFragmentManager.beginTransaction()
        val existingFragment = supportFragmentManager.findFragmentByTag(tag)

        if (existingFragment != null) {
            for (fragmentInStack in supportFragmentManager.fragments) {
                transaction.hide(fragmentInStack)
            }
            transaction.show(existingFragment)
        } else {
            transaction.add(viewId, fragment, tag)
        }

        transaction.addToBackStack(tag)

        transaction.commit()
    }
    override fun onDestroy() {
        super.onDestroy()
        // Hủy đăng ký BroadcastReceiver
//        unregisterReceiver(networkChangeReceiver)
    }

}
