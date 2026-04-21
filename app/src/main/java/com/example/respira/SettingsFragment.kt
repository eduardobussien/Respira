package com.example.respira

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val options = arrayOf("Dark Mode (Coming Soon)", "Haptic Feedback (Coming Soon)")
        val checkedItems = booleanArrayOf(false, true)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Settings")
            .setMultiChoiceItems(options, checkedItems) { _, _, _ ->

            }
            .setPositiveButton("Save", null)
            .create()
    }
}