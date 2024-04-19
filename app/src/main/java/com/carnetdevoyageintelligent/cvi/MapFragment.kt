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
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker


class MapFragment : Fragment(), OnLocationReceivedListener {

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
    override fun onLocationReceived(latitude: Double, longitude: Double) {
        // Créer un marqueur à partir des coordonnées et l'ajouter à la MapView
        val marker = Marker(mapView)
        marker.position = GeoPoint(latitude, longitude)
        Log.d(TAG, "création du marker pour les coordonnées : $latitude , $longitude")
        mapView.overlays.add(marker)
        mapView.invalidate() // Rafraîchir la MapView pour afficher le marqueur
    }
}

