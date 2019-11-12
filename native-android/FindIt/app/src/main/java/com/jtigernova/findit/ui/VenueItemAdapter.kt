package com.jtigernova.findit.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jtigernova.findit.Nav
import com.jtigernova.findit.R
import com.jtigernova.findit.data.Venue

class VenueItemAdapter(private val context: Context, private val data: Array<Venue>) :
        RecyclerView.Adapter<VenueItemAdapter.ViewHolder>() {
    class ViewHolder(val view: LinearLayout) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_venue_list_item, parent, false) as LinearLayout

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val venue = data[position]

        //replace the view
        holder.view.findViewById<ImageView>(R.id.venue_image).setImageResource(
                R.drawable.ic_launcher_foreground)
        holder.view.findViewById<TextView>(R.id.venue_name).text = venue.name

        holder.view.setOnClickListener {
            Toast.makeText(context, "Why did you click ${venue.name}", Toast.LENGTH_LONG).show()

            Nav.venueDetails(context, venue)
        }
    }

    override fun getItemCount() = data.size

}