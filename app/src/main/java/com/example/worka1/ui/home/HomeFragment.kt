package com.example.worka1.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

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
        lifecycleScope.launch {
            val videoList = fetchVideo()
            val feedbackRecyclerView = binding.root.findViewById<RecyclerView>(R.id.feedbackPreviews)
            feedbackRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            feedbackRecyclerView.adapter = VideoAdapter(requireContext(), videoList)
        }

        lifecycleScope.launch {
            val newSubCategoriesList = fetchSubcategories("new")
            val newSubCategoriesRecyclerView = binding.root.findViewById<RecyclerView>(R.id.newSubCategories)
            newSubCategoriesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            newSubCategoriesRecyclerView.adapter = SubCategoryItemAdapter(requireContext(), newSubCategoriesList)
        }

        lifecycleScope.launch {
            val mostBookedSubCategoryList = fetchSubcategories("trending")
            val mostBookedSubCategoryRecyclerView = binding.root.findViewById<RecyclerView>(R.id.mostBookedSubCategories)
            mostBookedSubCategoryRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            mostBookedSubCategoryRecyclerView.adapter = SubCategoryItemAdapter(requireContext(), mostBookedSubCategoryList)
        }
    }

    private suspend fun fetchVideo(): List<VideoItem> = withContext(Dispatchers.IO) {
        val list = mutableListOf<VideoItem>()
        try {
            val querySnapshot = db.collection("misc").document("videos").get().await()
            for (document in querySnapshot.data?.values.orEmpty()) {
                val data = document as? Map<*, *> ?: continue
                val video_url = data["videoUrl"] as? String ?: continue
                val video_name = data["title"] as? String ?: continue
                list.add(VideoItem(video_url, video_name))
            }
        }
        catch (e: Exception){
         e.printStackTrace()
        }
        return@withContext list
    }
    private suspend fun fetchSubcategories(type: String): List<SubCategoryItem> = withContext(Dispatchers.IO) {
        val list = mutableListOf<SubCategoryItem>()
        try {
            val querySnapshot = db.collection("misc").document(type).get().await()
            val subcategoriesSnapshot = db.collection("service_subcategories")

            for (document in querySnapshot.data?.values.orEmpty()) {
                val data = document as? Map<*, *> ?: continue
                val item_id = data["item_id"] as? String ?: continue
                val subcategory_id = data["subcategory_id"] as? String ?: continue

                val subcategory_map = subcategoriesSnapshot.document(subcategory_id).get().await()
                val subcategory_data = subcategory_map.data ?: continue

                val items = subcategory_data["items"] as? List<*> ?: continue  // FIX: Convert to List

                val item_map = items.find {
                    (it as? Map<*, *>)?.get("id") == item_id
                } as? Map<*, *> ?: continue

                val category_id = subcategory_data["category_id"] as? String ?: continue
                val item_name = item_map["itemName"] as? String ?: continue
                val item_image = item_map["imageUrl"] as? String ?: continue

                val item_price = item_map["price"]?.toString()?.toIntOrNull() ?: 0
                val item_rating = item_map["ratingCount"]?.toString()?.toFloatOrNull() ?: 0.0f
                val item_rating_count = item_map["reviewsCount"]?.toString()?.toIntOrNull() ?: 0  // FIX: Correct key

                list.add(
                    SubCategoryItem(
                        item_id, category_id, subcategory_id, item_name, item_image,
                        item_rating, item_rating_count, item_price
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
