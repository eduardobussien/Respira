package com.example.respira

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    // LiveData
    private val _timeLeft = MutableLiveData<String>()
    val timeLeft: LiveData<String> = _timeLeft

    private val _instruction = MutableLiveData<Int>()
    val instruction: LiveData<Int> = _instruction

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> = _progress

    private val _isRunning = MutableLiveData<Boolean>()
    val isRunning: LiveData<Boolean> = _isRunning

    private val _techniqueName = MutableLiveData<Int>()
    val techniqueName: LiveData<Int> = _techniqueName

    // Internal State
    private var timer: CountDownTimer? = null
    private var cyclesCompleted = 0
    private val TOTAL_CYCLES = 3

    // Current Mode Tracker
    private enum class Mode { STANDARD_478, BOX_4444 }
    private var currentMode = Mode.STANDARD_478

    init {
        resetSession()
    }

    // User Actions
    fun toggleTimer() {
        if (_isRunning.value == true) {
            stopSession()
        } else {
            startSession()
        }
    }

    fun switchMode() {
        stopSession() // Stop if running

        // Swap Modes
        if (currentMode == Mode.STANDARD_478) {
            currentMode = Mode.BOX_4444
            _techniqueName.value = R.string.technique_box
        } else {
            currentMode = Mode.STANDARD_478
            _techniqueName.value = R.string.technique_standard
        }

        _timeLeft.value = "04s"
    }

    // Session Logic
    private fun startSession() {
        _isRunning.value = true
        cyclesCompleted = 0

        // Pick the right pattern
        if (currentMode == Mode.STANDARD_478) {
            start478Inhale()
        } else {
            startBoxInhale()
        }
    }

    private fun stopSession() {
        timer?.cancel()
        _isRunning.value = false
        _instruction.value = R.string.phase_inhale
        _progress.value = 0
        _timeLeft.value = "04s"
    }

    private fun resetSession() {
        stopSession()
        _techniqueName.value = R.string.technique_standard
    }

    // 4-7-8
    // Inhale (4) -> Hold (7) -> Exhale (8)
    private fun start478Inhale() {
        runPhase(4000L, R.string.phase_inhale, true) { start478Hold() }
    }

    private fun start478Hold() {
        runPhase(7000L, R.string.phase_hold, false) { start478Exhale() }
        _progress.value = 100
    }

    private fun start478Exhale() {
        _instruction.value = R.string.phase_exhale
        timer = object : CountDownTimer(8000L, 50) {
            override fun onTick(millis: Long) {
                updateTimerText(millis)
                _progress.value = ((millis.toFloat() / 8000f) * 100).toInt()
            }
            override fun onFinish() {
                checkLoops { start478Inhale() }
            }
        }.start()
    }

    // Box Breathing
    // Inhale (4) -> Hold (4) -> Exhale (4) -> Hold (4)
    private fun startBoxInhale() {
        runPhase(4000L, R.string.phase_inhale, true) { startBoxHoldFull() }
    }

    private fun startBoxHoldFull() {
        runPhase(4000L, R.string.phase_hold, false) { startBoxExhale() }
        _progress.value = 100
    }

    private fun startBoxExhale() {
        _instruction.value = R.string.phase_exhale
        timer = object : CountDownTimer(4000L, 50) {
            override fun onTick(millis: Long) {
                updateTimerText(millis)
                _progress.value = ((millis.toFloat() / 4000f) * 100).toInt()
            }
            override fun onFinish() {
                startBoxHoldEmpty()
            }
        }.start()
    }

    private fun startBoxHoldEmpty() {
        runPhase(4000L, R.string.phase_hold, false) {
            checkLoops { startBoxInhale() }
        }
        _progress.value = 0
    }

    // Helpers
    private fun checkLoops(onContinue: () -> Unit) {
        cyclesCompleted++
        if (cyclesCompleted < TOTAL_CYCLES) {
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
        val seconds = (millis / 1000) + 1
        _timeLeft.value = String.format("%02ds", seconds)
    }
}