package com.metalsensor.gold.detector.finder.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.metalsensor.gold.detector.finder.AbsBaseActivity
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.SharedPreferenceUtils
import com.metalsensor.gold.detector.finder.databinding.ActivitySplashBinding
import com.metalsensor.gold.detector.finder.ui.intro.IntroActivity
import com.metalsensor.gold.detector.finder.ui.language.LanguageActivity
import com.metalsensor.gold.detector.finder.utils.AboutCoinViewmodel
import com.metalsensor.gold.detector.finder.utils.CoinViewModel
import com.metalsensor.gold.detector.finder.utils.Const
import com.metalsensor.gold.detector.finder.utils.Const.SPLASH_DELAY
import com.metalsensor.gold.detector.finder.utils.Const.countries
import com.metalsensor.gold.detector.finder.utils.Const.getSystemCountry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AbsBaseActivity<ActivitySplashBinding>(false) {
    override fun getFragmentID(): Int = 0
    override fun getLayoutId(): Int = R.layout.activity_splash

    private val coinViewModel: CoinViewModel by viewModels()
    var check_state: String? = null
    lateinit var providerSharedPreference: SharedPreferenceUtils

    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private lateinit var progressRunnable: Runnable
    private val splashDuration = SPLASH_DELAY // Thời gian delay splash screen

    override fun init() {
        if ((!isTaskRoot && intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intent.action != null) && intent.action == Intent.ACTION_MAIN) {
            finish()
            return
        }
        initData()
    }

    override fun onStart() {
        super.onStart()

        binding.apply {
            pbLoading.max = 100 // Cài đặt giá trị tối đa cho ProgressBar (100%)
            var progress = 0
            val updateInterval = splashDuration / pbLoading.max // Thời gian tăng mỗi bước (ms)

            progressRunnable = Runnable {
                if (progress <= pbLoading.max) {
                    pbLoading.progress = progress
                    progress++
                    handler.postDelayed(progressRunnable, updateInterval)
                }
            }
            handler.post(progressRunnable)
            handler.postDelayed({ initAction() }, splashDuration)
        }
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(progressRunnable)
    }

    private fun initData() {
        providerSharedPreference = SharedPreferenceUtils.getInstance(this@SplashActivity)
        check_state = providerSharedPreference.getStringValue("stateLanguage")
        val codeLang = providerSharedPreference.getStringValue("language")
        Const.trangthai = providerSharedPreference.getStringValue("trangthai")?.toIntOrNull() ?: 0

        if (codeLang.isNullOrEmpty()) {

        } else {

        }
    }

    private fun initAction() {
        val device = getSystemCountry()
        Log.d("Device", "Current device language: $device")
        val countries: List<String> = when (device) {
            "VN" -> listOf("viet-nam")
            "US" -> listOf("united-states")
            "GB" -> listOf("united-kingdom")
            "FR" -> listOf("france")
            "DE" -> listOf("germany")
            "ES" -> listOf("spain")
            "IT" -> listOf("italy")
            "CA" -> listOf("canada")
            "BR" -> listOf("brazil")
            "AU" -> listOf("australia")
            "JP" -> listOf("japon")
            "CN" -> listOf("china")
            "IN" -> listOf("india")
            "MX" -> listOf("mexico")
            "RU" -> listOf("russia")
            "KR" -> listOf("korea")
            "ZA" -> listOf("south-africa")
            "SE" -> listOf("suede")
            "NO" -> listOf("norway")
            "FI" -> listOf("finlande")
            "DK" -> listOf("denmark")
            "NL" -> listOf("netherlands")
            "BE" -> listOf("belgium")
            "CH" -> listOf("switzerland")
            "AT" -> listOf("austria")
            "PL" -> listOf("pologne")
            "PT" -> listOf("portugal")
            "GR" -> listOf("greece")
            "TR" -> listOf("turquie")
            "AR" -> listOf("argentina")
            "CO" -> listOf("colombie")
            "CL" -> listOf("chili")
            "PE" -> listOf("perou")
            "SA" -> listOf("saudi-arabia")
            "AE" -> listOf("emirats_arabes_unis")
            "KW" -> listOf("koweit")
            "QA" -> listOf("qatar")
            "SG" -> listOf("singapour")
            "MY" -> listOf("malaysia")
            "ID" -> listOf("indonesia")
            "TH" -> listOf("thailande")
            "PH" -> listOf("philippines")
            "TW" -> listOf("taiwan")
            "NZ" -> listOf("nouvelle-zelande")
            "PK" -> listOf("pakistan")
            "BD" -> listOf("bangladesh")
            "LK" -> listOf("sri-lanka")
            "NP" -> listOf("nepal")
            "MM" -> listOf("myanmar")
            "LA" -> listOf("laos")
            "KH" -> listOf("cambodge")
            "MN" -> listOf("mongolie")
            "AZ" -> listOf("azerbaijan")
            "KZ" -> listOf("kazakhstan")
            "UZ" -> listOf("ouzbekistan")
            "TM" -> listOf("turkmenistan")
            "KG" -> listOf("kirghizistan")
            "TJ" -> listOf("tadjikistan")
            "AM" -> listOf("armenie")
            "GE" -> listOf("georgie")
            "BY" -> listOf("bielorussie")
            "UA" -> listOf("ukraine")
            "BG" -> listOf("bulgarie")
            "RS" -> listOf("serbie")
            "RO" -> listOf("roumanie")
            "AL" -> listOf("albanie")
            "MK" -> listOf("macedoine")
            "KS" -> listOf("kosovo")
            "ME" -> listOf("montenegro")
            "BA" -> listOf("bosnie-herzegovine")
            "SI" -> listOf("slovenie")
            "HR" -> listOf("croatia")
            "HU" -> listOf("hungary")
            "SK" -> listOf("russia")
            "MD" -> listOf("moldavie")
            "EE" -> listOf("estonie")
            "LV" -> listOf("lettonie")
            "LT" -> listOf("lituanie")
            "IS" -> listOf("islande")
            "MT" -> listOf("malte")
            "CY" -> listOf("chypre")
            "LU" -> listOf("luxembourg")
            "MC" -> listOf("monaco")
            "AD" -> listOf("andorre")
            "SM" -> listOf("saint-marin")
            "LI" -> listOf("liechtenstein")
            "VA" -> listOf("vatican")
            "WS" -> listOf("samoa")
            "AO" -> listOf("angola")
            "BJ" -> listOf("benin")
            "BW" -> listOf("botswana")
            "BF" -> listOf("burkina-faso")
            "BI" -> listOf("burundi")
            "CM" -> listOf("cameroun")
            "CV" -> listOf("cap-vert")
            "CF" -> listOf("republique_centrafricaine")
            "TD" -> listOf("tchad")
            "KM" -> listOf("comores")
            "CD" -> listOf("democratic_republic_congo_period")
            "DJ" -> listOf("djibouti")
            "GQ" -> listOf("guinee_equatoriale")
            "ER" -> listOf("erythree")
            "ET" -> listOf("ethiopie")
            "GA" -> listOf("gabon")
            "GM" -> listOf("gambie")
            "GH" -> listOf("ghana")
            "GN" -> listOf("royaume-uni")
            "GW" -> listOf("guinea-bissau")
            "KE" -> listOf("kenya")
            "LS" -> listOf("lesotho")
            "LR" -> listOf("liberia")
            "LY" -> listOf("libye")
            "MA" -> listOf("maroc")
            "MZ" -> listOf("mozambique")
            "NA" -> listOf("namibia_period")
            "NE" -> listOf("niger")
            "NG" -> listOf("nigeria")
            "RW" -> listOf("rwanda")
            "SN" -> listOf("senegal")
            "SC" -> listOf("seychelles")
            "SL" -> listOf("sierra_leone")
            "SO" -> listOf("somalia")
            "SS" -> listOf("sudan")
            "SD" -> listOf("sudan")
            "TZ" -> listOf("tanzania")
            "TG" -> listOf("togo")
            "TN" -> listOf("tunisie")
            "UG" -> listOf("ouganda")
            "ZM" -> listOf("zambie")
            "ZW" -> listOf("zimbabwe")
            else -> listOf("united-kingdom")
        }


        CoroutineScope(Dispatchers.Main).launch {
            if (check_state.equals("on")) {

                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, LanguageActivity::class.java))
            }
            finish()
        }
        if (coinViewModel.coinList.value.isNullOrEmpty()) {
            lifecycleScope.launch {
                coinViewModel.fetchCoinsForCountries(countries)
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
    }
}
