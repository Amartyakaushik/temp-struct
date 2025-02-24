package com.example.worka1.ui.cart

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var emptyCartView: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Cart"
        }

        recyclerViewCart = findViewById(R.id.recyclerView_cart)
        emptyCartView = findViewById(R.id.empty_cart_view)

        val cartItems = getCartItems()

        if (cartItems.isNotEmpty()) {
            recyclerViewCart.visibility = View.VISIBLE
            emptyCartView.visibility = View.GONE

            recyclerViewCart.layoutManager = LinearLayoutManager(this)
            recyclerViewCart.adapter = CartAdapter(cartItems)
        } else {
            recyclerViewCart.visibility = View.GONE
            emptyCartView.visibility = View.VISIBLE
        }
    }

    private fun getCartItems(): List<String> {
        return listOf(
//            "Service 1",
//            "Service 2",
//            "Service 3",
//            "Service 4",
//            "Service 5"
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
