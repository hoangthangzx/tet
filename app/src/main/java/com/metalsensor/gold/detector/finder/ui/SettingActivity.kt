package com.metalsensor.gold.detector.finder.ui

import android.app.Dialog
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import com.metalsensor.gold.detector.finder.AbsBaseActivity
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.SharedPreferenceUtils
import com.metalsensor.gold.detector.finder.databinding.ActivitySettingBinding
import com.metalsensor.gold.detector.finder.utils.Const
import com.metalsensor.gold.detector.finder.utils.Const.KEY_MUSIC_TITLE
import com.metalsensor.gold.detector.finder.utils.Const.KEY_MUSIC_TITLE_GOLD
import com.metalsensor.gold.detector.finder.utils.Const.KEY_URI_MUSIC
import com.metalsensor.gold.detector.finder.utils.Const.KEY_URI_MUSIC_GOLD
import com.metalsensor.gold.detector.finder.utils.Const.REQUEST_CODE_RINGTONE_PICKER
import com.metalsensor.gold.detector.finder.utils.Const.SOUND_TITLE
import com.metalsensor.gold.detector.finder.utils.Const.TRIGGER_VALUE
import com.metalsensor.gold.detector.finder.utils.Const.TYPE_METAL
import com.metalsensor.gold.detector.finder.utils.Const.URI_MUSIC
import com.metalsensor.gold.detector.finder.utils.SystemUtils
import com.metalsensor.gold.detector.finder.utils.gone
import com.metalsensor.gold.detector.finder.utils.onSingleClick
import com.metalsensor.gold.detector.finder.utils.visible

class SettingActivity : AbsBaseActivity<ActivitySettingBinding>(false) {
    override fun getFragmentID(): Int = 0
    override fun getLayoutId(): Int = R.layout.activity_setting
    lateinit var shareData: SharedPreferenceUtils
    private var click = false
    override fun init() {
        initData()
        initView()
        initAction()
    }

    private fun initData() {
        shareData = SharedPreferenceUtils.getInstance(this)
        if (TYPE_METAL.equals("METAL")) {
            TRIGGER_VALUE =
                shareData.getStringTriggerValue(Const.TRIGGER_VALUE_KEY)?.toInt().toString().trim()
            SOUND_TITLE = shareData.getStringValue(KEY_MUSIC_TITLE).toString()
        } else {
            TRIGGER_VALUE =
                shareData.getStringTriggerValue(Const.TRIGGER_VALUE_GOLD_KEY)?.toInt().toString()
                    .trim()
            SOUND_TITLE = shareData.getStringValue(KEY_MUSIC_TITLE_GOLD).toString()
        }
        if (TYPE_METAL == "METAL") {
            val (isMute, isFlass, isVibration) = getSettings()
            if (isMute) {
                binding.valumeon.visible()
                binding.valumeoff.gone()
            } else {
                binding.valumeon.gone()
                binding.valumeoff.visible()
            }
            if (isFlass) {
                binding.flasson.visible()
                binding.flassoff.gone()
            } else {
                binding.flassoff.visible()
                binding.flasson.gone()
            }
            if (isVibration) {
                binding.vibration.visible()
                binding.vibrationoff.gone()

            } else {
                binding.vibrationoff.visible()
                binding.vibration.gone()
            }
        } else {
            val (isMute, isFlass, isVibration) = getSettings2()
            if (isMute) {
                binding.valumeon.visible()
                binding.valumeoff.gone()
            } else {
                binding.valumeon.gone()
                binding.valumeoff.visible()
            }
            if (isFlass) {
                binding.flasson.visible()
                binding.flassoff.gone()
            } else {
                binding.flassoff.visible()
                binding.flasson.gone()
            }
            if (isVibration) {
                binding.vibration.visible()
                binding.vibrationoff.gone()

            } else {
                binding.vibrationoff.visible()
                binding.vibration.gone()
            }
        }
    }

    fun getSettings(): Triple<Boolean, Boolean, Boolean> {
        val isMute = shareData.getBooleanMute("mute", true)
        val isFlass = shareData.getBooleanflass("flass", true)
        val isVibration = shareData.getBooleanvibration("vibration", true)
        return Triple(isMute, isFlass, isVibration)
    }

    fun getSettings2(): Triple<Boolean, Boolean, Boolean> {
        val isMute = shareData.getBooleanMute("muteGOLD", true)
        val isFlass = shareData.getBooleanflass("flassGOLD", true)
        val isVibration = shareData.getBooleanvibration("vibrationGOLD", true)
        return Triple(isMute, isFlass, isVibration)
    }
    private fun initView() {
        if (SOUND_TITLE.equals("")) {
            binding.tvSound.text = "ringtone_01"
        } else {
            binding.tvSound.text = SOUND_TITLE
        }
        binding.tvTrigger.text = TRIGGER_VALUE.toInt().toString() + " " + getString(R.string.t)
        binding.tvTrigger.isSelected = true
        binding.tvSound.isSelected = true
        binding.tvSetAlarm.isSelected = true
    }

