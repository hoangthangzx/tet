package com.metalsensor.gold.detector.finder.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
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
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.SharedPreferenceUtils
import com.metalsensor.gold.detector.finder.databinding.ActivityGoldDetecterBinding
import com.metalsensor.gold.detector.finder.databinding.CustomDialogNoteBinding
import com.metalsensor.gold.detector.finder.manager.FlashlightManager
import com.metalsensor.gold.detector.finder.utils.Const
import com.metalsensor.gold.detector.finder.utils.Const.KEY_URI_MUSIC_GOLD
import com.metalsensor.gold.detector.finder.utils.Const.TRIGGER_VALUE
import com.metalsensor.gold.detector.finder.utils.Const.TRIGGER_VALUE_GOLD_KEY
import com.metalsensor.gold.detector.finder.utils.Const.URI_MUSIC
import com.metalsensor.gold.detector.finder.utils.Const._checkFlashGold
import com.metalsensor.gold.detector.finder.utils.Const._checkSoundGold
import com.metalsensor.gold.detector.finder.utils.Const._checkVibrateGold
import com.metalsensor.gold.detector.finder.utils.Const.themeIndex2
import com.metalsensor.gold.detector.finder.utils.applyGradient_2
import com.metalsensor.gold.detector.finder.utils.gone
import com.metalsensor.gold.detector.finder.utils.onSingleClick
import com.metalsensor.gold.detector.finder.utils.setTextRes
import com.metalsensor.gold.detector.finder.utils.showSystemUI
import com.metalsensor.gold.detector.finder.utils.startVibration
import com.metalsensor.gold.detector.finder.utils.stopVibration
import com.metalsensor.gold.detector.finder.utils.visible
import kotlin.math.PI
import kotlin.math.sin

class GoldDetecterActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var flashlightManager: FlashlightManager
    var mainHandler = Handler(Looper.getMainLooper())
    private var TAG = "AAA"
    private val handel by lazy {
        Handler(Looper.getMainLooper())
    }
    private var isClick = true

    private var mediaPlayer: MediaPlayer? = null
    private var sensorManager: SensorManager? = null
    private var _warningIndicator: String? = null
    private var magnitude: Double? = null
    private lateinit var audioManager: AudioManager
    lateinit var shareData: SharedPreferenceUtils
    lateinit var binding: ActivityGoldDetecterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getCurrentTheme())
        showSystemUI(true)
        binding = ActivityGoldDetecterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initData()
        initView()
        initAction()
    }

    private fun initView() {
        if (themeIndex2 == 0 || themeIndex2 == 1 || themeIndex2 == 2 || themeIndex2 == 3 || themeIndex2 == 4) {

            Log.d(TAG, "initView: 1")
            binding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
            binding.lnValue.orientation = LinearLayout.HORIZONTAL
            binding.lnValue.gravity = Gravity.BOTTOM
            applyGradienttt(binding.tvDetector)
            applyGradientno(binding.yn)
            binding.theme.visible()
            binding.diagram1.visible()
            binding.graph1.visible()
            settingGraph1()
        } else {
            when (Const.theme2) {

                "Theme6" -> {
                    Log.d(TAG, "initView: 1")
                    binding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
                    binding.lnValue.orientation = LinearLayout.HORIZONTAL
                    binding.lnValue.gravity = Gravity.BOTTOM
                    applyGradienttt(binding.tvDetector)
                    applyGradientno(binding.yn)
                    binding.theme.visible()
                    binding.diagram1.visible()
                    binding.graph1.visible()
                    settingGraph1()
                }

                "Theme7" -> {
                    Log.d(TAG, "initView: 2")
                    binding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
                    binding.tvValue.applyGradient_2(this)
                    binding.tvT.applyGradient_2(this)
                    binding.lnValue.orientation = LinearLayout.VERTICAL
                    binding.lnValue.gravity = Gravity.CENTER
                    applyGradientno(binding.yn)
                    binding.tvDetector.setTextColor(Color.parseColor("#030507"))
                    binding.theme2.visible()
                    binding.diagram1.visible()
                    binding.graph2.visible()
                    settingGraphcham()
                }

                "Theme8" -> {
                    binding.tvDetector.setTextColor(Color.parseColor("#FFDFB1"))
                    binding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
                    binding.lnValue.orientation = LinearLayout.HORIZONTAL
                    binding.lnValue.gravity = Gravity.BOTTOM
                    applyGradientno(binding.yn)
                    binding.theme3.visible()
                    binding.diagram1.visible()
                    binding.graph1.visible()
                    settingGraph3()
                }

                "Theme9" -> {
                    applyGradienttt9(binding.tvDetector)
                    binding.tvValue.setTypeface(null, Typeface.ITALIC)
                    binding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
                    binding.lnValue.orientation = LinearLayout.VERTICAL
                    binding.lnValue.gravity = Gravity.CENTER
                    binding.tvT.setTextColor(getColor(R.color.theme4_text_color))
                    binding.tvT.setTypeface(null, Typeface.ITALIC)
                    applyGradientno(binding.yn)
                    binding.theme4.visible()
                    binding.diagram2.visible()
                    binding.graph.visible()
                    settingGraph()
                }

                "Theme10" -> {
                    binding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50f)
                    binding.lnValue.orientation = LinearLayout.VERTICAL
                    binding.lnValue.gravity = Gravity.CENTER
                    binding.tvT.setTextColor(getColor(R.color.theme4_text_color))
                    binding.tvT.setTypeface(null, Typeface.ITALIC)
                    binding.tvDetector.setTextColor(Color.parseColor("#61330A"))
                    applyGradientno(binding.yn)
                    binding.theme5.visible()
                    binding.diagram2.visible()
                    binding.graph.visible()
                    settingGraph()
                }

                else -> {
                    binding.tvT.setTypeface(null, Typeface.ITALIC)
                    binding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
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
            }
        }
    }

    private fun initData() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        flashlightManager = FlashlightManager(this)
        shareData = SharedPreferenceUtils.getInstance(this)
        binding.tvDetector.text = getString(R.string.gold_detector)
        URI_MUSIC = shareData.getStringValue(KEY_URI_MUSIC_GOLD).toString()
        TRIGGER_VALUE = shareData.getStringTriggerValue(TRIGGER_VALUE_GOLD_KEY).toString()
    }

    private fun initAction() {
        binding.lnNote.onSingleClick {
            showDialogNote()
        }
        binding.lnST2.onSingleClick {
            st()
        }
        binding.imvBack.onSingleClick {
            startActivity(Intent(this@GoldDetecterActivity, HomeActivity::class.java))
        }
        binding.imvSetting.onSingleClick {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        binding.imvFlashOn.onSingleClick {
            binding.imvFlashOff.visibility = View.VISIBLE
            binding.imvFlashOn.visibility = View.GONE
            _checkFlashGold = false
            shareData.putBooleanMute("flassGOLD", false)
        }

        binding.imvFlashOff.onSingleClick {
            binding.imvFlashOff.visibility = View.GONE
            binding.imvFlashOn.visibility = View.VISIBLE
            _checkFlashGold = true
            shareData.putBooleanMute("flassGOLD", true)
        }
        //////////
        binding.imvFlashOn2.onSingleClick {
            binding.imvFlashOff2.visibility = View.VISIBLE
            binding.imvFlashOn2.visibility = View.GONE
            _checkFlashGold = false
            shareData.putBooleanMute("flassGOLD", false)

        }

        binding.imvFlashOff2.onSingleClick {
            binding.imvFlashOff2.visibility = View.GONE
            binding.imvFlashOn2.visibility = View.VISIBLE
            _checkFlashGold = true
            shareData.putBooleanMute("flassGOLD", true)
        }

        //////////////
        binding.imvMusicOn.onSingleClick {
            binding.imvMusicOff.visibility = View.VISIBLE
            binding.imvMusicOn.visibility = View.GONE
            _checkSoundGold = false
            shareData.putBooleanMute("muteGOLD", false)
        }

        binding.imvMusicOff.onSingleClick {
            binding.imvMusicOff.visibility = View.GONE
            binding.imvMusicOn.visibility = View.VISIBLE
            _checkSoundGold = true
            shareData.putBooleanMute("muteGOLD", true)
        }
        binding.imvMusicOn2.onSingleClick {
            binding.imvMusicOff2.visibility = View.VISIBLE
            binding.imvMusicOn2.visibility = View.GONE
            _checkSoundGold = false
            shareData.putBooleanMute("muteGOLD", false)

        }

        binding.imvMusicOff2.onSingleClick {
            binding.imvMusicOff2.visibility = View.GONE
            binding.imvMusicOn2.visibility = View.VISIBLE
            _checkSoundGold = true
            shareData.putBooleanMute("muteGOLD", true)
        }
////////////////////
        binding.imvVibrateOn.onSingleClick {
            binding.imvVibrateOff.visibility = View.VISIBLE
            binding.imvVibrateOn.visibility = View.GONE
            _checkVibrateGold = false
            shareData.putBooleanvibration("vibrationGOLD", false)
        }

        binding.imvVibrateOff.onSingleClick {
            binding.imvVibrateOff.visibility = View.GONE
            binding.imvVibrateOn.visibility = View.VISIBLE
            _checkVibrateGold = true
            shareData.putBooleanvibration("vibrationGOLD", true)
        }
        binding.imvVibrateOn2.onSingleClick {
            binding.imvVibrateOff2.visibility = View.VISIBLE
            binding.imvVibrateOn2.visibility = View.GONE
            _checkVibrateGold = false
            shareData.putBooleanvibration("vibrationGOLD", false)
        }

        binding.imvVibrateOff2.onSingleClick {
            binding.imvVibrateOff2.visibility = View.GONE
            binding.imvVibrateOn2.visibility = View.VISIBLE
            shareData.putBooleanvibration("vibrationGOLD", true)
            _checkVibrateGold = true
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
        when (themeIndex2) {
            5 -> Const.theme2 = "Theme6"
            6 -> Const.theme2 = "Theme7"
            7 -> Const.theme2 = "Theme8"
            8 -> Const.theme2 = "Theme9"
            9 -> Const.theme2 = "Theme10"
        }

        return when (Const.theme2) {
            "Theme6" -> R.style.Theme6
            "Theme7" -> R.style.Theme7
            "Theme8" -> R.style.Theme8
            "Theme9" -> R.style.Theme9
            "Theme10" -> R.style.Theme10
            else -> R.style.Theme6
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
                binding.yn.setTextRes(R.string.gold_detected)
                 when (Const.theme2) {
                    "Theme6" -> applyGradienttt(binding.yn)
                    "Theme7" -> applyGradientyes2(binding.yn)
                    "Theme8" -> applyGradientyes32(binding.yn)
                    "Theme9" -> applyGradientyes4(binding.yn)
                    "Theme10" -> applyGradientyes5(binding.yn)
                    else -> applyGradienttt(binding.yn)
                }

            } else {
                binding.yn.setTextRes(R.string.gold_cannot_detected)
                when (Const.theme2) {
                    "Theme6" -> applyGradientno(binding.yn)
                    "Theme7" -> applyGradientno2(binding.yn)
                    "Theme8" -> applyGradientno3(binding.yn)
                    "Theme9" -> applyGradientno4(binding.yn)
                    "Theme10" -> applyGradientno5(binding.yn)
                    else -> applyGradientno(binding.yn)
                }
            }
            if (magnitude!! >= _warningIndicator!!.toDouble()) {
                if (_checkSoundGold) {
                    when {
                        magnitude!!.toInt() >= _warningIndicator!!.toInt() && magnitude!!.toInt() <= 110 -> {
                            audioManager.setStreamVolume(
                                AudioManager.STREAM_MUSIC,
                                smallVolume,
                                0
                            )
                        }

                        magnitude!!.toInt() > 110 && magnitude!!.toInt() <= 300 -> {
                            audioManager.setStreamVolume(
                                AudioManager.STREAM_MUSIC,
                                mediumVolume,
                                0
                            )
                        }

                        magnitude!!.toInt() > 300 -> {
                            audioManager.setStreamVolume(
                                AudioManager.STREAM_MUSIC,
                                maxVolume,
                                0
                            )
                        }
                    }
                    startSound()
                    Log.d("sound", "Sound")
                } else {
                    stopSound()
                }
                if (_checkVibrateGold!!) {
                    startVibration()
                } else {
                    stopVibration()
                }

                if (_checkFlashGold!!) {
                    flashlightManager.turnOnFlashlight()
                } else {
                    flashlightManager.turnOffFlashlight()
                }
            } else if (magnitude!! < _warningIndicator!!.toDouble()) {
                stopSound()
                stopVibration()
                flashlightManager.stopFlashPattern()
            }

            Log.d(TAG, "onSensorChanged: " + Const.themeIndex2)
            when (Const.themeIndex2) {
                5 -> {
                    updateGraph1(magX, magY, magZ)
                    binding.tvValue.setText(magnitude?.toInt().toString() + " ")
                    binding.customArcProgress.setMaxProgress(1000F)
                    binding.customArcProgress.setProgress((magnitude!!).toFloat())
                    Log.d(TAG, "onSensorChanged: " + magnitude)
                }

                6 -> {
                    updateGraph2(magX, magY, magZ)
                    binding.tvut2.setText(magnitude?.toInt().toString() + " ")
                    binding.customArcProgress2.setProgress((magnitude!!).toFloat())
                    Log.d(TAG, "onSensorChanged: " + magnitude)
                    binding.kim7.setProgress(magnitude!!.toFloat())
                }

                7 -> {
                    updateGraph3(magX, magY, magZ)
                    binding.tvut3.setText(magnitude?.toInt().toString() + " ")
                    Log.d(TAG, "onSensorChanged: " + magnitude)
                    binding.kim8.setProgress(magnitude!!.toFloat())
                }

                8 -> {
                    updateGraph4(magX, magY, magZ)
                    binding.tvut4.setText(magnitude?.toInt().toString() + " ")
                    binding.customArcProgress4.setMaxProgress(1000F)
                    binding.customArcProgress4.setProgress((magnitude!!).toFloat())
                    Log.d(TAG, "onSensorChanged: " + magnitude)
                    binding.kim9.setProgress(magnitude!!.toFloat())

                }

                9 -> {
                    updateGraph5(magX, magY, magZ)
                    binding.tvut5.setText(magnitude?.toInt().toString() + " ")
                    binding.customArcProgress5.setProgress((magnitude!!).toFloat())
                    Log.d(TAG, "onSensorChanged: " + magnitude)
                    binding.kim10.setProgress(magnitude!!.toFloat())
                }

                else -> {
                    updateGraph1(magX, magY, magZ)
                    binding.tvValue.setText(magnitude?.toInt().toString() + " ")
                    binding.customArcProgress.setMaxProgress(1000F)
                    binding.customArcProgress.setProgress((magnitude!!).toFloat())
                    Log.d(TAG, "onSensorChanged: " + magnitude + " | Defaulted to 5")
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
    fun settingGraph() {
        seriesX = LineGraphSeries()
        seriesY = LineGraphSeries()
        seriesZ = LineGraphSeries()
        seriesX.appendData(DataPoint(0.0, 0.0), true, 500)
        seriesY.appendData(DataPoint(0.0, 0.0), true, 500)
        seriesZ.appendData(DataPoint(0.0, 0.0), true, 500)
        seriesX.color = ContextCompat.getColor(this, R.color.colory4)
        seriesY.color = ContextCompat.getColor(this, R.color.color_app)
        seriesZ.color = ContextCompat.getColor(this, R.color.colorz2)

        binding.graph.addSeries(seriesX)
        binding.graph.addSeries(seriesY)
        binding.graph.addSeries(seriesZ)

        // set manual X bounds
        binding.graph.viewport.isXAxisBoundsManual = true
        binding.graph.viewport.setMinX(0.0)
        binding.graph.viewport.setMaxX(500.0)

        // set manual Y bounds
        binding.graph.viewport.isYAxisBoundsManual = true
        binding.graph.viewport.setMinY(minYDisplay)
        binding.graph.viewport.setMaxY(maxYDisplay)

        // Ẩn nhãn ngang
        binding.graph.gridLabelRenderer.isHorizontalLabelsVisible = false

        // Hiển thị nhãn dọc với số lượng tối đa là 4
        binding.graph.gridLabelRenderer.isVerticalLabelsVisible = true
        binding.graph.gridLabelRenderer.numVerticalLabels = 4

        // Cho phép cuộn và thu phóng
        binding.graph.viewport.isScrollable = true
        binding.graph.viewport.isScalable = true

        // Tùy chỉnh màu sắc của nhãn và lưới
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
    private fun updateGraph5(magX: Float, magY: Float, magZ: Float) {
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

        // set manual X bounds
        binding.graph2.viewport.isXAxisBoundsManual = true
        binding.graph2.viewport.setMinX(0.0)
        binding.graph2.viewport.setMaxX(500.0)

        // set manual Y bounds
        binding.graph2.viewport.isYAxisBoundsManual = true
        binding.graph2.viewport.setMinY(minYDisplay)
        binding.graph2.viewport.setMaxY(maxYDisplay)
        binding.graph2.gridLabelRenderer.isHorizontalLabelsVisible = false
        binding.graph2.setOnTouchListener { _, _ -> true }

        binding.graph2.viewport.isScrollable = true
        binding.graph2.viewport.isScalable = true

        binding.graph2.gridLabelRenderer.numVerticalLabels = 4
        binding.graph2.gridLabelRenderer.textSize = 36f
        binding.graph2.gridLabelRenderer.horizontalLabelsColor = Color.parseColor("#959595")
        binding.graph2.gridLabelRenderer.verticalLabelsColor = Color.parseColor("#FFFFFF")
        binding.graph2.gridLabelRenderer.gridColor = Color.parseColor("#959595")
    }

    private var dataCounter = 0

    private fun updateGraph2(magX: Float, magY: Float, magZ: Float) {
        val x = seriesX.highestValueX + 1

        seriesX.appendData(DataPoint(x, magX.toDouble()), true, 500)
        seriesY.appendData(DataPoint(x, magY.toDouble()), true, 500)
        seriesZ.appendData(DataPoint(x, magZ.toDouble()), true, 500)

        dataCounter++
        if (dataCounter % 20 == 0) {
            if (!::seriesXPoints.isInitialized) {
                seriesXPoints = PointsGraphSeries()
                seriesXPoints.shape = PointsGraphSeries.Shape.POINT
                seriesXPoints.color = ContextCompat.getColor(this, R.color.colory4)
                seriesXPoints.size = 5f
                binding.graph2.addSeries(seriesXPoints)
            }
            if (!::seriesYPoints.isInitialized) {
                seriesYPoints = PointsGraphSeries()
                seriesYPoints.shape = PointsGraphSeries.Shape.POINT
                seriesYPoints.color = ContextCompat.getColor(this, R.color.color_app)
                seriesYPoints.size = 5f
                binding.graph2.addSeries(seriesYPoints)
            }
            if (!::seriesZPoints.isInitialized) {
                seriesZPoints = PointsGraphSeries()
                seriesZPoints.shape = PointsGraphSeries.Shape.POINT
                seriesZPoints.color = ContextCompat.getColor(this, R.color.colorz2)
                seriesZPoints.size = 5f
                binding.graph2.addSeries(seriesZPoints)
            }

            // Thêm điểm chấm vào biểu đồ
            seriesXPoints.appendData(DataPoint(x, magX.toDouble()), true, 500)
            seriesYPoints.appendData(DataPoint(x, magY.toDouble()), true, 500)
            seriesZPoints.appendData(DataPoint(x, magZ.toDouble()), true, 500)
        }

        // Cập nhật giới hạn Y axis
        val maxY = maxOf(magX.toDouble(), magY.toDouble(), magZ.toDouble())
        val minY = minOf(magX.toDouble(), magY.toDouble(), magZ.toDouble())

        if (maxY > binding.graph2.viewport.getMaxY(false)) {
            binding.graph2.viewport.setMaxY(maxY + 50.0) // Add buffer
        }
        if (minY < binding.graph2.viewport.getMinY(false)) {
            binding.graph2.viewport.setMinY(minY - 50.0) // Add buffer
        }

        binding.graph2.onDataChanged(false, false)
    }

    fun settingGraph3() {
        seriesX = LineGraphSeries()
        seriesY = LineGraphSeries()
        seriesZ = LineGraphSeries()

        // Thêm các thuộc tính để làm mượt đường
        seriesX.apply {
            color = ContextCompat.getColor(this@GoldDetecterActivity, R.color.colory4)
            isDrawDataPoints = false
            thickness = 5
        }
        seriesY.apply {
            color = ContextCompat.getColor(this@GoldDetecterActivity, R.color.color_app)
            isDrawDataPoints = false
            thickness = 5
        }
        seriesZ.apply {
            color = ContextCompat.getColor(this@GoldDetecterActivity, R.color.colorz2)
            isDrawDataPoints = false
            thickness = 5
        }

        binding.graph1.apply {
            addSeries(seriesX)
            addSeries(seriesY)
            addSeries(seriesZ)

            viewport.apply {
                isXAxisBoundsManual = true
                setMinX(0.0)
                setMaxX(500.0)
                isYAxisBoundsManual = true
                setMinY(minYDisplay)
                setMaxY(maxYDisplay)
                isScrollable = true
                isScalable = true
                setScrollableY(false)  // Chỉ cho phép scroll theo trục X
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
        intent = Intent(this@GoldDetecterActivity, SelectThemeActivity::class.java)
        startActivity(intent)
        handel.postDelayed({ isClick = true }, 500)
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

    private fun applyGradienttt(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#ECB92C"),
                            Color.parseColor("#D2AF43")
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

    private fun applyGradienttt9(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#9D4108"),
                            Color.parseColor("#9D4108")
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

    private fun applyGradientyes2(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#105103"),
                            Color.parseColor("#105103")
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
                            Color.parseColor("#811107"),
                            Color.parseColor("#811107")
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

    private fun applyGradientyes32(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#116F04"),
                            Color.parseColor("#116F04")
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
                            Color.parseColor("#720703"),
                            Color.parseColor("#720703")
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

    private fun applyGradientyes4(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#277A00"),
                            Color.parseColor("#277A00")
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
                            Color.parseColor("#941E0E"),
                            Color.parseColor("#941E0E")
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

    private fun applyGradientyes5(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#007B29"),
                            Color.parseColor("#007B29")
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

    private fun applyGradientno5(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (textView.width > 0) {
                    val textShader: Shader = LinearGradient(
                        0f, 0f, textView.width.toFloat(), 0f,
                        intArrayOf(
                            Color.parseColor("#822300"),
                            Color.parseColor("#822300")
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

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        _warningIndicator = TRIGGER_VALUE
        val (isMute, isFlass, isVibration) = getSettings()
        if (isMute) {
            _checkSoundGold = true
            binding.imvMusicOn.visible()
            binding.imvMusicOff.gone()
            binding.imvMusicOn2.visible()
            binding.imvMusicOff2.gone()
        } else {
            _checkSoundGold = false
            binding.imvMusicOn.gone()
            binding.imvMusicOff.visible()
            binding.imvMusicOn2.gone()
            binding.imvMusicOff2.visible()
        }
        if (isFlass) {
            _checkFlashGold = true
            binding.imvFlashOn.visible()
            binding.imvFlashOff.gone()
            binding.imvFlashOn2.visible()
            binding.imvFlashOff2.gone()
        } else {
            _checkFlashGold = false
            binding.imvFlashOn.gone()
            binding.imvFlashOff.visible()
            binding.imvFlashOn2.gone()
            binding.imvFlashOff2.visible()
        }

        if (isVibration) {
            _checkVibrateGold = true
            binding.imvVibrateOn.visible()
            binding.imvVibrateOff.gone()
            binding.imvVibrateOn2.visible()
            binding.imvVibrateOff2.gone()

        } else {
            _checkVibrateGold = false
            binding.imvVibrateOn.gone()
            binding.imvVibrateOff.visible()
            binding.imvVibrateOn2.gone()
            binding.imvVibrateOff2.visible()
        }
    }

    fun getSettings(): Triple<Boolean, Boolean, Boolean> {
        val isMute = shareData.getBooleanMute("muteGOLD", true)
        val isFlass = shareData.getBooleanflass("flassGOLD", true)
        val isVibration = shareData.getBooleanvibration("vibrationGOLD", true)
        return Triple(isMute, isFlass, isVibration)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@GoldDetecterActivity, HomeActivity::class.java))
    }
}