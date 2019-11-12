package com.jtigernova.findit.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jtigernova.findit.R
import com.jtigernova.findit.data.Venue
import kotlinx.android.synthetic.main.activity_venue_detail.*

class VenueDetailActivity : AppCompatActivity() {

    private lateinit var name: TextView
    private lateinit var category: TextView
    private lateinit var address: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val venue = intent.getParcelableExtra<Venue>(Venue::class.java.name)

        initDetail(venue)
    }

    private fun initDetail(venue: Venue?) {
        name = findViewById(R.id.name)
        category = findViewById(R.id.category)
        address = findViewById(R.id.address)

        name.text = venue?.name
        category.text = venue?.mainCategory ?: "Unknown"

        val sb = StringBuilder()

        for (line in venue?.location?.formattedAddress!!) {
            sb.appendln(line)
        }

        address.text = sb
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
