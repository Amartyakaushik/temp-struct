package com.example.worka1.ui.show_category_details

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R
import com.example.worka1.ui.show_category_details.components.ServiceAdapter
import com.example.worka1.ui.home.components.ServiceCategories
import com.example.worka1.ui.show_category_details.components.OnServiceClickListener
import com.example.worka1.ui.show_category_details.components.Subcategory
import com.example.worka1.ui.show_category_details.components.SubcategoryAdapter
import com.example.worka1.ui.show_category_details.components.SubcategoryItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ShowCategoryDetailsActivity : AppCompatActivity(), OnServiceClickListener {
    private lateinit var categoryId: String
    private lateinit var subCategoryId: String
    private lateinit var itemId: String
    private lateinit var servicesList: MutableList<ServiceCategories>
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var subCategoriesRecyclerView: RecyclerView
    private lateinit var subCategoriesList: MutableList<Subcategory>
    private lateinit var servicesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_category_details)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        supportActionBar?.show()
        val subCategoriesDetails = intent.getSerializableExtra("sub_categories_details") as? HashMap<*, *>
        val fb = Firebase.firestore

        if (subCategoriesDetails != null) {
            categoryId = subCategoriesDetails["category_id"].toString()
            subCategoryId = subCategoriesDetails["sub_category_id"].toString()
            itemId = subCategoriesDetails["item_id"].toString()

            val subcategoriesSerialized = intent.getSerializableExtra("sub_categories")
            if (subcategoriesSerialized != null) {
                servicesList = subcategoriesSerialized as MutableList<ServiceCategories>
            }
            else {
                servicesList = mutableListOf()
                fb.collection("services").document(categoryId)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val subCategoriesArray = documentSnapshot.get("subCategories") as? List<Map<String, Any>>
                            if (subCategoriesArray != null) {
                                for (item in subCategoriesArray) {
                                    val service = ServiceCategories(
                                        item["id"].toString(),
                                        item["name"].toString(),
                                        item["image"].toString()
                                    )
                                    servicesList.add(service)
                                }
                            }
                        }
                        runOnUiThread {
                            servicesRecyclerView.adapter = ServiceAdapter(servicesList, this)
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreError", "Error fetching subcategories", e)
                    }
            }

            supportActionBar?.title = categoryId

            servicesRecyclerView = findViewById(R.id.services_container)
            val layoutManager = GridLayoutManager(this, 4)
            servicesRecyclerView.layoutManager = layoutManager
            val servicesAdapter = ServiceAdapter(servicesList, this)
            servicesRecyclerView.adapter = servicesAdapter

            nestedScrollView = findViewById(R.id.category_details_scrollview)

            subCategoriesList = mutableListOf()
            val sc = fb.collection("service_subcategories")

            for (service in servicesList) {
                sc.document(service.id).get()
                    .addOnSuccessListener { documentSnapshot ->
                        documentSnapshot.toObject(Subcategory::class.java)?.let { subcategory ->
                            subCategoriesList.add(subcategory)
                            runOnUiThread {
                                subCategoriesRecyclerView.adapter?.notifyDataSetChanged()
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreError", "Error fetching subcategory", e)
                    }
            }

            subCategoriesRecyclerView = findViewById(R.id.subcategories_container)
            subCategoriesRecyclerView.setHasFixedSize(true)
            subCategoriesRecyclerView.isNestedScrollingEnabled = false
            subCategoriesRecyclerView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            subCategoriesRecyclerView.requestLayout()
            val subCategoriesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            subCategoriesRecyclerView.layoutManager = subCategoriesLayoutManager
            val subCategoriesAdapter = SubcategoryAdapter(subCategoriesList)
            subCategoriesRecyclerView.adapter = subCategoriesAdapter
            subCategoriesRecyclerView.post {
                onServiceClick(subCategoryId)
            }
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
        val position = subCategoriesList.indexOfFirst { it.id == subcategoryId }
        if (position != -1) {
            subCategoriesRecyclerView.smoothScrollToPosition(position)
            val viewHolder = subCategoriesRecyclerView.findViewHolderForAdapterPosition(position)
            viewHolder?.itemView?.let { itemView ->
                val targetY = itemView.top + subCategoriesRecyclerView.top
                ObjectAnimator.ofInt(nestedScrollView, "scrollY", nestedScrollView.scrollY, targetY)
                    .apply { duration = 800; start() }
            }
        }

    }
}

