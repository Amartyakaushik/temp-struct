package com.example.worka1.ui.show_category_details

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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

class ShowCategoryDetailsActivity : AppCompatActivity(), OnServiceClickListener {
    private lateinit var categoryId: String
    private lateinit var subCategoryId: String
    private lateinit var itemId: String
    private lateinit var servicesList: MutableList<ServiceCategories>
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var subCategoriesRecyclerView: RecyclerView
    private lateinit var subCategoriesList: List<Subcategory>
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
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        supportActionBar?.show()
        val subCategoriesDetails = intent.getSerializableExtra("sub_categories_details") as? HashMap<*, *>

        if (subCategoriesDetails != null) {
            categoryId = subCategoriesDetails["category_id"].toString()
            subCategoryId = subCategoriesDetails["sub_category_id"].toString()
            itemId = subCategoriesDetails["item_id"].toString()
            servicesList = intent.getSerializableExtra("sub_categories") as MutableList<ServiceCategories>
            supportActionBar?.title = categoryId

            servicesRecyclerView = findViewById(R.id.services_container)
            val layoutManager = GridLayoutManager(this, 3)
            servicesRecyclerView.layoutManager = layoutManager
            val servicesAdapter = ServiceAdapter(servicesList, this)
            servicesRecyclerView.adapter = servicesAdapter

            nestedScrollView = findViewById(R.id.category_details_scrollview)

            subCategoriesList = listOf(
                Subcategory("clothes_hanger","clothes hanger 1", listOf(
                    SubcategoryItem("Item 1", 4.5f, 100, 100, 60, "Description 1", "https://picsum.photos/200/300"),
                    SubcategoryItem("Item 2", 4.5f, 100, 100, 60, "Description 2", "https://picsum.photos/200/300"),
                    SubcategoryItem("Item 3", 4.5f, 100, 100, 60, "Description 3", "https://picsum.photos/200/300"),
                    SubcategoryItem("Item 4", 4.5f, 100, 100, 60, "Description 4", "https://picsum.photos/200/300"),
                    SubcategoryItem("Item 5", 4.5f, 100, 100, 60, "Description 5", "https://picsum.photos/200/300"),
                    SubcategoryItem("Item 6", 4.5f, 100, 100, 60, "Description 6", "https://picsum.photos/200/300"),
                    SubcategoryItem("Item 7", 4.5f, 100, 100, 60, "Description 7", "https://picsum.photos/200/300"),
                    SubcategoryItem("Item 8", 4.5f, 100, 100, 60, "Description 8", "https://picsum.photos/200/300"),
                )),
                Subcategory("bed","clothes hanger 2", listOf(
                    SubcategoryItem("Item 1", 4.5f, 100, 100, 60, "Description 1", "https://picsum.photos/200/300"),
                    SubcategoryItem("Item 2", 4.5f, 100, 100, 60, "Description 2", "https://picsum.photos/200/300"),
                    SubcategoryItem("Item 3", 4.5f, 100, 100, 60, "Description 3", "https://picsum.photos/200/300"),
                    SubcategoryItem("Item 4", 4.5f, 100, 100, 60, "Description 4", "https://picsum.photos/200/300"),
                    SubcategoryItem("Item 5", 4.5f, 100, 100, 60, "Description 5", "https://picsum.photos/200/300"),
                    SubcategoryItem("Item 6", 4.5f, 100, 100, 60, "Description 6", "https://picsum.photos/200/300"),
                    SubcategoryItem("Item 7", 4.5f, 100, 100, 60, "Description 7", "https://picsum.photos/200/300"),
                    SubcategoryItem("Item 8", 4.5f, 100, 100, 60, "Description 8", "https://picsum.photos/200/300"),
                )),
            )
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

