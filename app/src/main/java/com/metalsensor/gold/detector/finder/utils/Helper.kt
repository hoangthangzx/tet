package com.metalsensor.gold.detector.finder.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.metalsensor.gold.detector.finder.R


var isSound : Boolean = true
var isFlash : Boolean = true
var isVibrate : Boolean = true

const val APPLICATION = "KEY"
const val IS_LANGUAGE = "IS_LANGUAGE"
const val SELECT_RATE = "SELECT_RATE"
const val INTERACTION = "INTERACTION"
const val LOG_APP = "LOG_APP"
const val CHECK_PERMISSION = "CHECK_PERMISSION"
const val VIDEOCALL = "VIDEOCALL"
const val CALL = "CALL"
const val MESSAGE = "MESSAGE"
const val THIEP = "THIEP"
const val IDOL_I = "IDOL_I"
const val VIBRATION = "VIBRATION"
const val SOUND = "SOUND"
const val FLASH = "FLASH"
const val IncomingCall = "Incoming call"
const val OnThePhone = "OnThePhone"
const val Returns = "Return"

private val handler = Handler(Looper.getMainLooper())
private val _elapsedTime = MutableLiveData<Long>()
val elapsedTime: LiveData<Long> = _elapsedTime
val timerTask = object : Runnable {
    override fun run() {
        val currentTime = _elapsedTime.value ?: 0
        _elapsedTime.value = currentTime + 1
        handler.postDelayed(this, 1000)
    }
}
private var isRunning = false
fun startTimer() {
    if (!isRunning) {
        isRunning = true
        _elapsedTime.value = 0
        handler.postDelayed(timerTask, 1000)
    }
}
fun stopTimer() {
    isRunning = false
    handler.removeCallbacks(timerTask)
}

 fun setMediaPlayer(mediaPlayer : MediaPlayer){
    mediaPlayer.setOnCompletionListener {
        mediaPlayer.seekTo(0)
        mediaPlayer.start()
    }
}
fun openPolicy(context: Context, url: String) {
    try {
        val intent = Uri.parse(url).buildUpon().build()
        context.startActivity(Intent(Intent.ACTION_VIEW, intent))
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun updateImage(check :Boolean ,imageView: ImageView,icSelector : Int,icUnSelector: Int) {
    if (check) {
        imageView.setImageResource(icSelector)
    } else {
        imageView.setImageResource(icUnSelector)
    }
}

fun shareApplication(context: Context) {
    val pInfo: PackageInfo =
        context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
    val appPackageName = pInfo.packageName

    val appName = context.getString(R.string.app_name)
    val shareBodyText = "https://play.google.com/store/apps/details?id=$appPackageName"

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, appName)
        putExtra(Intent.EXTRA_TEXT, shareBodyText)
    }
    context.startActivity(Intent.createChooser(sendIntent, null))
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