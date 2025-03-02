package com.example.worka1.ui.services

import CartViewModel
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R
import com.example.worka1.ui.cart.CartActivity
import com.example.worka1.ui.home.components.ServiceCategories
import com.example.worka1.ui.show_category_details.components.OnServiceClickListener
import com.example.worka1.ui.show_category_details.components.ServiceAdapter
import com.example.worka1.ui.show_category_details.components.Subcategory
import com.example.worka1.ui.show_category_details.components.SubcategoryAdapter
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore

class ServicesFragment : Fragment(), OnServiceClickListener {
    private lateinit var categoryId: String
    private lateinit var subCategoryId: String
    private lateinit var itemId: String
    private lateinit var servicesList: MutableList<ServiceCategories>
    private lateinit var subCategoriesList: MutableList<Subcategory>
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var servicesRecyclerView: RecyclerView
    private lateinit var subCategoriesRecyclerView: RecyclerView
    private val fb = Firebase.firestore
    private val userId = "DH8j7CdzJHioSBFlrPav" // test user id
    private lateinit var cartViewModel: CartViewModel
    private lateinit var floatingCartLayout :View
    private lateinit var cartTotalTextView: TextView
    companion object {
        fun newInstance() = ServicesFragment()
    }

    private val viewModel: ServicesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_services, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            show()
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        categoryId = "carpenter"
        subCategoryId = ""
        itemId = ""
        initializeViews(view)

        floatingCartLayout = view.findViewById(R.id.floating_cart_layout)
        cartTotalTextView = view.findViewById(R.id.cart_total_text)
        view.findViewById<Button>(R.id.viewcart).setOnClickListener {
            startActivity(Intent(requireContext(), CartActivity::class.java))
        }
        cartViewModel.cartTotal.observe(viewLifecycleOwner) { total ->
            if (total > 0) {
                floatingCartLayout.visibility = View.VISIBLE
                cartTotalTextView.text = "₹$total"
            } else {
                floatingCartLayout.visibility = View.GONE
            }
        }

        cartViewModel.fetchCartTotal(userId)
        fetchServices { services ->
            servicesList.clear()
            servicesList.addAll(services)
            servicesRecyclerView.adapter?.notifyDataSetChanged()

            fetchSubcategories()
        }

        return view
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().popBackStack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeViews(view: View) {
        nestedScrollView = view.findViewById(R.id.category_details_scrollview)
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        servicesList = mutableListOf()
        servicesRecyclerView = view.findViewById(R.id.services_container)
        servicesRecyclerView.layoutManager = GridLayoutManager(view.context, 4)
        servicesRecyclerView.adapter = ServiceAdapter(servicesList, this)

        subCategoriesList = mutableListOf()
        subCategoriesRecyclerView = view.findViewById(R.id.subcategories_container)
        subCategoriesRecyclerView.layoutManager = LinearLayoutManager(view.context)
        subCategoriesRecyclerView.adapter = SubcategoryAdapter(subCategoriesList, userId, categoryId, cartViewModel, this)
    }

    private fun fetchServices(callback: (List<ServiceCategories>) -> Unit) {
        fb.collection("services").document(categoryId).get()
            .addOnSuccessListener { documentSnapshot ->
                val tempList = mutableListOf<ServiceCategories>()
                if (documentSnapshot.exists()) {
                    val subCategoriesArray = documentSnapshot.get("subCategories") as? List<Map<String, Any>>
                    subCategoriesArray?.forEach { item ->
                        val service = ServiceCategories(
                            item["id"]?.toString() ?: "",
                            item["name"]?.toString() ?: "Unknown",
                            item["image"]?.toString() ?: ""
                        )
                        tempList.add(service)
                    }
                }
                callback(tempList)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error fetching services", e)
                callback(emptyList())
            }
    }

    private fun fetchSubcategories() {
        val sc = fb.collection("service_subcategories")

        val tasks = servicesList.map { service ->
            sc.document(service.id).get()
        }

        Tasks.whenAllSuccess<DocumentSnapshot>(tasks)
            .addOnSuccessListener { documents ->
                val fetchedSubcategories = documents.mapNotNull { it.toObject(Subcategory::class.java) }
                subCategoriesList.clear()
                subCategoriesList.addAll(fetchedSubcategories)
                subCategoriesRecyclerView.adapter?.notifyDataSetChanged()
                onServiceClick(subCategoryId)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error fetching subcategories", e)
            }
    }

    override fun onServiceClick(subcategoryId: String) {
        subCategoriesRecyclerView.postDelayed({
            val position = subCategoriesList.indexOfFirst { it.id == subcategoryId }
            if (position != -1) {
                subCategoriesRecyclerView.smoothScrollToPosition(position)
                subCategoriesRecyclerView.postDelayed({
                    val viewHolder = subCategoriesRecyclerView.findViewHolderForAdapterPosition(position)
                    viewHolder?.itemView?.let { itemView ->
                        val targetY = itemView.top + subCategoriesRecyclerView.top
                        ObjectAnimator.ofInt(nestedScrollView, "scrollY", nestedScrollView.scrollY, targetY)
                            .apply { duration = 800; start() }
                    }
                }, 500)
            }
        }, 1000)
    }
}