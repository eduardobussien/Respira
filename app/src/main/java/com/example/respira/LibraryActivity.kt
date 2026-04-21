package com.example.respira

import android.os.Bundle
import android.view.View
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

        val database = TechniqueDatabase.getDatabase(this)

        adapter = TechniqueAdapter(
            onEditClick = { technique ->
                val editDialog = AddTechniqueFragment()
                editDialog.techniqueToEdit = technique
                editDialog.show(supportFragmentManager, "EditTechniqueFragment")
            },
            onDeleteClick = { technique ->
                lifecycleScope.launch {
                    database.techniqueDao().delete(technique)
                }
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            database.techniqueDao().getAllTechniques().collect { techniquesList ->
                adapter.submitList(techniquesList)
                binding.tvEmptyState.visibility = if (techniquesList.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        binding.fabAdd.setOnClickListener {
            val addDialog = AddTechniqueFragment()
            addDialog.show(supportFragmentManager, "AddTechniqueFragment")
        }
    }
}