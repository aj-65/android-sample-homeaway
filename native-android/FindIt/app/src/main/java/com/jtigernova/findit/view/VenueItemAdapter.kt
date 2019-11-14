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
import com.jtigernova.findit.Nav
import com.jtigernova.findit.R
import com.jtigernova.findit.api.FourSq
import com.jtigernova.findit.ext.loadImgInto
import com.jtigernova.findit.ext.updateFavoriteText
import com.jtigernova.findit.model.Venue
import com.jtigernova.findit.viewmodel.FavoriteViewModel

class VenueItemAdapter(private val context: Context, private val venues: List<Venue>,
                       private val favoriteVenueIds: Set<String>,
                       private val api: FourSq, private val favoriteViewModel: FavoriteViewModel) :
        RecyclerView.Adapter<VenueItemAdapter.ViewHolder>() {
    class ViewHolder(val view: LinearLayout) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_venue_list_item, parent, false) as LinearLayout


        return ViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val venue = venues[position]
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

        fav.isChecked = favoriteVenueIds.contains(venue.id)
        fav.updateFavoriteText(context)

        //event listeners
        with(venueClick(venue)) {
            name.setOnClickListener(this)
            icon.setOnClickListener(this)
        }

        fav.setOnCheckedChangeListener { p0, checked ->
            if (checked) {
                favoriteViewModel.addFavoriteVenue(venue)
            } else {
                favoriteViewModel.removeFavoriteVenue(venue)
            }

            fav.updateFavoriteText(context)
        }
    }

    private fun venueClick(venue: Venue): (View) -> Unit {
        return {
            Nav.venueDetails(context, venue)
        }
    }

    override fun getItemCount() = venues.size

}