package com.example.respira

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class AddTechniqueFragment : DialogFragment() {

    var techniqueToEdit: BreathingTechnique? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_add_technique, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etTitle)
        val etInhale = dialogView.findViewById<EditText>(R.id.etInhale)
        val etHold = dialogView.findViewById<EditText>(R.id.etHold)
        val etExhale = dialogView.findViewById<EditText>(R.id.etExhale)

        techniqueToEdit?.let {
            etTitle.setText(it.title)
            etInhale.setText(it.inhale.toString())
            etHold.setText(it.hold.toString())
            etExhale.setText(it.exhale.toString())
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (techniqueToEdit == null) "New Technique" else "Edit Technique")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val title = etTitle.text.toString()
                val inhaleStr = etInhale.text.toString()
                val holdStr = etHold.text.toString()
                val exhaleStr = etExhale.text.toString()

                if (title.isNotEmpty() && inhaleStr.isNotEmpty() && holdStr.isNotEmpty() && exhaleStr.isNotEmpty()) {

                    val savedId = techniqueToEdit?.id ?: 0

                    val newTechnique = BreathingTechnique(
                        id = savedId,
                        title = title,
                        inhale = inhaleStr.toInt(),
                        hold = holdStr.toInt(),
                        exhale = exhaleStr.toInt()
                    )

                    val database = TechniqueDatabase.getDatabase(requireContext())
                    lifecycleScope.launch {
                        if (techniqueToEdit == null) {
                            database.techniqueDao().insert(newTechnique)
                        } else {
                            database.techniqueDao().update(newTechnique)
                        }
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}