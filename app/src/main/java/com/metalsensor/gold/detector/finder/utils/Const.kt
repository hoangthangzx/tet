package com.metalsensor.gold.detector.finder.utils

import android.content.res.Resources
import android.net.Uri
import android.os.Build
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.model.Coin
import com.metalsensor.gold.detector.finder.model.LanguageModel
import org.opencv.core.Mat
import java.io.File
import java.util.Locale

object Const {
    private val _coinList = mutableListOf<Coin>()

    fun getCoinList(): List<Coin> = _coinList

    fun addCoins(coins: List<Coin>) {
        _coinList.addAll(coins)
    }

    fun clearCoins() {
        _coinList.clear()
    }
    public val sampleImages = mutableListOf<CoinImage>()
    data class CoinImage(
        val id: Int,
        val mat: Mat,
        val url: String,
        val url2: String,
        val name: String
    )
var trangthai=0
var coinlist=0
    var thememe=1
    var id =308304
    var SPLASH_DELAY = 3000L
    var positionLanguageOld = 0
    val LANGUAGE: String = "tjii6tyh5"
    val PERMISSION: String = "krkgeas"
    var Interact : String ="false"
    var KEY_THEME = "uwuhe3ff"
    var TYPE_METAL =""
    var themeIndex = 0
    var themeIndex2 = 0
    val REQUEST_CODE_RINGTONE_PICKER = 1011
    var SOUND_TITLE = "ringtone_01"
    var URI_MUSIC : String = ""
    var KEY_URI_MUSIC = "key_music"
    var KEY_MUSIC_TITLE = "key_title"
    var KEY_URI_MUSIC_GOLD = "key_music_gold"
    var KEY_MUSIC_TITLE_GOLD = "key_title_gold"
    var TRIGGER_VALUE = "60"
    val TRIGGER_VALUE_KEY ="metal"
    val TRIGGER_VALUE_GOLD_KEY = "gold"
    val KEY_THEME_TYPE = "jjhhj"
    var checkPickTheme = false
    var _checkSound = true
    var _checkVibrate = true
    var _checkFlash = true
    var _checkSoundGold = true
    var _checkVibrateGold = true
    var _checkFlashGold = true
    var theme : String = "Theme1"
    var theme2 : String = "Theme6"
    val URI_RINGTONE = "hjsjdj"
    const val SOUND = "KEY_SOUND"
    const val SOUND_ON = "ON_SOUND"
    const val SOUND_OFF = "OFF_SOUND"
    var countries = listOf("united-states")
    var listLanguage = mutableListOf<LanguageModel>(
        LanguageModel("Spanish", "es", R.drawable.ic_flag_spanish),
        LanguageModel("French", "fr", R.drawable.ic_flag_french),
        LanguageModel("Hindi", "hi", R.drawable.ic_flag_hindi),
        LanguageModel("English", "en", R.drawable.ic_flag_english),
        LanguageModel("Portuguese", "pt", R.drawable.ic_flag_portugeese),
        LanguageModel("German", "de", R.drawable.ic_flag_germani),
        LanguageModel("Indonesian", "in", R.drawable.ic_flag_indo)
    )

    var name=""
    var url=""
    var url2=""
    var Title=""
    var Metal=""
    var Rarity=""
    var Shape=""
    var Value=""
    var Weight=""
    var Years=""
    fun resetValues() {
        Title = ""
        Metal = ""
        Rarity = ""
        Shape = ""
        Value = ""
        Weight = ""
        Years = ""
    }
    fun getSystemCountry(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Android 7.0 trở lên: sử dụng `locales`
            Resources.getSystem().configuration.locales[0].country
        } else {
            // Android 6.0 trở xuống: sử dụng `locale`
            Resources.getSystem().configuration.locale.country
        }
    }

}