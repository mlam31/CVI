package com.carnetdevoyageintelligent.cvi

import ImageGridAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.carnetdevoyageintelligent.cvi.databinding.FragmentGalleryBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Gallery : Fragment() {
    private lateinit var binding: FragmentGalleryBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var adapter: ImageGridAdapter

    private val TAG = "GalleryFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        storage = FirebaseStorage.getInstance()
        val imagesRef: StorageReference = storage.reference.child("images")

        imagesRef.listAll().addOnSuccessListener { result ->
            val imageUrls = mutableListOf<String>()
            for (imageRef in result.items) {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    imageUrls.add(uri.toString())
                    adapter.notifyDataSetChanged()
                }.addOnFailureListener { exception ->
                    Log.e(TAG, "Failed to get download URL: $exception")
                }
            }
            adapter = ImageGridAdapter(requireContext(), imageUrls)
            recyclerView.adapter = adapter
        }.addOnFailureListener {
            Log.e(TAG, "Failed to list images: $it")
        }
    }

    companion object {
        private const val TAG = "GalleryFragment"
    }
}
