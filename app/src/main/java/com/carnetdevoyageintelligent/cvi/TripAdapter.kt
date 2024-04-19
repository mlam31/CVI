package com.carnetdevoyageintelligent.cvi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KFunction1

class TripAdapter(
    private val trips: MutableList<String>,
    private val showPopupMenu: (String, View) -> Unit,
    private val recyclerView: RecyclerView,
    private val previewPhotosClick: (String) -> Unit,
    private val addPhotosClick: KFunction1<String, Unit>,
    private val getPhotosLocation: (String) -> Unit


): RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tripNameTextView: TextView = itemView.findViewById(R.id.tripNameTextView)
        val optionsButton: ImageButton = itemView.findViewById(R.id.options_button)
        val previewPhotosButton: ImageButton = itemView.findViewById(R.id.preview_photos_button)
        val addPhotosButton: ImageButton = itemView.findViewById(R.id.add_photos_button)
        val previewPhotosOnMapButton: ImageButton = itemView.findViewById(R.id.preview_photos_on_map_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trip_item_layout, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val currentTrip = trips[position]
        holder.tripNameTextView.text = currentTrip
        holder.optionsButton.setOnClickListener {
            showPopupMenu(currentTrip, it)
        }
        holder.previewPhotosButton.setOnClickListener {
            recyclerView.visibility = View.GONE

            previewPhotosClick(currentTrip)
        }
        holder.addPhotosButton.setOnClickListener{
            addPhotosClick(currentTrip)
        }
        holder.previewPhotosOnMapButton.setOnClickListener{
            getPhotosLocation(currentTrip)
        }
    }
    override fun getItemCount() = trips.size


}
