package com.example.respira

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Locale
import kotlin.math.ceil

class TimerViewModel : ViewModel() {

    private val _timeLeft = MutableLiveData<String>()
    val timeLeft: LiveData<String> = _timeLeft

    private val _instruction = MutableLiveData<Int>()
    val instruction: LiveData<Int> = _instruction

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> = _progress

    private val _isRunning = MutableLiveData<Boolean>()
    val isRunning: LiveData<Boolean> = _isRunning

    private val _techniqueName = MutableLiveData<String>()
    val techniqueName: LiveData<String> = _techniqueName

    private var timer: CountDownTimer? = null
    private var cyclesCompleted = 0
    private var totalCycles = 3

    fun setCycles(n: Int) {
        if (_isRunning.value != true) totalCycles = n
    }

    private var currentInhale = 4000L
    private var currentHold = 7000L
    private var currentExhale = 8000L

    init {
        setTechnique("Quick Session (4-7-8)", 4, 7, 8)
    }

    fun setTechnique(title: String, inhale: Int, hold: Int, exhale: Int) {
        stopSession()
        _techniqueName.value = title
        currentInhale = inhale * 1000L
        currentHold = hold * 1000L
        currentExhale = exhale * 1000L

        _timeLeft.value = String.format(Locale.ROOT, "%02ds", inhale)
    }

    fun toggleTimer() {
        if (_isRunning.value == true) {
            stopSession()
        } else {
            startSession()
        }
    }

    private fun startSession() {
        _isRunning.value = true
        cyclesCompleted = 0
        startDynamicInhale()
    }

    private fun stopSession() {
        timer?.cancel()
        _isRunning.value = false
        _instruction.value = R.string.phase_inhale
        _progress.value = 0
        _timeLeft.value = String.format(Locale.ROOT, "%02ds", currentInhale / 1000)
    }

    private fun startDynamicInhale() {
        runPhase(currentInhale, R.string.phase_inhale, true) { startDynamicHold() }
    }

    private fun startDynamicHold() {
        if (currentHold > 0) {
            runPhase(currentHold, R.string.phase_hold, false) { startDynamicExhale() }
            _progress.value = 100
        } else {
            startDynamicExhale()
        }
    }

    private fun startDynamicExhale() {
        _instruction.value = R.string.phase_exhale
        timer = object : CountDownTimer(currentExhale, 50) {
            override fun onTick(millis: Long) {
                updateTimerText(millis)
                _progress.value = ((millis.toFloat() / currentExhale.toFloat()) * 100).toInt()
            }
            override fun onFinish() {
                checkLoops { startDynamicInhale() }
            }
        }.start()
    }

    private fun checkLoops(onContinue: () -> Unit) {
        cyclesCompleted++
        if (cyclesCompleted < totalCycles) {
            onContinue()
        } else {
            stopSession()
            _instruction.value = R.string.phase_done
        }
    }

    private fun runPhase(duration: Long, textRes: Int, isFilling: Boolean, onFinish: () -> Unit) {
        _instruction.value = textRes
        timer = object : CountDownTimer(duration, 50) {
            override fun onTick(millis: Long) {
                updateTimerText(millis)
                if (isFilling) {
                    _progress.value = 100 - ((millis.toFloat() / duration.toFloat()) * 100).toInt()
                }
            }
            override fun onFinish() {
                onFinish()
            }
        }.start()
    }

    private fun updateTimerText(millis: Long) {
        val seconds = ceil(millis / 1000.0).toLong()
        _timeLeft.value = String.format(Locale.ROOT, "%02ds", seconds)
    }
}