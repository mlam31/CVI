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

class PreviewPhotosFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val imageUrls = mutableListOf<String>()
    private lateinit var  tripName: String
    private lateinit var imageAdapter: ImageGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preview_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tripName = arguments?.getString("tripName") ?: ""
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        // Initialize adapter with an empty list
        imageAdapter = ImageGridAdapter(imageUrls)
        recyclerView.adapter = imageAdapter

        // Add equal spacing between grid items
        val spanCount = 2
        val spacing = resources.getDimensionPixelSize(R.dimen.spacing)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, true))
        // Fetch image URLs
        fetchImageUrls()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchImageUrls() {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        val imagesRef: StorageReference = storageRef.child(tripName)

        imagesRef.listAll()
            .addOnSuccessListener { listResult ->
                listResult.items.forEach { item ->
                    item.downloadUrl.addOnSuccessListener { imageUrl ->
                        imageUrls.add(imageUrl.toString())
                        imageAdapter.notifyDataSetChanged() // Notify adapter after adding image URL
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
        private const val TAG = "PreviewPhotosFragment"
    }
}