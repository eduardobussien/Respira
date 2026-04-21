package com.example.respira

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val prefs = requireContext().getSharedPreferences("prefs_respira", Context.MODE_PRIVATE)
        val options = arrayOf("1 cycle (~30s)", "3 cycles (~90s)", "5 cycles (~2.5min)")
        val cycleValues = intArrayOf(1, 3, 5)
        val savedCycles = prefs.getInt("pref_cycles", 3)
        val checkedIndex = cycleValues.indexOfFirst { it == savedCycles }.coerceAtLeast(0)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Session Length")
            .setSingleChoiceItems(options, checkedIndex) { _, which ->
                prefs.edit().putInt("pref_cycles", cycleValues[which]).apply()
            }
            .setPositiveButton("Done", null)
            .create()
    }
}
