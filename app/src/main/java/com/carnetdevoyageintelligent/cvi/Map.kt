package com.carnetdevoyageintelligent.cvi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint


class Map: Fragment() {

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = rootView.findViewById(R.id.mapView)

        // Initialize osmdroid
        Configuration.getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )

        // Set the center of the map to the world center (latitude: 0, longitude: 0)
        val latitudeParis = 48.8566
        val longitudeParis = 2.3522
        val geoPoint = GeoPoint(latitudeParis, longitudeParis)
        val parisGeoPoint : IGeoPoint = geoPoint
        mapView.controller.setCenter(parisGeoPoint)

        // Set an appropriate zoom level to display the entire world
        mapView.controller.setZoom(13) // Zoom level 1 usually displays the entire world

        // Set the tile source (e.g., Mapnik)
        mapView.setTileSource(TileSourceFactory.MAPNIK)

        return rootView
    }

}
