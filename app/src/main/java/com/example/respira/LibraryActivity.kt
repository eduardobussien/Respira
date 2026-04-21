package com.example.respira

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.respira.databinding.ActivityLibraryBinding
import kotlinx.coroutines.launch

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding
    private lateinit var adapter: TechniqueAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackLibrary.setOnClickListener {
            finish()
        }

        adapter = TechniqueAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        val database = TechniqueDatabase.getDatabase(this)

        lifecycleScope.launch {
            database.techniqueDao().getAllTechniques().collect { techniquesList ->
                adapter.submitList(techniquesList)
            }
        }

        binding.fabAdd.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_technique, null)
            val etTitle = dialogView.findViewById<android.widget.EditText>(R.id.etTitle)
            val etInhale = dialogView.findViewById<android.widget.EditText>(R.id.etInhale)
            val etHold = dialogView.findViewById<android.widget.EditText>(R.id.etHold)
            val etExhale = dialogView.findViewById<android.widget.EditText>(R.id.etExhale)

            com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Save") { dialog, _ ->
                    val title = etTitle.text.toString()
                    val inhaleStr = etInhale.text.toString()
                    val holdStr = etHold.text.toString()
                    val exhaleStr = etExhale.text.toString()

                    if (title.isNotEmpty() && inhaleStr.isNotEmpty() && holdStr.isNotEmpty() && exhaleStr.isNotEmpty()) {
                        val newTechnique = BreathingTechnique(
                            title = title,
                            inhale = inhaleStr.toInt(),
                            hold = holdStr.toInt(),
                            exhale = exhaleStr.toInt()
                        )

                        lifecycleScope.launch {
                            database.techniqueDao().insert(newTechnique)
                        }
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}