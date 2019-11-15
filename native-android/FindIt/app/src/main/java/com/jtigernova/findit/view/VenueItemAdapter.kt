package com.jtigernova.findit.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jtigernova.findit.R
import com.jtigernova.findit.ext.loadImgInto
import com.jtigernova.findit.ext.setupFavorite
import com.jtigernova.findit.model.Venue
import com.jtigernova.findit.viewmodel.FavoriteViewModel

class VenueItemAdapter(private val context: Context, val venues: List<Venue>,
                       private val favoriteVenueIds: Set<String>,
                       private val favoriteViewModel: FavoriteViewModel,
                       private val venueClick: (Venue, viewPosition: Int) -> Unit) :
        RecyclerView.Adapter<VenueItemAdapter.ViewHolder>() {
    class ViewHolder(val view: LinearLayout) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_venue_list_item, parent, false) as LinearLayout

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val venue = venues[position]

        holder.view.tag = venue.id

        val name = holder.view.findViewById<TextView>(R.id.venue_name)
        val icon = holder.view.findViewById<ImageView>(R.id.venue_image)
        val fav = holder.view.findViewById<CheckBox>(R.id.venue_fav)

        //replace the view
        name.text = venue.name
        holder.view.findViewById<TextView>(R.id.venue_distance_from_city_center).text =
                context.getString(R.string.distance_from_city_center,
                        venue.location?.getDistanceFromCityCenterInMilesDisplay())

        //load image
        context.loadImgInto(uri = venue.mainCategory?.icon?.fullPath,
                imageView = holder.view.findViewById(R.id.venue_image))

        //TODO this will be a bug when favorites are changed on another screen and then
        //return to the screen and then scroll
        fav.isChecked = favoriteVenueIds.contains(venue.id)

        fav.setupFavorite(context = context, favViewModel = favoriteViewModel,
                venue = venue, onCheckedChange = {
            //notifyItemChanged(position, it)
        })

        //event listeners
        val cl = View.OnClickListener {
            venueClick(venue, position)
        }

        name.setOnClickListener(cl)
        icon.setOnClickListener(cl)
    }

    override fun getItemCount() = venues.size

}