    private fun initAction() {
        binding.chose.onSingleClick {
            if (click) {
                binding.chose.setImageResource(R.drawable.xuong)
                binding.layoutproces.gone()
                binding.lnTriggerValue.setBackgroundResource(R.color.transparent)
                click = false
            } else {
                binding.chose.setImageResource(R.drawable.nen2)
                showProgressTriggerValue()
                click = true
            }

        }
        binding.imvBack.onSingleClick {
            finish()
        }
        binding.lnAlarmSound.onSingleClick {
            openRingtonePicker()
        }
        if (TYPE_METAL == "METAL") {
            binding.valumeon.onSingleClick {
                binding.valumeon.gone()
                binding.valumeoff.visible()
                shareData.putBooleanMute("mute", false)
            }
            binding.valumeoff.onSingleClick {
                shareData.putBooleanMute("mute", true)
                binding.valumeon.visible()
                binding.valumeoff.gone()
            }
            binding.flasson.onSingleClick {
                shareData.putBooleanflass("flass", false)
                binding.flasson.gone()
                binding.flassoff.visible()
            }
            binding.flassoff.onSingleClick {
                shareData.putBooleanflass("flass", true)
                binding.flassoff.gone()
                binding.flasson.visible()
            }
            binding.vibration.onSingleClick {
                shareData.putBooleanvibration("vibration", false)
                binding.vibration.gone()
                binding.vibrationoff.visible()
            }
            binding.vibrationoff.onSingleClick {
                shareData.putBooleanvibration("vibration", true)
                binding.vibration.visible()
                binding.vibrationoff.gone()
            }
        } else {
            binding.valumeon.onSingleClick {
                binding.valumeon.gone()
                binding.valumeoff.visible()
                shareData.putBooleanMute("muteGOLD", false)
            }
            binding.valumeoff.onSingleClick {
                shareData.putBooleanMute("muteGOLD", true)
                binding.valumeon.visible()
                binding.valumeoff.gone()
            }
            binding.flasson.onSingleClick {
                shareData.putBooleanflass("flassGOLD", false)
                binding.flasson.gone()
                binding.flassoff.visible()
            }
            binding.flassoff.onSingleClick {
                shareData.putBooleanflass("flassGOLD", true)
                binding.flassoff.gone()
                binding.flasson.visible()
            }
            binding.vibration.onSingleClick {
                shareData.putBooleanvibration("vibrationGOLD", false)
                binding.vibration.gone()
                binding.vibrationoff.visible()
            }
            binding.vibrationoff.onSingleClick {
                shareData.putBooleanvibration("vibrationGOLD", true)
                binding.vibration.visible()
                binding.vibrationoff.gone()
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                TRIGGER_VALUE = p1.toString()
                if (TYPE_METAL == "METAL") {
                    shareData.putStringValue(Const.TRIGGER_VALUE_KEY, TRIGGER_VALUE)
                    binding.tvTrigger.text = "$TRIGGER_VALUE ${getString(R.string.t)}"
                    binding.tex2.text = "$TRIGGER_VALUE ${getString(R.string.t)}"
                } else {
                    shareData.putStringValue(Const.TRIGGER_VALUE_GOLD_KEY, TRIGGER_VALUE)
                    binding.tvTrigger.text = "$TRIGGER_VALUE ${getString(R.string.t)}"
                    binding.tex2.text = "$TRIGGER_VALUE ${getString(R.string.t)}"
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

    private fun showProgressTriggerValue() {
        binding.layoutproces.visible()
        binding.lnTriggerValue.setBackgroundResource(R.drawable.bgrvolume)
        // Lấy giá trị trigger
        if (TRIGGER_VALUE == "") {
            val result = binding.tvTrigger.text.toString().trim()
            TRIGGER_VALUE = removeSubstring(result, "μT").trim()
        }
        TRIGGER_VALUE = TRIGGER_VALUE.toInt().toString().trim()
        if (TRIGGER_VALUE.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_the_value, Toast.LENGTH_SHORT).show()
        } else {
            if (TYPE_METAL == "METAL") {
                shareData.putStringValue(Const.TRIGGER_VALUE_KEY, TRIGGER_VALUE)
                binding.tvTrigger.text = "$TRIGGER_VALUE ${getString(R.string.t)}"
                binding.seekBar.progress = TRIGGER_VALUE.toInt()
            } else {
                shareData.putStringValue(Const.TRIGGER_VALUE_GOLD_KEY, TRIGGER_VALUE)
                binding.tvTrigger.text = "$TRIGGER_VALUE ${getString(R.string.t)}"
                binding.seekBar.progress = TRIGGER_VALUE.toInt()
            }
        }
    }

    fun removeSubstring(input: String, toRemove: String): String {
        return input.replace(toRemove, "")
    }

    private fun openRingtonePicker() {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Chọn nhạc chuông")
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, null as Uri?)
        startActivityForResult(intent, REQUEST_CODE_RINGTONE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_RINGTONE_PICKER) {
            val ringtoneUri: Uri? =
                data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            if (ringtoneUri != null) {
                Log.d("check_link", "onActivityResult: " + ringtoneUri)
                val ringtone: Ringtone = RingtoneManager.getRingtone(this, ringtoneUri)
                val ringtoneTitle: String = ringtone.getTitle(this)
                binding.tvSound.text = ringtoneTitle
                SOUND_TITLE = ringtoneTitle
                URI_MUSIC = ringtoneUri.toString()
                if (TYPE_METAL.equals("METAL")) {
                    shareData.putStringValue(KEY_URI_MUSIC, ringtoneUri.toString())
                    shareData.putStringValue(KEY_MUSIC_TITLE, SOUND_TITLE)
                } else {
                    shareData.putStringValue(KEY_URI_MUSIC_GOLD, ringtoneUri.toString())
                    shareData.putStringValue(KEY_MUSIC_TITLE_GOLD, SOUND_TITLE)
                }
            }
        }
    }
}