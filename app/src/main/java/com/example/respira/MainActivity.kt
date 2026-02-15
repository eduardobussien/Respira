package com.example.respira

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.respira.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Enable ViewBinding
    private lateinit var binding: ActivityMainBinding

    // Connect ViewModel
    private val viewModel: TimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Timer
        viewModel.timeLeft.observe(this) { timeText ->
            binding.tvTimer.text = timeText
        }

        // Instructions
        viewModel.instruction.observe(this) { resId ->
            binding.tvInstruction.setText(resId)
        }

        // Technique Name
        viewModel.techniqueName.observe(this) { resId ->
            binding.tvTechnique.setText(resId)
        }

        // Progress Circle
        viewModel.progress.observe(this) { value ->
            binding.progressBar.progress = value
        }

        // Button State
        viewModel.isRunning.observe(this) { running ->
            if (running) {
                binding.btnToggle.text = getString(R.string.btn_stop)
                binding.btnToggle.setTextColor(getColor(R.color.sage_bg))
                binding.btnToggle.background.setTint(getColor(R.color.white))
            } else {
                binding.btnToggle.text = getString(R.string.btn_start)
                binding.btnToggle.setTextColor(getColor(R.color.sage_bg))
                binding.btnToggle.background.setTint(getColor(R.color.white))
            }
        }

        // CLICKS (User Actions)

        binding.btnToggle.setOnClickListener {
            viewModel.toggleTimer()
        }

        binding.btnMode.setOnClickListener {
            viewModel.switchMode()
        }
    }
}