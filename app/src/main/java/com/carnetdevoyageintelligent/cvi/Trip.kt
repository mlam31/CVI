package com.carnetdevoyageintelligent.cvi

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage


class Trip : Fragment() {
    private lateinit var viewFragment: View
    private val tripList = mutableListOf<String>()
    private var tripName: String? = null
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
            tripName = editTextTripName.text.toString() // Assigner le nom du voyage ici
            if (tripName!!.isNotEmpty()) {
                dialog.dismiss() // Fermer la fenêtre
                openGalleryForPhotos()
            } else {
                // Afficher un message d'erreur si le champ est vide
                Toast.makeText(requireContext(), "Veuillez saisir un nom de voyage", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Annuler") { dialog, _ ->
            dialog.dismiss() // Fermer la fenêtre
        }

        val dialog = builder.create()
        dialog.show()
    }


    private fun openGalleryForPhotos() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        pickImages.launch(intent)
    }
    private fun uploadImagesToFirebase(selectedImagesUriList: List<Uri>, tripName: String) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val tripFolderRef = storageRef.child(tripName)

        selectedImagesUriList.forEachIndexed { index, uri ->
            val imageRef = tripFolderRef.child("image_$index.jpg")
            imageRef.putFile(uri)
                .addOnSuccessListener {
                    // L'image a été téléchargée avec succès
                    Log.d(TAG, "Image uploaded successfully: ${it.metadata?.path}")
                }
                .addOnFailureListener { e ->
                    // Erreur lors du téléchargement de l'image
                    Log.e(TAG, "Error uploading image: ${e.message}")
                }
        }
    }
    private val pickImages =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                // Traitement des données retournées par l'activité de sélection d'images
                data?.let {
                    val selectedImagesUriList = mutableListOf<Uri>()
                    if (it.clipData != null) {
                        // Plusieurs images sélectionnées
                        val clipData = it.clipData
                        for (i in 0 until clipData!!.itemCount) {
                            val uri = clipData.getItemAt(i).uri
                            selectedImagesUriList.add(uri)
                        }
                    } else if (it.data != null) {
                        // Une seule image sélectionnée
                        val uri = it.data
                        selectedImagesUriList.add(uri!!)
                    }
                    // Télécharger les images dans Firebase Storage
                    tripName?.let { name ->
                        uploadImagesToFirebase(selectedImagesUriList, name)
                    }
                }
            }
        }

}


