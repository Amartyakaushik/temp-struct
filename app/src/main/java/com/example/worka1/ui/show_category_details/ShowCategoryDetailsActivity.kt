package com.example.worka1.ui.show_category_details

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.worka1.R

class ShowCategoryDetailsActivity : AppCompatActivity() {
    lateinit var categoryId: String
    lateinit var subCategoryId: String
    lateinit var itemId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_category_details)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val subCategoriesDetails =
            intent.getSerializableExtra("sub_categories_details") as? HashMap<*, *>
        val textViewCategory = findViewById<TextView>(R.id.textView_category)
        val textViewSubCategory = findViewById<TextView>(R.id.textView_sub_category)
        val textViewItem = findViewById<TextView>(R.id.textView_item)

        if (subCategoriesDetails != null) {
            categoryId = subCategoriesDetails["category_id"].toString()
            subCategoryId = subCategoriesDetails["sub_category_id"].toString()
            itemId = subCategoriesDetails["item_id"].toString()
            textViewCategory.text = "Category ID: $categoryId"
            textViewSubCategory.text = "Sub-Category ID: $subCategoryId"
            textViewItem.text = "Item ID: $itemId"
        } else {
            textViewCategory.text = "No category details available"
            textViewSubCategory.text = ""
            textViewItem.text = ""
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            title = categoryId
        }
        supportActionBar?.show()
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
}
