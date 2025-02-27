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
            val layoutManager = GridLayoutManager(this, 4)
            servicesRecyclerView.layoutManager = layoutManager
            val servicesAdapter = ServiceAdapter(servicesList, this)
            servicesRecyclerView.adapter = servicesAdapter

            nestedScrollView = findViewById(R.id.category_details_scrollview)

            subCategoriesList = listOf(
                Subcategory("clothes_hanger","Clothes hanger", listOf(
                    SubcategoryItem("ceiling_mounted_hanger_installation", "Ceiling-mounted hanger installation", 4.84f, 15000, 100, "1 hour 30 mins", "", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_128,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1714032579918-778d26.jpeg"),
                    SubcategoryItem("wall_door_hanger_installation", "Wall/door hanger installation", 4.83f, 7000, 199, "30 mins", "", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_128,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1714032420379-ae5eb1.jpeg"),
                )),
                Subcategory("bed","Bed", listOf(
                    SubcategoryItem("bed_support_repair", "Bed support repair", 4.86f, 18000, 459, "60 mins", "", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_128,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1714032585514-9362d7.jpeg"),
                    SubcategoryItem("bed_legs_headboard_repair", "Bed legs/Headboard repair", 4.85f, 17000, 299, "60 mins", "", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_128,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726807500155-968bcb.jpeg"),
                )),
                Subcategory("cupboard_drawer", "Cupboard & drawer", listOf(
                    SubcategoryItem("cupboard_hinge_installation_upto_2", "Cupboard hinge installation (upto 2)", 4.84f, 61000, 199, "60 mins", "", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_128,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1714032573487-455006.jpeg"),
                    SubcategoryItem("channel_repair_one_set", "Channel repair (one set)", 4.85f, 36000, 178, "30 mins", "", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_128,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1714032515421-0c373e.jpeg"),
                    SubcategoryItem("drawer_channel_replacement_one_set", "Drawer/Channel replacement (one set)", 4.85f, 16000, 239, "30 mins", "", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_128,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1714032512040-b76f31.jpeg"),
                    SubcategoryItem("cupboard_handle_installation_replacement", "Cupboard handle installation/replacement", 4.84f, 16000, 119, "30 mins", "", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_128,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726806353954-1bfeb1.jpeg"),
                    SubcategoryItem("cupboard_lock_installation", "Cupboard lock installation", 4.81f, 4000, 449, "30 mins", "", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_128,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726806570495-e264db.jpeg"),
                    SubcategoryItem("cupboard_lock_repair", "Cupboard lock repair", 4.83f, 6000, 189, "30 mins", "", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_128,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726806779316-a69960.jpeg")
                ))
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

