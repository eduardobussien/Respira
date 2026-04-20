package com.example.respira

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.respira.databinding.ActivityMainBinding

class SessionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: TimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.btnBack.setOnClickListener {
            finish()
        }

        viewModel.timeLeft.observe(this) { timeText ->
            binding.tvTimer.text = timeText
        }

        viewModel.instruction.observe(this) { resId ->
            binding.tvInstruction.setText(resId)
        }

        viewModel.techniqueName.observe(this) { resId ->
            binding.tvTechnique.setText(resId)
        }

        viewModel.progress.observe(this) { value ->
            binding.progressBar.progress = value
        }

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

        binding.btnToggle.setOnClickListener {
            viewModel.toggleTimer()
        }

        binding.btnMode.setOnClickListener {
            viewModel.switchMode()
        }
    }
}