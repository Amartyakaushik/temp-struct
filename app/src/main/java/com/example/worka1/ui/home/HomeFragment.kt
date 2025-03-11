package com.example.worka1.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R
import com.example.worka1.ServicesAdapter
import com.example.worka1.databinding.FragmentHomeBinding
import com.example.worka1.ui.cart.CartActivity
import com.example.worka1.ui.home.components.*
import com.example.worka1.ui.location_manager.MapsActivity
import com.example.worka1.ui.search_services.SearchServicesActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingComponent = view.findViewById<ConstraintLayout>(R.id.loading_component)
        val servicesRecyclerView = binding.root.findViewById<RecyclerView>(R.id.services_container)

        val firestore = Firebase.firestore
        val servicesCollection = firestore.collection("services")

        val servicesList = mutableListOf<HomeService>()
        val servicesAdapter = ServicesAdapter(servicesList)
        servicesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        servicesRecyclerView.adapter = servicesAdapter

        fun toggleLoading(isLoading: Boolean) {
            loadingComponent.visibility = if (isLoading) View.VISIBLE else View.GONE
            servicesRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        toggleLoading(true)
        servicesCollection.get()
            .addOnSuccessListener { documents ->
                servicesList.clear()
                for (document in documents) {
                    val service = document.toObject(HomeService::class.java)
                    servicesList.add(service)
                }
                servicesAdapter.notifyDataSetChanged()
                toggleLoading(false)
            }
            .addOnFailureListener { exception ->
                toggleLoading(false)
                exception.printStackTrace()
            }

        val locationIntent = Intent(requireContext(), MapsActivity::class.java)
        val locationContainer = view.findViewById<LinearLayout>(R.id.location_container_home)
        locationContainer.findViewById<LinearLayout>(R.id.locationLayout).setOnClickListener {
            startActivity(locationIntent)
        }

        val cartIntent = Intent(requireContext(), CartActivity::class.java)
        locationContainer.findViewById<LinearLayout>(R.id.cartButtonContainer).setOnClickListener {
            startActivity(cartIntent)
        }

        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.inputType = InputType.TYPE_NULL
        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            val searchIntent = Intent(requireContext(), SearchServicesActivity::class.java)
            startActivity(searchIntent)
        }

        val videoList = listOf(
            VideoItem("https://media.giphy.com/media/kJEK2i63DXne04EYJh/giphy.gif", "Trending Now"),
            VideoItem("https://media.giphy.com/media/f9p9VjrLWO5ag/giphy.gif", "Popular Services"),
            VideoItem("https://media.giphy.com/media/xT9IgzoKnwFNmISR8I/giphy.gif", "Best Rated")
        )

        val feedbackRecyclerView = binding.root.findViewById<RecyclerView>(R.id.feedbackPreviews)
        feedbackRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        feedbackRecyclerView.adapter = VideoAdapter(requireContext(), videoList)

        val newSubCategoriesList = listOf(
            SubCategoryItem("1", "electrician", "switch_socket", "Smart Lights", R.drawable.plumber, 4.5f, 120, "₹1,299"),
            SubCategoryItem("2", "carpenter", "drill_hang", "Roofing", R.drawable.carpenter, 4.2f, 85, "₹2,499"),
            SubCategoryItem("3", "cleaner", "bathroom_fittings", "Bathroom Cleaning", R.drawable.cleaner, 4.8f, 230, "₹3,799"),
            SubCategoryItem("4", "plumber", "drainage_system", "Drainage System", R.drawable.plumber, 4.6f, 150, "₹4,999"),
            SubCategoryItem("5", "painter", "interior_painting", "Interior Painting", R.drawable.painter, 4.3f, 95, "₹899")
        )

        val newSubCategoriesRecyclerView = binding.root.findViewById<RecyclerView>(R.id.newSubCategories)
        newSubCategoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        newSubCategoriesRecyclerView.adapter = SubCategoryItemAdapter(requireContext(), newSubCategoriesList)

        val mostBookedSubCategoryList = listOf(
            SubCategoryItem("1", "labour", "masonry_work", "Masonry Work", R.drawable.labour, 4.5f, 120, "₹1,299"),
            SubCategoryItem("2", "beautician", "male_beautician", "Male Beautician", R.drawable.beautician, 4.2f, 85, "₹2,499"),
            SubCategoryItem("3", "home_decor", "interior_decoration", "Interior Decoration", R.drawable.homedecor, 4.8f, 230, "₹3,799"),
            SubCategoryItem("4", "cleaner", "bathroom_cleaning", "Bathroom Cleaning", R.drawable.cleaner, 4.6f, 150, "₹4,999"),
            SubCategoryItem("5", "carpenter", "roofing", "Roofing", R.drawable.carpenter, 4.3f, 95, "₹899")
        )

        val mostBookedSubCategoryRecyclerView = binding.root.findViewById<RecyclerView>(R.id.mostBookedSubCategories)
        mostBookedSubCategoryRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mostBookedSubCategoryRecyclerView.adapter = SubCategoryItemAdapter(requireContext(), mostBookedSubCategoryList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
