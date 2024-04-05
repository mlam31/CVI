package com.carnetdevoyageintelligent.cvi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Gallery : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val imageUrls = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view) // Utilise la variable de classe
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        val adapter = ImageGridAdapter(requireContext(), imageUrls)
        recyclerView.adapter = adapter

        // Ajouter l'espacement égal entre les éléments de la grille
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing) // Récupère la taille de l'espacement depuis les ressources
        recyclerView.addItemDecoration(EqualSpacingItemDecoration(3, spacingInPixels, true))

        // Récupération des URLs d'images depuis Firebase Storage
        fetchImageUrls()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchImageUrls() {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        val imagesRef: StorageReference = storageRef.child("images")

        imagesRef.listAll()
            .addOnSuccessListener { listResult ->
                listResult.items.forEach { item ->
                    item.downloadUrl.addOnSuccessListener { imageUrl ->
                        imageUrls.add(imageUrl.toString())
                        recyclerView.adapter?.notifyDataSetChanged()
                    }.addOnFailureListener { exception ->
                        Log.e(TAG, "Failed to fetch image URL: ${exception.message}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to list images: ${exception.message}")
            }
    }

    companion object {
        private const val TAG = "GalleryFragment"
    }
}