package com.carnetdevoyageintelligent.cvi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TripAdapter(private val trips: MutableList<String>, private val showPopupMenu: (String, View) -> Unit) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tripNameTextView: TextView = itemView.findViewById(R.id.tripNameTextView)
        val optionsButton: ImageButton = itemView.findViewById(R.id.options_button)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trip_item_layout, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val currentTrip = trips[position]
        holder.tripNameTextView.text = currentTrip
        holder.optionsButton.setOnClickListener {
            showPopupMenu(currentTrip, holder.optionsButton)
        }
    }

    override fun getItemCount() = trips.size


}
