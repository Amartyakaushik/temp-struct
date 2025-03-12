package com.example.worka1.ui.show_category_details

import CartViewModel
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R
import com.example.worka1.ui.cart.CartActivity
import com.example.worka1.ui.show_category_details.components.ServiceAdapter
import com.example.worka1.ui.home.components.ServiceCategories
import com.example.worka1.ui.show_category_details.components.OnServiceClickListener
import com.example.worka1.ui.show_category_details.components.Subcategory
import com.example.worka1.ui.show_category_details.components.SubcategoryAdapter
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore

class ShowCategoryDetailsActivity : AppCompatActivity(), OnServiceClickListener {
    private lateinit var categoryId: String
    private lateinit var subCategoryId: String
    private lateinit var itemId: String
    private lateinit var servicesList: MutableList<ServiceCategories>
    private lateinit var subCategoriesList: MutableList<Subcategory>
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var servicesRecyclerView: RecyclerView
    private lateinit var subCategoriesRecyclerView: RecyclerView
    private val fb = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid
    private lateinit var cartViewModel: CartViewModel
    private lateinit var floatingCartLayout :View
    private lateinit var cartTotalTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_category_details)
        if (userId == null) {
            Toast.makeText(this, "User not found. Returning to previous screen.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()

        val subCategoriesDetails = intent.getSerializableExtra("sub_categories_details") as? HashMap<*, *>
        if (subCategoriesDetails != null) {
            categoryId = subCategoriesDetails["category_id"].toString()
            subCategoryId = subCategoriesDetails["sub_category_id"].toString()
            itemId = subCategoriesDetails["item_id"].toString()
            initializeViews()

            floatingCartLayout = findViewById(R.id.floating_cart_layout)
            cartTotalTextView = findViewById(R.id.cart_total_text)
            findViewById<Button>(R.id.viewcart).setOnClickListener {
                startActivity(Intent(this, CartActivity::class.java))
            }
            cartViewModel.cartTotal.observe(this) { total ->
                if (total > 0) {
                    floatingCartLayout.visibility = View.VISIBLE
                    cartTotalTextView.text = "₹$total"
                } else {
                    floatingCartLayout.visibility = View.GONE
                }
            }

            if (userId != null) {
                cartViewModel.fetchCartTotal(userId)
            }
            supportActionBar?.title = categoryId

            fetchServices { services ->
                servicesList.clear()
                servicesList.addAll(services)
                servicesRecyclerView.adapter?.notifyDataSetChanged()

                fetchSubcategories()
            }
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun initializeViews() {
        nestedScrollView = findViewById(R.id.category_details_scrollview)
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        servicesList = mutableListOf()
        servicesRecyclerView = findViewById(R.id.services_container)
        servicesRecyclerView.layoutManager = GridLayoutManager(this, 4)
        servicesRecyclerView.adapter = ServiceAdapter(servicesList, this)

        subCategoriesList = mutableListOf()
        subCategoriesRecyclerView = findViewById(R.id.subcategories_container)
        subCategoriesRecyclerView.layoutManager = LinearLayoutManager(this)
        subCategoriesRecyclerView.adapter =
            userId?.let {
                SubcategoryAdapter(subCategoriesList,
                    it, categoryId, cartViewModel, this)
            }
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
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
