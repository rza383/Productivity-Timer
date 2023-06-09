package org.hyperskill.stopwatch

import android.graphics.Color
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Random
import java.util.concurrent.TimeUnit

const val startTime = "00:00"
const val defaultTime = 1500000L

class TimerViewModel: ViewModel() {
    private var elapsedTime: MutableLiveData<String> = MutableLiveData<String>(startTime)
    val _elapsedTime: LiveData<String> = elapsedTime

    private var isStartBtnEnabled: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val _isStartBtnEnabled = isStartBtnEnabled

    private var progressBarColor: MutableLiveData<Int> = MutableLiveData()
    val _progressBarColor: LiveData<Int> = progressBarColor

    private var isTimeLimitExceeded: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val _isTimeLimitExceeded: LiveData<Boolean> = isTimeLimitExceeded

    private var setPoint = defaultTime
    private var countDownTimer = createCountdownTimer()

    fun startTimer() {
        countDownTimer.start()
        setStartBtn()
    }

    fun endTimer() {
        countDownTimer.cancel()
        setStartBtn()
        elapsedTime.value = startTime
    }
    fun setStartBtn(){
        isStartBtnEnabled.value = !isStartBtnEnabled.value!!
    }

    private fun generateColors(): Int{
        var rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    fun updateSetPoint(newTime: Long){
        setPoint = newTime
    }

    fun defaultSetPoint(){
        setPoint = defaultTime
    }

    private fun createCountdownTimer(): CountDownTimer =
        object : CountDownTimer(1500000L, 1000) {
            override fun onTick(p0: Long) {
                val diff = 1500000L  - p0
                elapsedTime.value = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(diff),
                    TimeUnit.MILLISECONDS.toSeconds(diff) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            diff
                        )
                    )
                )
                Log.d("Debug", "$setPoint $diff")
                if(setPoint / 1000 < diff / 1000 && setPoint > 0)
                    isTimeLimitExceeded.value = true
                progressBarColor.value = generateColors()
            }

            override fun onFinish() {
                elapsedTime.value = startTime
                setStartBtn()
            }
        }
}