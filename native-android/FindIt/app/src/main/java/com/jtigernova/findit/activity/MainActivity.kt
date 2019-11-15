package com.jtigernova.findit.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jtigernova.findit.Constants.CITY_CENTER_GPS
import com.jtigernova.findit.Nav
import com.jtigernova.findit.R
import com.jtigernova.findit.model.Venue
import com.jtigernova.findit.model.VenueCategory
import com.jtigernova.findit.model.VenueCategoryIcon
import com.jtigernova.findit.model.VenueLocation
import com.jtigernova.findit.persistence.Prefs
import com.jtigernova.findit.repository.DataRepository
import com.jtigernova.findit.view.VenueItemAdapter
import com.jtigernova.findit.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: VenueItemAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var favViewModel: FavoriteViewModel

    private var currentItemPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        fab.hide()

        initViewModels()
        initResults()
    }

    private fun initViewModels() {
        favViewModel = DataRepository.favoriteViewModel

        favViewModel.favoriteVenues.value = Prefs.getFavoriteVenues(this)

        favViewModel.favoriteVenues.observe(this, Observer<MutableSet<String>> {
            Prefs.saveFavoriteVenues(this, it)

            if (currentItemPosition < 0 ||
                    (currentItemPosition >= viewAdapter.itemCount)) return@Observer

            //use post() to avoid state issues with recyclerView
            recyclerView.post {
                viewAdapter.notifyItemChanged(currentItemPosition)
            }
        })
    }

    private fun initResults() {

        val mockIcon = VenueCategoryIcon("https://www.google.com/images/branding/" +
                "googlelogo/2x/googlelogo_color_272x92dp", ".png")

        val venues = arrayListOf(
                Venue(id = "0", name = "Bob's", url = "https://google.com",
                        categories = listOf(
                                VenueCategory(id = "0", name = "Test", shortName = "Testtest",
                                        pluralName = "Tests", primary = true, icon = mockIcon)),
                        location = VenueLocation(address = "123 lane", lat = 30.2747,
                                lng = -97.7404, postalCode = "23231", cc = "", city = "Austin",
                                state = "Texas", country = "USA",
                                formattedAddress = listOf("123 land"))),
                Venue(id = "1", name = "Tim Burger Shop", url = "https://google.com",
                        categories = listOf(
                                VenueCategory(id = "1", name = "Test", shortName = "Testtest",
                                        pluralName = "Tests", primary = true, icon = mockIcon)),
                        location = VenueLocation(address = "123 lane", lat = 30.2637675,
                                lng = -97.7380349, postalCode = "23231", cc = "", city = "Austin",
                                state = "Texas", country = "USA",
                                formattedAddress = listOf("1001 1st Ave (at Madison St)",
                                        "Seattle, WA 98104",
                                        "United States"))))

        for (venue in venues) {
            venue.location?.calculateDistanceFromCityCenter(CITY_CENTER_GPS)
        }

        viewManager = LinearLayoutManager(this@MainActivity)

        recyclerView = findViewById<RecyclerView>(R.id.results).apply {
            //content changes do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            layoutManager = viewManager

            adapter = getAdapter(venues)
        }
    }

    private fun venueClick(): (venue: Venue, viewPosition: Int) -> Unit {
        return { venue: Venue, viewPosition: Int ->
            currentItemPosition = viewPosition

            Nav.venueDetails(context = this, venue = venue)
        }
    }

    private fun getAdapter(venues: ArrayList<Venue>): RecyclerView.Adapter<*> {
        viewAdapter = VenueItemAdapter(context = this@MainActivity, venues = venues,
                favoriteViewModel = favViewModel,
                favoriteVenueIds = favViewModel.favoriteVenues.value!!,
                venueClick = venueClick())

        if (venues.any()) {
            fab.setOnClickListener {
                Nav.venuesMap(context = this@MainActivity, venues = venues)
            }

            fab.show()
        } else {
            fab.hide()
            fab.setOnClickListener(null)
        }

        return viewAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }
}
