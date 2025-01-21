package com.metalsensor.gold.detector.finder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.metalsensor.gold.detector.finder.utils.SystemUtils
import com.metalsensor.gold.detector.finder.utils.showSystemUI


abstract class AbsBaseActivity<V : ViewDataBinding>(var fragment : Boolean) : AppCompatActivity() {
    lateinit var binding: V
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var systemUtils: SystemUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        systemUtils  = SystemUtils()
        SystemUtils.setLocale(this)
        binding = DataBindingUtil.setContentView(this, getLayoutId())

        if(fragment){
            navHostFragment =
                supportFragmentManager.findFragmentById(getFragmentID()) as NavHostFragment
            navController = navHostFragment.navController
        }

        init()
    }

    override fun onResume() {
        super.onResume()
        showSystemUI(true)
    }

    abstract fun getFragmentID(): Int
    abstract fun getLayoutId(): Int
    abstract fun init()

}