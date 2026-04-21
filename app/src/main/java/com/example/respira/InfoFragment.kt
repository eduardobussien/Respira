package com.example.respira

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InfoFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("About Respira")
            .setMessage("Respira is designed to help you find your center through guided breathing techniques. \n\nRemember to breathe from your diaphragm and find a quiet space for the best experience.")
            .setPositiveButton("Got it!", null)
            .create()
    }
}