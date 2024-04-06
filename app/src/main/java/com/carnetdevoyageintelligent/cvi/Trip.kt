package com.carnetdevoyageintelligent.cvi

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Trip : Fragment() {
    private lateinit var viewFragment: View
    private val tripList = mutableListOf<String>()
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
        button.setOnClickListener {
            showMyDialog()
        }

        // Utilisez la variable de classe tripList ici
        val recyclerView: RecyclerView = view.findViewById(R.id.tripRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = TripAdapter(tripList)
        recyclerView.adapter = adapter
    }
    private fun showMyDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Ajout d'un nouveau voyage")
        builder.setMessage("Déterminer le nom du voyage")
        val editTextTripName = EditText(requireContext())
        builder.setView(editTextTripName)
        builder.setNeutralButton("Ajouter un voyage") { dialog, _ ->
            val tripName = editTextTripName.text.toString()
            if (tripName.isNotEmpty()) {
                createFolderInStorage(tripName)
                tripList.add(tripName) // Ajouter le nom du voyage à la liste
                // Mettez à jour votre RecyclerView avec la nouvelle liste de voyages
                updateRecyclerView()
            }
            dialog.dismiss() // Fermer la fenêtre
        }
        builder.setNegativeButton("Annuler") { dialog, _ ->
            dialog.dismiss() // Fermer la fenêtre
        }

        val dialog = builder.create()
        dialog.show()
    }
    private fun createFolderInStorage(folderName: String) {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        val tripFolderRef: StorageReference = storageRef.child(folderName)

        // Ajoutez un fichier vide dans le dossier pour le créer
        val emptyFile: ByteArray = byteArrayOf()
        tripFolderRef.putBytes(emptyFile)
            .addOnSuccessListener {
                // Le dossier a été créé avec succès
                Log.d(TAG, "Dossier vide créé avec succès: $folderName")
            }
            .addOnFailureListener { exception ->
                // Une erreur s'est produite lors de la création du dossier
                Log.e(TAG, "Erreur lors de la création du dossier vide $folderName: ${exception.message}")
            }
    }






    @SuppressLint("NotifyDataSetChanged")
    private fun updateRecyclerView() {
        // Utilisez la liste de voyages pour mettre à jour votre RecyclerView avec les nouveaux noms de voyage
        val recyclerView = view?.findViewById<RecyclerView>(R.id.tripRecyclerView)
        recyclerView?.adapter?.notifyDataSetChanged()
    }

}