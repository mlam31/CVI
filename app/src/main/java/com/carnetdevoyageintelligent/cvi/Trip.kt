package com.carnetdevoyageintelligent.cvi

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageButton

class Trip : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip, container, false)
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
        builder.setTitle("Ajout d'un nouveau voyage")
        builder.setMessage("Déterminer le nom du voyage")
        builder.setNeutralButton("Ajouter un voyage") { dialog, _ ->
            // Action à effectuer lorsque l'utilisateur appuie sur le bouton Annuler
            dialog.dismiss() // Fermer la fenêtre
        }
        builder.setNegativeButton("Annuler") { dialog, _ ->
            // Action à effectuer lorsque l'utilisateur appuie sur le bouton Annuler
            dialog.dismiss() // Fermer la fenêtre
        }


        val dialog = builder.create()
        dialog.show()
    }

}