package com.carnetdevoyageintelligent.cvi

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageButton

class Trip : Fragment() {
    private lateinit var viewFragment: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_trip, container, false)
        viewFragment = view // Assigner la vue inflatée à la variable viewFragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button: AppCompatImageButton = view.findViewById(R.id.add_trip_button)
        button.setOnClickListener{
            showMyDialog()
        }
    }
    private fun showMyDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_trip, null)
        builder.setView(dialogView)


        val dialog = builder.create()
        dialog.show()

        val editTextTripName = dialogView.findViewById<EditText>(R.id.editTextTripName)

        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)
        val buttonAddTrip = dialogView.findViewById<Button>(R.id.buttonAddTrip)


        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonAddTrip.setOnClickListener {
            val tripName = editTextTripName.text.toString()
            // fonction qui ajoute un dossier dans firebase et dans l'interface
            dialog.dismiss()
        }
    }


}