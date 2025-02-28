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
import com.example.worka1.ui.cart.components.CartItem
import com.example.worka1.ui.cart.components.ServiceItem

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
            title = "My Cart"
        }

        recyclerViewCart = findViewById(R.id.recyclerView_cart)
        emptyCartView = findViewById(R.id.empty_cart_view)

        val cartItems = getCartItems()

        if (cartItems.isNotEmpty()) {
            hideEmptyCartView()

            recyclerViewCart.layoutManager = LinearLayoutManager(this)
            recyclerViewCart.adapter = CartAdapter(cartItems)
        } else {
            showEmptyCartView()
        }
    }

    private fun getCartItems(): MutableList<CartItem> {
        return mutableListOf(
            CartItem(
                "carpenter",
                "Carpenter",
                "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_56,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/home-screen/1678868062337-08bfc2.jpeg",
                1,
                1000,
                mutableListOf(
                    ServiceItem("cupboard_lock_repair", "Cupboard Lock Repair", 2),
                    ServiceItem("cupboard_lock_repair", "Cupboard Lock Repair", 2),
                )
            )
        )
    }
    fun showEmptyCartView() {
        recyclerViewCart.visibility = View.GONE
        emptyCartView.visibility = View.VISIBLE
    }
    fun hideEmptyCartView() {
        recyclerViewCart.visibility = View.VISIBLE
        emptyCartView.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
