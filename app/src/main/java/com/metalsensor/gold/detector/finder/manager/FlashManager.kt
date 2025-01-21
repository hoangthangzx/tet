package com.metalsensor.gold.detector.finder.manager

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.Looper



class FlashlightManager(private val context: Context) {

    private var cameraManager: CameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var cameraId: String = cameraManager.cameraIdList[0]
    private var handler: Handler = Handler(Looper.getMainLooper())

    fun turnOnFlashlight() {
        cameraManager.setTorchMode(cameraId, true)
    }

    fun turnOffFlashlight() {
        cameraManager.setTorchMode(cameraId, false)
    }

    fun discoMode() {
        val discoPattern = longArrayOf(200, 100, 200, 100, 200, 100)
        startFlashPattern(discoPattern)
    }

    fun sosMode() {
        val sosPattern = longArrayOf(100, 100, 100, 300, 300, 300, 100, 100, 100)
        startFlashPattern(sosPattern)
    }

    private fun startFlashPattern(pattern: LongArray) {
        var index = 0

        val flashRunnable = object : Runnable {
            override fun run() {
                if (index >= pattern.size) {
                    index = 0
                }
                val isOn = index % 2 == 0
                cameraManager.setTorchMode(cameraId, isOn)
                handler.postDelayed(this, pattern[index])
                index++
            }
        }
        handler.post(flashRunnable)
    }

    fun stopFlashPattern() {
        handler.removeCallbacksAndMessages(null)
        turnOffFlashlight()
    }
}