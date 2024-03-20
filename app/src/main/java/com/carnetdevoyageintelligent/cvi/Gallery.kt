package com.carnetdevoyageintelligent.cvi

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage

class Gallery : Fragment() {

    private lateinit var galleryAdapter: GalleryAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_gallery, container, false)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerView_gallery)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Retrieve image URLs from Firebase Storage
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference.child("images")

        val imageUrls = mutableListOf<String>()

        storageReference.listAll().addOnSuccessListener { listResult ->
            listResult.items.forEach { item ->
                item.downloadUrl.addOnSuccessListener { url ->
                    imageUrls.add(url.toString())
                    galleryAdapter.notifyDataSetChanged()
                }
            }
        }

        // Initialize and set adapter
        galleryAdapter = GalleryAdapter(imageUrls)
        recyclerView.adapter = galleryAdapter

        return rootView
    }
}