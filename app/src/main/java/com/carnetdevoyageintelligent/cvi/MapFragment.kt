package com.carnetdevoyageintelligent.cvi

import android.content.ContentValues.TAG
import android.graphics.Bitmap

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker


class MapFragment : Fragment(){

    private lateinit var mapView: MapView
    private lateinit var zoomInButton: ImageButton
    private lateinit var zoomOutButton: ImageButton
    private lateinit var tripSpinner: Spinner
    private lateinit var getLocationButton: ImageButton

    data class PhotoInfo(
        val latitude: Double,
        val longitude: Double,
        val imageURL: String
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = rootView.findViewById(R.id.mapView)
        zoomInButton = rootView.findViewById(R.id.zoom_in_button)
        zoomOutButton = rootView.findViewById(R.id.zoom_out_button)
        getLocationButton = rootView.findViewById(R.id.get_location_button)
        tripSpinner= rootView.findViewById(R.id.spinnerTripList) // Initialiser tripList ici

        // Initialize osmdroid
        Configuration.getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        initializeMap()
        setupZoomButtons()

        // Charger les noms des dossiers depuis Firebase Storage et les configurer dans la liste déroulante
        loadFolderNamesFromFirebaseStorage()

        getLocationButton.setOnClickListener{
            Log.d(TAG, "bouton appuyé")
            getLocationForSelectedTrip()
        }

        return rootView
    }

    private fun initializeMap(){
        val latitudeParis = 48.8566
        val longitudeParis = 2.3522
        val geoPoint = GeoPoint(latitudeParis, longitudeParis)
        val parisGeoPoint : IGeoPoint = geoPoint
        mapView.controller.setCenter(parisGeoPoint)
        mapView.controller.setZoom(5)
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

    private fun loadFolderNamesFromFirebaseStorage() {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        storageRef.listAll()
            .addOnSuccessListener { listResult ->
                val folderNames = listResult.prefixes.map { it.name }

                // Vérifier si le fragment est attaché avant d'appeler requireContext()
                if (isAdded) {
                    configureSpinner(folderNames)
                } else {
                    Log.e(TAG, "Fragment not attached to a context.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error retrieving folder names: ${exception.message}")
            }
    }

    private fun configureSpinner(folderNames: List<String>) {
        val defaultText = "Choisir le voyage"
        val spinnerArray = mutableListOf(defaultText)
        spinnerArray.addAll(folderNames)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tripSpinner.adapter = adapter
    }

    private fun getLocationForSelectedTrip() {
        Log.d(TAG, "lancement getLocationForSelectionTrip")
        val selectedTrip = tripSpinner.selectedItem.toString()
        val database = FirebaseFirestore.getInstance()
        val collectionRef = database.collection("photos")
        val query = collectionRef
            .whereGreaterThanOrEqualTo("url", "https://firebasestorage.googleapis.com/v0/b/projetbddl3-c232c.appspot.com/o/$selectedTrip")
            .whereLessThan("url", "https://firebasestorage.googleapis.com/v0/b/projetbddl3-c232c.appspot.com/o/${selectedTrip}2%2F")
        Log.d(TAG, "$query")
        query.get()
            .addOnSuccessListener { querySnapshot ->
                val photoList = mutableListOf<PhotoInfo>()
                for (document in querySnapshot.documents) {
                    val data = document.data
                    val coordinates = data?.get("coordinates") as? Map<*, *>
                    val imageURL = data?.get("url") as String
                    val latitude = coordinates?.get("latitude")?.toString()?.toDoubleOrNull()
                    val longitude = coordinates?.get("longitude")?.toString()?.toDoubleOrNull()
                    Log.d(TAG, "coordonnées: $latitude $longitude, url: $imageURL")
                    if (latitude != null && longitude != null) {
                        val photoInfo = PhotoInfo(latitude, longitude, imageURL)
                        photoList.add(photoInfo)

                    } else {
                        Log.e(TAG, "Latitude or longitude is null for document ${document.id}")
                    }
                }
                Log.d(TAG, "La liste de photo: $photoList")
                displayMarkers(photoList)

            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: $exception")
            }
    }
    private fun loadMarkerImage(latitude: Double, longitude: Double, imageURL: String) {
        Log.d(TAG, "lancement loadMarkerImage avec URL : $imageURL")
        activity?.let { fragmentActivity ->
            Glide.with(fragmentActivity)
                .asBitmap()
                .load(imageURL)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        Log.d(TAG, "lancement onResourceReady")
                        val iconSize = 150
                        val scaledBitmap = Bitmap.createScaledBitmap(resource, iconSize, iconSize, true)
                        val photoMarker = Marker(mapView)
                        photoMarker.position = GeoPoint(latitude, longitude)
                        photoMarker.icon = BitmapDrawable(resources, scaledBitmap)
                        mapView.overlays.add(photoMarker)
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Not used in this case
                    }
                })
        }

        Log.d(TAG, "fin de loadMarkerImage")
    }




    private fun displayMarkers(photoList: List<PhotoInfo>) {
        Log.d(TAG, "lancement de displayMarkers")
        for (photo in photoList) {
            loadMarkerImage(photo.latitude, photo.longitude, photo.imageURL)
        }
        Log.d(TAG, "fin de displayMarkers")
    }



}

