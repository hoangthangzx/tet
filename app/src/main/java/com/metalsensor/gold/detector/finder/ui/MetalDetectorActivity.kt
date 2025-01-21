package com.metalsensor.gold.detector.finder.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.SharedPreferenceUtils
import com.metalsensor.gold.detector.finder.databinding.ActivityMetalDetectorBinding
import com.metalsensor.gold.detector.finder.databinding.CustomDialogNoteBinding
import com.metalsensor.gold.detector.finder.manager.FlashlightManager
import com.metalsensor.gold.detector.finder.utils.Const
import com.metalsensor.gold.detector.finder.utils.Const.KEY_URI_MUSIC
import com.metalsensor.gold.detector.finder.utils.Const.KEY_URI_MUSIC_GOLD
import com.metalsensor.gold.detector.finder.utils.Const.TRIGGER_VALUE
import com.metalsensor.gold.detector.finder.utils.Const.TRIGGER_VALUE_GOLD_KEY
import com.metalsensor.gold.detector.finder.utils.Const.TRIGGER_VALUE_KEY
import com.metalsensor.gold.detector.finder.utils.Const.TYPE_METAL
import com.metalsensor.gold.detector.finder.utils.Const.URI_MUSIC
import com.metalsensor.gold.detector.finder.utils.Const._checkFlash
import com.metalsensor.gold.detector.finder.utils.Const._checkFlashGold
import com.metalsensor.gold.detector.finder.utils.Const._checkSound
import com.metalsensor.gold.detector.finder.utils.Const._checkSoundGold
import com.metalsensor.gold.detector.finder.utils.Const._checkVibrate
import com.metalsensor.gold.detector.finder.utils.Const._checkVibrateGold
import com.metalsensor.gold.detector.finder.utils.applyGradient_10
import com.metalsensor.gold.detector.finder.utils.applyGradient_2
import com.metalsensor.gold.detector.finder.utils.applyGradient_9
import com.metalsensor.gold.detector.finder.utils.gone
import com.metalsensor.gold.detector.finder.utils.onSingleClick
import com.metalsensor.gold.detector.finder.utils.setTextRes
import com.metalsensor.gold.detector.finder.utils.showSystemUI
import com.metalsensor.gold.detector.finder.utils.startVibration
import com.metalsensor.gold.detector.finder.utils.stopVibration
import com.metalsensor.gold.detector.finder.utils.visible
import java.lang.Math.abs
import kotlin.math.PI
import kotlin.math.sin

class MetalDetectorActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var flashlightManager: FlashlightManager
    var mainHandler = Handler(Looper.getMainLooper())
    private var TAG = "AAA"
    private val handel by lazy {
        Handler(Looper.getMainLooper())
    }
    private var isClick = true

    private var mediaPlayer: MediaPlayer? = null
    private val vibrator: Vibrator by lazy {
        this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    private var sensorManager: SensorManager? = null
    private var _soundStatus: String? = null
    private var _vibrateStatus: String? = null
    private var _warningIndicator: String? = null
    private var magnitude: Double? = null
    private var uri_ringtone: String? = null
    private var magneticFieldSensor: Sensor? = null
    private lateinit var audioManager: AudioManager
    lateinit var shareData: SharedPreferenceUtils
    lateinit var binding: ActivityMetalDetectorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getCurrentTheme())
        showSystemUI(true)
        binding = ActivityMetalDetectorBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initData()
        initView()
        initAction()

    }

    private fun initView() {
        Log.d(TAG, "initView: " + Const.theme)
        if (Const.theme != "Theme1" && Const.theme != "Theme2" && Const.theme != "Theme3" && Const.theme != "Theme4" && Const.theme != "Theme5") {
            // Mặc định là Theme1
            Log.d(TAG, "initView: Default to Theme1")
            binding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
            binding.lnValue.orientation = LinearLayout.HORIZONTAL
            binding.lnValue.gravity = Gravity.BOTTOM
            applyGradientyes(binding.tvDetector)
            applyGradientno(binding.yn)
            binding.theme.visible()
            binding.diagram1.visible()
            binding.graph1.visible()
            settingGraph1()
        } else {
            when (Const.theme) {
                "Theme1" -> {
                    Log.d(TAG, "initView: 1")
                    binding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
                    binding.lnValue.orientation = LinearLayout.HORIZONTAL
                    binding.lnValue.gravity = Gravity.BOTTOM
                    applyGradientyes(binding.tvDetector)
                    applyGradientno(binding.yn)
                    binding.theme.visible()
                    binding.diagram1.visible()
                    binding.graph1.visible()
                    settingGraph1()
                }

                "Theme2" -> {
                    Log.d(TAG, "initView: 2")
                    binding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
                    binding.tvValue.applyGradient_2(this)
                    binding.tvT.applyGradient_2(this)
                    binding.lnValue.orientation = LinearLayout.VERTICAL
                    binding.lnValue.gravity = Gravity.CENTER
                    applyGradienttt2(binding.tvDetector)
                    applyGradientno2(binding.yn)
                    binding.theme2.visible()
                    binding.diagram1.visible()
                    binding.graph2.visible()
                    settingGraphcham()
                }

                "Theme3" -> {
                    binding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
                    binding.lnValue.orientation = LinearLayout.HORIZONTAL
                    binding.lnValue.gravity = Gravity.BOTTOM
                    applyGradienttt3(binding.tvDetector)
                    applyGradientno3(binding.yn)
                    binding.theme3.visible()
                    binding.diagram1.visible()
                    binding.graph1.visible()
                    settingGraph3()
                }

                "Theme4" -> {
                    binding.tvValue.setTypeface(null, Typeface.ITALIC)
                    binding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
                    binding.lnValue.orientation = LinearLayout.VERTICAL
                    binding.lnValue.gravity = Gravity.CENTER
                    binding.tvT.setTextColor(getColor(R.color.theme4_text_color))
                    binding.tvT.setTypeface(null, Typeface.ITALIC)
                    binding.tvDetector.setTextColor(Color.parseColor("#381E05"))
                    applyGradientno4(binding.yn)
                    binding.theme4.visible()
                    binding.diagram2.visible()
                    binding.graph.visible()
                    settingGraph4()
                }

                "Theme5" -> {
                    binding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50f)
                    binding.lnValue.orientation = LinearLayout.VERTICAL
                    binding.lnValue.gravity = Gravity.CENTER
                    binding.tvT.setTextColor(getColor(R.color.theme4_text_color))
                    binding.tvT.setTypeface(null, Typeface.ITALIC)
                    applyGradienttt5(binding.tvDetector)
                    applyGradientno(binding.yn)
                    binding.theme5.visible()
                    binding.diagram2.visible()
                    binding.graph.visible()
                    settingGraph5()
                }

                else -> {
                    Log.d(TAG, "initView: Default to Theme1")
                    binding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
                    binding.lnValue.orientation = LinearLayout.HORIZONTAL
                    binding.lnValue.gravity = Gravity.BOTTOM
                    applyGradientyes(binding.tvDetector)
                    applyGradientno(binding.yn)
                    binding.theme.visible()
                    binding.diagram1.visible()
                    binding.graph1.visible()
                    settingGraph1()
                }
            }
        }

    }

    private fun initData() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        flashlightManager = FlashlightManager(this)
        shareData = SharedPreferenceUtils.getInstance(this)
        binding.tvDetector.text = getString(R.string.metal_detector)
        URI_MUSIC = shareData.getStringValue(KEY_URI_MUSIC).toString()
        TRIGGER_VALUE = shareData.getStringTriggerValue(TRIGGER_VALUE_KEY).toString()

    }

    private fun initAction() {
        binding.lnNote.onSingleClick {
            showDialogNote()
        }
        binding.lnST2.onSingleClick {
            st()
        }
        binding.imvBack.onSingleClick {
            startActivity(Intent(this@MetalDetectorActivity, HomeActivity::class.java))
        }
        binding.imvSetting.onSingleClick {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        binding.imvFlashOn.onSingleClick {
            binding.imvFlashOff.visibility = View.VISIBLE
            binding.imvFlashOn.visibility = View.GONE
            _checkFlash = false
            shareData.putBooleanMute("flass", false)
        }

        binding.imvFlashOff.onSingleClick {
            binding.imvFlashOff.visibility = View.GONE
            binding.imvFlashOn.visibility = View.VISIBLE
            _checkFlash = true
            shareData.putBooleanMute("flass", true)
        }
        //////////
        binding.imvFlashOn2.onSingleClick {
            binding.imvFlashOff2.visibility = View.VISIBLE
            binding.imvFlashOn2.visibility = View.GONE
            _checkFlash = false
            shareData.putBooleanMute("flass", false)

        }

        binding.imvFlashOff2.onSingleClick {
            binding.imvFlashOff2.visibility = View.GONE
            binding.imvFlashOn2.visibility = View.VISIBLE
            _checkFlash = true
            shareData.putBooleanMute("flass", true)
        }

        //////////////
        binding.imvMusicOn.onSingleClick {
            binding.imvMusicOff.visibility = View.VISIBLE
            binding.imvMusicOn.visibility = View.GONE
            _checkSound = false
            shareData.putBooleanMute("mute", false)
        }

        binding.imvMusicOff.onSingleClick {
            binding.imvMusicOff.visibility = View.GONE
            binding.imvMusicOn.visibility = View.VISIBLE
            _checkSound = true
            shareData.putBooleanMute("mute", true)
        }
        binding.imvMusicOn2.onSingleClick {
            binding.imvMusicOff2.visibility = View.VISIBLE
            binding.imvMusicOn2.visibility = View.GONE
            _checkSound = false
            shareData.putBooleanMute("mute", false)

        }

        binding.imvMusicOff2.onSingleClick {
            binding.imvMusicOff2.visibility = View.GONE
            binding.imvMusicOn2.visibility = View.VISIBLE
            _checkSound = true
            shareData.putBooleanMute("mute", true)
        }
////////////////////
        binding.imvVibrateOn.onSingleClick {
            binding.imvVibrateOff.visibility = View.VISIBLE
            binding.imvVibrateOn.visibility = View.GONE
            _checkVibrate = false
            shareData.putBooleanvibration("vibration", false)
        }

        binding.imvVibrateOff.onSingleClick {
            binding.imvVibrateOff.visibility = View.GONE
            binding.imvVibrateOn.visibility = View.VISIBLE
            _checkVibrate = true
            shareData.putBooleanvibration("vibration", true)
        }
        binding.imvVibrateOn2.onSingleClick {
            binding.imvVibrateOff2.visibility = View.VISIBLE
            binding.imvVibrateOn2.visibility = View.GONE
            _checkVibrate = false
            shareData.putBooleanvibration("vibration", false)
        }

        binding.imvVibrateOff2.onSingleClick {
            binding.imvVibrateOff2.visibility = View.GONE
            binding.imvVibrateOn2.visibility = View.VISIBLE
            shareData.putBooleanvibration("vibration", true)
            _checkVibrate = true
        }
    }

    private fun showDialogNote() {
        val dialogBinding = CustomDialogNoteBinding.inflate(LayoutInflater.from(this))
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(getDrawable(R.color.transparent))
        dialog.setCancelable(false)
        dialogBinding.imvDelete.onSingleClick {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getCurrentTheme(): Int {
        when (Const.themeIndex) {
            0 -> Const.theme = "Theme1"
            1 -> Const.theme = "Theme2"
            2 -> Const.theme = "Theme3"
            3 -> Const.theme = "Theme4"
            4 -> Const.theme = "Theme5"
        }

        return when (Const.theme) {
            "Theme1" -> R.style.Theme1
            "Theme2" -> R.style.Theme2
            "Theme3" -> R.style.Theme3
            "Theme4" -> R.style.Theme4
            "Theme5" -> R.style.Theme5
            else -> R.style.Theme1
        }
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type === Sensor.TYPE_MAGNETIC_FIELD) {
            // get values for each axes X,Y,Z
            val magX = event!!.values[0]
            val magY = event.values[1]
            val magZ = event.values[2]
            magnitude = Math.sqrt((magX * magX + magY * magY + magZ * magZ).toDouble())

            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val mediumVolume = (0.6 * maxVolume).toInt()
            val smallVolume = (0.2 * maxVolume).toInt()
            if (magnitude!! >= _warningIndicator!!.toDouble()) {
                binding.yn.setTextRes(R.string.metal_detector)
                when (Const.themeIndex) {
                    0 -> applyGradientyes(binding.yn)
                    1 -> applyGradientyes2(binding.yn)
                    2 -> applyGradientyes3(binding.yn)
                    3 -> applyGradientyes2(binding.yn)
                    4 -> binding.yn.setTextColor(Color.WHITE)
                }
            } else {
                binding.yn.setTextRes(R.string.metal_cannot_detected)
                when (Const.themeIndex) {
                    0 -> applyGradientno(binding.yn)
                    1 -> applyGradientno3(binding.yn)
                    2 -> applyGradientno2(binding.yn)
                    3 -> applyGradientno2(binding.yn)
                    4 -> binding.yn.setTextColor(Color.parseColor("#02F2BA"))
                }
            }
            if (magnitude!! >= _warningIndicator!!.toDouble()) {
                if (_checkSound) {
                    when {
                        magnitude!!.toInt() >= _warningIndicator!!.toInt() && magnitude!!.toInt() <= 110 -> {
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, smallVolume, 0)
                        }

                        magnitude!!.toInt() > 110 && magnitude!!.toInt() <= 300 -> {
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mediumVolume, 0)
                        }

                        magnitude!!.toInt() > 300 -> {
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
                        }
                    }
                    startSound()
                    Log.d("sound", "Sound")
                } else {
                    stopSound()
                }
                if (_checkVibrate!!) {
                    startVibration()
                } else {
                    stopVibration()
                }

                if (_checkFlash) {
                    flashlightManager.turnOnFlashlight()
                } else {
                    flashlightManager.turnOffFlashlight()
                }
            } else if (magnitude!! < _warningIndicator!!.toDouble()) {
                stopSound()
                stopVibration()
                flashlightManager.stopFlashPattern()
            }
            when (Const.themeIndex) {
                0 -> {
                    updateGraph1(magX, magY, magZ)
                    binding.tvValue.setText(magnitude?.toInt().toString() + " ")
                    binding.customArcProgress.setMaxProgress(1000F)
                    binding.customArcProgress.setProgress((magnitude!!).toFloat())
                    Log.d(TAG, "onSensorChanged: " + magnitude)
                }

                1 -> {
                    updateGraph2(magX, magY, magZ)
                    binding.tvut2.setText(magnitude?.toInt().toString() + " ")
                    binding.customArcProgress2.setProgress((magnitude!!).toFloat())
                    Log.d(TAG, "onSensorChanged: " + magnitude)
                    binding.kim2.setProgress(magnitude!!.toFloat())
                }

                2 -> {
                    updateGraph3(magX, magY, magZ)
                    binding.tvut3.setText(magnitude?.toInt().toString() + " ")
                    binding.customArcProgress3.setProgress((magnitude!!).toFloat())
                    Log.d(TAG, "onSensorChanged: " + magnitude)
                    binding.kim3.setProgress(magnitude!!.toFloat())
                }

                3 -> {
                    updateGraph4(magX, magY, magZ)
                    binding.tvut4.setText(magnitude?.toInt().toString() + " ")
                    binding.customArcProgress4.setMaxProgress(1000F)
                    binding.customArcProgress4.setProgress((magnitude!!).toFloat())
                    Log.d(TAG, "onSensorChanged: " + magnitude)
                    binding.kim4.setProgress(magnitude!!.toFloat());

                }

                4 -> {
                    updateGraph4(magX, magY, magZ)
                    binding.tvValue5.setText(magnitude?.toInt().toString() + " ")
                    binding.customArcProgress5.setMaxProgress(1000F)
                    binding.customArcProgress5.setProgress((magnitude!!).toFloat())
                    Log.d(TAG, "onSensorChanged: " + magnitude)
                }
            }


        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    private lateinit var seriesX: LineGraphSeries<DataPoint>
    private lateinit var seriesY: LineGraphSeries<DataPoint>
    private lateinit var seriesZ: LineGraphSeries<DataPoint>
    private val maxYDisplay = 100.0
    private val minYDisplay = -10.0
    fun settingGraph4() {
        seriesX = LineGraphSeries()
        seriesY = LineGraphSeries()
        seriesZ = LineGraphSeries()
        seriesX.appendData(DataPoint(0.0, 0.0), true, 500)
        seriesY.appendData(DataPoint(0.0, 0.0), true, 500)
        seriesZ.appendData(DataPoint(0.0, 0.0), true, 500)
        seriesX.color = ContextCompat.getColor(this, R.color.colorx4)
        seriesY.color = ContextCompat.getColor(this, R.color.colory4)
        seriesZ.color = ContextCompat.getColor(this, R.color.colorz4)

        binding.graph.addSeries(seriesX)
        binding.graph.addSeries(seriesY)
        binding.graph.addSeries(seriesZ)

        // Set manual X bounds
        binding.graph.viewport.isXAxisBoundsManual = true
        binding.graph.viewport.setMinX(0.0)
        binding.graph.viewport.setMaxX(500.0)

        // Set manual Y bounds
        binding.graph.viewport.isYAxisBoundsManual = true
        binding.graph.viewport.setMinY(minYDisplay)
        binding.graph.viewport.setMaxY(maxYDisplay)

        // Limit labels to 4
        binding.graph.gridLabelRenderer.numHorizontalLabels = 4
        binding.graph.gridLabelRenderer.numVerticalLabels = 4

        binding.graph.gridLabelRenderer.isHorizontalLabelsVisible = false
        binding.graph.setOnTouchListener { _, _ -> true }

        binding.graph.viewport.isScrollable = true
        binding.graph.viewport.isScalable = true
        binding.graph.gridLabelRenderer.horizontalLabelsColor = Color.parseColor("#959595")
        binding.graph.gridLabelRenderer.verticalLabelsColor = Color.parseColor("#959595")
        binding.graph.gridLabelRenderer.gridColor = Color.parseColor("#959595")
    }

    fun settingGraph5() {
        seriesX = LineGraphSeries()
        seriesY = LineGraphSeries()
        seriesZ = LineGraphSeries()
        seriesX.appendData(DataPoint(0.0, 0.0), true, 500)
        seriesY.appendData(DataPoint(0.0, 0.0), true, 500)
        seriesZ.appendData(DataPoint(0.0, 0.0), true, 500)
        seriesX.color = ContextCompat.getColor(this, R.color.colorx5)
        seriesY.color = ContextCompat.getColor(this, R.color.colory5)
        seriesZ.color = ContextCompat.getColor(this, R.color.colorz5)

        binding.graph.addSeries(seriesX)
        binding.graph.addSeries(seriesY)
        binding.graph.addSeries(seriesZ)

        // Set manual X bounds
        binding.graph.viewport.isXAxisBoundsManual = true
        binding.graph.viewport.setMinX(0.0)
        binding.graph.viewport.setMaxX(500.0)

        // Set manual Y bounds
        binding.graph.viewport.isYAxisBoundsManual = true
        binding.graph.viewport.setMinY(minYDisplay)
        binding.graph.viewport.setMaxY(maxYDisplay)

        // Limit labels to 4
        binding.graph.gridLabelRenderer.numHorizontalLabels = 4
        binding.graph.gridLabelRenderer.numVerticalLabels = 4

        binding.graph.gridLabelRenderer.isHorizontalLabelsVisible = false
        binding.graph.setOnTouchListener { _, _ -> true }

        binding.graph.viewport.isScrollable = true
        binding.graph.viewport.isScalable = true
        binding.graph.gridLabelRenderer.horizontalLabelsColor = Color.parseColor("#959595")
        binding.graph.gridLabelRenderer.verticalLabelsColor = Color.parseColor("#959595")
        binding.graph.gridLabelRenderer.gridColor = Color.parseColor("#959595")
    }
    private fun updateGraph4(magX: Float, magY: Float, magZ: Float) {
        Log.d(TAG, "updateGraph4: " + magX)
        val x = seriesX.highestValueX + 1
        seriesX.appendData(DataPoint(x, magX.toDouble()), true, 500)
        seriesY.appendData(DataPoint(x, magY.toDouble()), true, 500)
        seriesZ.appendData(DataPoint(x, magZ.toDouble()), true, 500)

        val maxY = maxOf(magX.toDouble(), magY.toDouble(), magZ.toDouble())
        val minY = minOf(magX.toDouble(), magY.toDouble(), magZ.toDouble())

        if (maxY > binding.graph.viewport.getMaxY(false)) {
            binding.graph.viewport.setMaxY(maxY + 50.0) // Add buffer
        }
        if (minY < binding.graph.viewport.getMinY(false)) {
            binding.graph.viewport.setMinY(minY - 50.0) // Add buffer
        }
        binding.graph.onDataChanged(false, false)
    }

    fun settingGraph1() {
        seriesX = LineGraphSeries()
        seriesY = LineGraphSeries()
        seriesZ = LineGraphSeries()

        seriesX.color = ContextCompat.getColor(this, R.color.colorx1)
        seriesY.color = ContextCompat.getColor(this, R.color.colory1)
        seriesZ.color = ContextCompat.getColor(this, R.color.colorz1)

        binding.graph1.addSeries(seriesX)
        binding.graph1.addSeries(seriesY)
        binding.graph1.addSeries(seriesZ)

        // set manual X bounds
        binding.graph1.viewport.isXAxisBoundsManual = true
        binding.graph1.viewport.setMinX(0.0)
        binding.graph1.viewport.setMaxX(500.0)

        // set manual Y bounds
        binding.graph1.viewport.isYAxisBoundsManual = true
        binding.graph1.viewport.setMinY(minYDisplay)
        binding.graph1.viewport.setMaxY(maxYDisplay)
        binding.graph1.gridLabelRenderer.isHorizontalLabelsVisible = false
        binding.graph1.setOnTouchListener { _, _ -> true }

        binding.graph1.viewport.isScrollable = true
        binding.graph1.viewport.isScalable = true

        binding.graph1.gridLabelRenderer.numVerticalLabels = 4
        binding.graph1.gridLabelRenderer.textSize = 36f
        binding.graph1.gridLabelRenderer.horizontalLabelsColor = Color.parseColor("#959595")
        binding.graph1.gridLabelRenderer.verticalLabelsColor = Color.parseColor("#FFFFFF")
        binding.graph1.gridLabelRenderer.gridColor = Color.parseColor("#959595")
    }

    private fun updateGraph1(magX: Float, magY: Float, magZ: Float) {
        val x = seriesX.highestValueX + 1
        seriesX.appendData(DataPoint(x, magX.toDouble()), true, 500)
        seriesY.appendData(DataPoint(x, magY.toDouble()), true, 500)
        seriesZ.appendData(DataPoint(x, magZ.toDouble()), true, 500)

        val maxY = maxOf(magX.toDouble(), magY.toDouble(), magZ.toDouble())
        val minY = minOf(magX.toDouble(), magY.toDouble(), magZ.toDouble())

        if (maxY > binding.graph1.viewport.getMaxY(false)) {
            binding.graph1.viewport.setMaxY(maxY + 50.0) // Add buffer
        }
        if (minY < binding.graph1.viewport.getMinY(false)) {
            binding.graph1.viewport.setMinY(minY - 50.0) // Add buffer
        }
        binding.graph1.onDataChanged(false, false)
    }
    fun settingGraphcham() {
        seriesX = LineGraphSeries()
        seriesY = LineGraphSeries()
        seriesZ = LineGraphSeries()

        seriesX.color = ContextCompat.getColor(this, R.color.colory4)
        seriesY.color = ContextCompat.getColor(this, R.color.color_app)
        seriesZ.color = ContextCompat.getColor(this, R.color.colorz2)

        binding.graph2.addSeries(seriesX)
        binding.graph2.addSeries(seriesY)
        binding.graph2.addSeries(seriesZ)

        // Set manual X bounds
        binding.graph2.viewport.isXAxisBoundsManual = true
        binding.graph2.viewport.setMinX(-100.0)
        binding.graph2.viewport.setMaxX(500.0)

        // Set manual Y bounds
        binding.graph2.viewport.isYAxisBoundsManual = true
        binding.graph2.viewport.setMinY(minYDisplay)
        binding.graph2.viewport.setMaxY(maxYDisplay)

        // Hide all grid lines and labels initially
        binding.graph2.gridLabelRenderer.apply {
            isHorizontalLabelsVisible = false
            isVerticalLabelsVisible = true  // Show vertical labels (Y-axis)
            gridStyle = GridLabelRenderer.GridStyle.BOTH  // Show both gridlines (horizontal and vertical)
            numVerticalLabels = 4  // Show 4 labels on the Y-axis
            horizontalLabelsColor = Color.parseColor("#959595")
            verticalLabelsColor = Color.parseColor("#FFFFFF")
            gridColor = Color.parseColor("#959595")
        }
        binding.graph2.gridLabelRenderer.numHorizontalLabels = 5  // You can adjust the number as needed

        binding.graph2.setOnTouchListener { _, _ -> true }
        binding.graph2.viewport.isScrollable = true
        binding.graph2.viewport.isScalable = true
    }

    private var dataCounter = 0

    private var prevX = 0.0
    private var prevY = 0.0
    private var prevZ = 0.0
    private val threshold = 2.0

    private fun updateGraph2(magX: Float, magY: Float, magZ: Float) {
        val newX = magX.coerceIn(0f, 100f).toDouble()
        val newY = magY.coerceIn(0f, 100f).toDouble()
        val newZ = magZ.coerceIn(-100f, 100f).toDouble()

        val x = seriesX.highestValueX + 1

        // Update lines
        if (abs(newX - prevX) >= threshold) {
            seriesX.appendData(DataPoint(x, newX), true, 500)
            prevX = newX
            // Add point for significant changes
            if (!::seriesXPoints.isInitialized) {
                seriesXPoints = PointsGraphSeries()
                seriesXPoints.shape = PointsGraphSeries.Shape.POINT
                seriesXPoints.color = ContextCompat.getColor(this, R.color.colory4)
                seriesXPoints.size = 5f
                binding.graph2.addSeries(seriesXPoints)
            }
            seriesXPoints.appendData(DataPoint(x, newX), true, 500)
        } else {
            seriesX.appendData(DataPoint(x, prevX), true, 500)
        }

        if (abs(newY - prevY) >= threshold) {
            seriesY.appendData(DataPoint(x, newY), true, 500)
            prevY = newY
            // Add point for significant changes
            if (!::seriesYPoints.isInitialized) {
                seriesYPoints = PointsGraphSeries()
                seriesYPoints.shape = PointsGraphSeries.Shape.POINT
                seriesYPoints.color = ContextCompat.getColor(this, R.color.color_app)
                seriesYPoints.size = 5f
                binding.graph2.addSeries(seriesYPoints)
            }
            seriesYPoints.appendData(DataPoint(x, newY), true, 500)
        } else {
            seriesY.appendData(DataPoint(x, prevY), true, 500)
        }

        if (abs(newZ - prevZ) >= threshold) {
            seriesZ.appendData(DataPoint(x, newZ), true, 500)
            prevZ = newZ
            // Add point for significant changes
            if (!::seriesZPoints.isInitialized) {
                seriesZPoints = PointsGraphSeries()
                seriesZPoints.shape = PointsGraphSeries.Shape.POINT
                seriesZPoints.color = ContextCompat.getColor(this, R.color.colorz2)
                seriesZPoints.size = 5f
                binding.graph2.addSeries(seriesZPoints)
            }
            seriesZPoints.appendData(DataPoint(x, newZ), true, 500)
        } else {
            seriesZ.appendData(DataPoint(x, prevZ), true, 500)
        }

        binding.graph2.viewport.setMinY(-100.0)
        binding.graph2.viewport.setMaxY(100.0)
        binding.graph2.onDataChanged(false, false)
    }
    fun settingGraph3() {
        seriesX = LineGraphSeries()
        seriesY = LineGraphSeries()
        seriesZ = LineGraphSeries()

        // Thêm các thuộc tính để làm mượt đường
        seriesX.apply {
            color = ContextCompat.getColor(this@MetalDetectorActivity, R.color.colory4)
            isDrawDataPoints = false
            thickness = 5
        }
        seriesY.apply {
            color = ContextCompat.getColor(this@MetalDetectorActivity, R.color.color_app)
            isDrawDataPoints = false
            thickness = 5
        }
        seriesZ.apply {
            color = ContextCompat.getColor(this@MetalDetectorActivity, R.color.colorz2)
            isDrawDataPoints = false
            thickness = 5
        }

        binding.graph1.apply {
            addSeries(seriesX)
            addSeries(seriesY)
            addSeries(seriesZ)

            viewport.apply {
                isXAxisBoundsManual = true
                setMinX(-50.0)
                setMaxX(500.0)
                isYAxisBoundsManual = true
                setMinY(minYDisplay)
                setMaxY(maxYDisplay)
                isScrollable = true
                isScalable = true
                setScrollableY(false)
            }

            gridLabelRenderer.apply {
                isHorizontalLabelsVisible = false
                numVerticalLabels = 4
                textSize = 36f
                horizontalLabelsColor = Color.parseColor("#959595")
                verticalLabelsColor = Color.parseColor("#FFFFFF")
                gridColor = Color.parseColor("#959595")
            }

            setOnTouchListener { _, _ -> true }
        }
    }

    private var lastX = 0.0
    private var offsetX = 0.0
    private var offsetY = 0.0
    private var offsetZ = 0.0

    private fun updateGraph3(magX: Float, magY: Float, magZ: Float) {
        // Tạo hiệu ứng sóng sin cho mỗi giá trị
        val x = seriesX.highestValueX + 1

        // Thêm thành phần sin để tạo đường cong mượt
        val frequency = 0.1
        val amplitude = 20.0

        val smoothX = magX + amplitude * sin((x + offsetX) * frequency)
        val smoothY = magY + amplitude * sin((x + offsetY) * frequency + PI / 3)
        val smoothZ = magZ + amplitude * sin((x + offsetZ) * frequency + 2 * PI / 3)

        // Cập nhật offset cho lần sau
        offsetX += 0.1
        offsetY += 0.1
        offsetZ += 0.1

        // Thêm điểm dữ liệu mới
        seriesX.appendData(DataPoint(x, smoothX), true, 500)
        seriesY.appendData(DataPoint(x, smoothY), true, 500)
        seriesZ.appendData(DataPoint(x, smoothZ), true, 500)

        // Tự động điều chỉnh viewport
        val maxY = maxOf(smoothX, smoothY, smoothZ)
        val minY = minOf(smoothX, smoothY, smoothZ)

        if (maxY > binding.graph1.viewport.getMaxY(false)) {
            binding.graph1.viewport.setMaxY(maxY + 50.0)
        }
        if (minY < binding.graph1.viewport.getMinY(false)) {
            binding.graph1.viewport.setMinY(minY - 50.0)
        }

        binding.graph1.onDataChanged(false, false)
    }

    // Khai báo các biến cho series điểm chấm ở cấp lớp
    lateinit var seriesXPoints: PointsGraphSeries<DataPoint>
    lateinit var seriesYPoints: PointsGraphSeries<DataPoint>
    lateinit var seriesZPoints: PointsGraphSeries<DataPoint>
    fun startSound() {
        if (URI_MUSIC.equals("")) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.sound_alarm)
            }
        } else {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, Uri.parse(URI_MUSIC))
            }
        }
        mediaPlayer?.start()
    }

    fun stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
            mediaPlayer?.prepare()
        }
    }

    private fun st() {
        isClick = true
        intent = Intent(this@MetalDetectorActivity, SelectThemeActivity::class.java)
        startActivity(intent)
        handel.postDelayed({ isClick = true }, 500)
    }

    override fun onResume() {
        super.onResume()
//        setTheme(getCurrentTheme())
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        _warningIndicator = TRIGGER_VALUE
        val (isMute, isFlass, isVibration) = getSettings()
        if (isMute) {
            _checkSound = true
            binding.imvMusicOn.visible()
            binding.imvMusicOff.gone()
            binding.imvMusicOn2.visible()
            binding.imvMusicOff2.gone()
        } else {
            _checkSound = false
            binding.imvMusicOn.gone()
            binding.imvMusicOff.visible()
            binding.imvMusicOn2.gone()
            binding.imvMusicOff2.visible()
        }
        if (isFlass) {
            _checkFlash = true
            binding.imvFlashOn.visible()
            binding.imvFlashOff.gone()
            binding.imvFlashOn2.visible()
            binding.imvFlashOff2.gone()
        } else {
            _checkFlash = false
            binding.imvFlashOn.gone()
            binding.imvFlashOff.visible()
            binding.imvFlashOn2.gone()
            binding.imvFlashOff2.visible()
        }

        if (isVibration) {
            _checkVibrate = true
            binding.imvVibrateOn.visible()
            binding.imvVibrateOff.gone()
            binding.imvVibrateOn2.visible()
            binding.imvVibrateOff2.gone()

        } else {
            _checkVibrate = false
            binding.imvVibrateOn.gone()
            binding.imvVibrateOff.visible()
            binding.imvVibrateOn2.gone()
            binding.imvVibrateOff2.visible()
        }
//        initView()
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
        stopSound()
        stopVibration()
        flashlightManager.stopFlashPattern()
        mainHandler!!.removeCallbacksAndMessages(null);
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSound()
        stopVibration()
        flashlightManager.stopFlashPattern()
    }

    private fun applyGradientyes(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#D8F6FD"),
                            Color.parseColor("#23E6ED")
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

    private fun applyGradientno(textView: TextView) {

        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#F52E13"),
                            Color.parseColor("#DBCECE")
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
    private fun applyGradienttt2(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#F0D672"), // Màu đầu tiên (trên cùng)
                            Color.parseColor("#C83871")  // Màu thứ hai (dưới cùng)
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

    private fun applyGradientyes2(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#03511D"),
                            Color.parseColor("#03511D")

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

    private fun applyGradientno2(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#690243"),
                            Color.parseColor("#690243")
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
    private fun applyGradienttt3(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#FFF9B8"),
                            Color.parseColor("#FFE205")
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

    private fun applyGradientyes3(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#B8FFA6"),
                            Color.parseColor("#5ECA0C")
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

    private fun applyGradientno3(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#D68080"),
                            Color.parseColor("#F80404")
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

    private fun applyGradientno4(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#7C1300"),
                            Color.parseColor("#7C1300")
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
    private fun applyGradienttt5(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#A9581E"),
                            Color.parseColor("#2C1404")
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
    fun getSettings(): Triple<Boolean, Boolean, Boolean> {
        val isMute = shareData.getBooleanMute("mute", true) // Mặc định là false
        val isFlass = shareData.getBooleanflass("flass", true) // Mặc định là true
        val isVibration = shareData.getBooleanvibration("vibration", true) // Mặc định là true
        return Triple(isMute, isFlass, isVibration)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@MetalDetectorActivity, HomeActivity::class.java))
    }
}