package com.example.respira

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.respira.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class SessionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TimerViewModel by viewModels()

    private var techniquesList: List<BreathingTechnique> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        val database = TechniqueDatabase.getDatabase(this)
        lifecycleScope.launch {
            database.techniqueDao().getAllTechniques().collect { list ->
                techniquesList = list
            }
        }

        binding.btnMode.setOnClickListener {

            val defaultTechniques = listOf(
                BreathingTechnique(id = -1, title = "Relax (4-7-8)", inhale = 4, hold = 7, exhale = 8),
                BreathingTechnique(id = -2, title = "Calm (5-5-5)", inhale = 5, hold = 5, exhale = 5),
                BreathingTechnique(id = -3, title = "Energize (6-0-2)", inhale = 6, hold = 0, exhale = 2)
            )

            val allTechniques = defaultTechniques + techniquesList

            val titles = allTechniques.map { it.title }.toTypedArray()

            MaterialAlertDialogBuilder(this)
                .setTitle("Select a Rhythm")
                .setItems(titles) { _, which ->
                    val selected = allTechniques[which]

                    viewModel.setTechnique(selected.title, selected.inhale, selected.hold, selected.exhale)
                }
                .show()
        }

        viewModel.timeLeft.observe(this) { timeText ->
            binding.tvTimer.text = timeText
        }

        viewModel.instruction.observe(this) { resId ->
            binding.tvInstruction.setText(resId)
        }

        viewModel.techniqueName.observe(this) { name ->
            binding.tvTechnique.text = name
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
    }
}