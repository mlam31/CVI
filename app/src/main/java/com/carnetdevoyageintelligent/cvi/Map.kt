package com.carnetdevoyageintelligent.cvi

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.preference.PreferenceManager
import com.google.firebase.storage.FirebaseStorage
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import android.media.ExifInterface
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class Map : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var zoomInButton: ImageButton
    private lateinit var zoomOutButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = rootView.findViewById(R.id.mapView)
        zoomInButton = rootView.findViewById(R.id.zoom_in_button)
        zoomOutButton = rootView.findViewById(R.id.zoom_out_button)

        // Initialize osmdroid
        Configuration.getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        initializeMap()
        setupZoomButtons()
        loadPhotosFromStorage()
        return rootView
    }
    private fun initializeMap(){
        val latitudeParis = 48.8566
        val longitudeParis = 2.3522
        val geoPoint = GeoPoint(latitudeParis, longitudeParis)
        val parisGeoPoint : IGeoPoint = geoPoint
        mapView.controller.setCenter(parisGeoPoint)

        // Set an appropriate zoom level to display the entire world
        mapView.controller.setZoom(5) // Zoom level 1 usually displays the entire world
        // Set the tile source (e.g., Mapnik)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
    }

    private fun setupZoomButtons(){
        zoomInButton.setOnClickListener{
            mapView.controller.zoomIn()
        }
        zoomOutButton.setOnClickListener{
            mapView.controller.zoomOut()
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun loadPhotosFromStorage() {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val folderName = "tiffany photos"
        storageRef.child(folderName).listAll()
            .addOnSuccessListener { listResult ->
                GlobalScope.launch(Dispatchers.Main) {
                    listResult.items.forEach { photoReference ->
                        // Get download URL for the photo
                        val photoUrl = photoReference.downloadUrl.await()

                        // Extract GPS coordinates from the photo URL
                        val coordinates = extractGPSFromPhoto(photoUrl.toString())

                        coordinates?.let { (latitude, longitude) ->
                            Log.d(TAG, "Latitude: $latitude, Longitude: $longitude")

                        println(coordinates)
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error loading photos from Firebase Storage: ${exception.message}")
            }
    }
    private fun extractGPSFromPhoto(photoPath: String): Pair<Double, Double>? {
        try {
            val exif = ExifInterface(photoPath)
            val latLong = FloatArray(2)
            if (exif.getLatLong(latLong)) {
                val latitude = latLong[0].toDouble()
                val longitude = latLong[1].toDouble()
                val coordinate = Pair(latitude, longitude)
                Log.d(TAG, "$coordinate")
                return coordinate

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

