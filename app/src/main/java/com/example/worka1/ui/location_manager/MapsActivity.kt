package com.example.worka1.ui.location_manager

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.worka1.databinding.ActivityMapsBinding
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.example.worka1.BuildConfig

class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var placesClient: PlacesClient

    private val searchHistory = mutableListOf<String>()
    private val searchResults = mutableListOf<String>()
    private val placeDetailsMap = mutableMapOf<String, String>()

    private lateinit var historyAdapter: ArrayAdapter<String>
    private lateinit var resultsAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Places.initialize(applicationContext, BuildConfig.PLACES_API_KEY)
        placesClient = Places.createClient(this)

        sharedPreferences = getSharedPreferences("search_history", MODE_PRIVATE)

        setupAdapters()
        loadSearchHistory()
        setupSearchListener()
    }

    private fun setupAdapters() {
        historyAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, searchHistory)
        resultsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, searchResults)

        binding.historyListView.adapter = historyAdapter
        binding.searchResultsListView.adapter = resultsAdapter

        binding.historyListView.setOnItemClickListener { _, _, position, _ ->
            val selectedPlace = searchHistory[position]
            saveSearchHistory(selectedPlace)
            binding.searchView.setQuery(selectedPlace, false)
            fetchPlaceDetails(selectedPlace)
        }

        binding.searchResultsListView.setOnItemClickListener { _, _, position, _ ->
            val selectedPlace = searchResults[position]
            saveSearchHistory(selectedPlace)
            fetchPlaceDetails(selectedPlace)
            finish()
        }
    }

    private fun setupSearchListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    saveSearchHistory(it)
                    binding.searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    showHistory()
                } else {
                    fetchPlaceSuggestions(newText)
                }
                return true
            }
        })
    }

    private fun fetchPlaceSuggestions(query: String) {
        val request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                searchResults.clear()
                placeDetailsMap.clear()
                response.autocompletePredictions.forEach { prediction ->
                    val placeName = prediction.getFullText(null).toString()
                    searchResults.add(placeName)
                    placeDetailsMap[placeName] = prediction.placeId
                }
                showSearchResults()
            }
    }

    private fun fetchPlaceDetails(placeName: String) {
        val placeId = placeDetailsMap[placeName] ?: return
        val placeFields = listOf(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS_COMPONENTS)
        val request = FetchPlaceRequest.builder(placeId, placeFields).build()

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                val place = response.place
                val name = place.name ?: "Unknown"
                val latLng = place.latLng
                val latitude = latLng?.latitude ?: 0.0
                val longitude = latLng?.longitude ?: 0.0

                var city = "Unknown"
                var state = "Unknown"

                place.addressComponents?.asList()?.forEach { component ->
                    when {
                        component.types.contains("locality") -> city = component.name
                        component.types.contains("administrative_area_level_1") -> state = component.name
                    }
                }

                val formattedLocation = "$name, $city, $state"

                sharedPreferences.edit()
                    .putString("last_selected_location", formattedLocation)
                    .apply()

                Toast.makeText(this, "Location saved: $formattedLocation", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch place details", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveSearchHistory(placeName: String) {
        if (!searchHistory.contains(placeName)) {
            searchHistory.add(0, placeName)
            sharedPreferences.edit().putStringSet("history", searchHistory.toSet()).apply()
        }
        historyAdapter.notifyDataSetChanged()
    }

    private fun loadSearchHistory() {
        searchHistory.clear()
        searchHistory.addAll(sharedPreferences.getStringSet("history", emptySet()) ?: emptySet())
        historyAdapter.notifyDataSetChanged()
        showHistory()
    }

    private fun showHistory() {
        binding.historyListView.visibility = View.VISIBLE
        binding.historyLabel.visibility = View.VISIBLE
        binding.searchResultsListView.visibility = View.GONE
        binding.searchResultsLabel.visibility = View.GONE
    }

    private fun showSearchResults() {
        binding.historyListView.visibility = View.GONE
        binding.historyLabel.visibility = View.GONE
        binding.searchResultsListView.visibility = View.VISIBLE
        binding.searchResultsLabel.visibility = View.VISIBLE
        resultsAdapter.notifyDataSetChanged()
    }
}
