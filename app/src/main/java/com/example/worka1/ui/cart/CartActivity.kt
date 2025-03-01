package com.example.worka1.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R
import com.example.worka1.ui.cart.components.CartItem
import com.example.worka1.ui.cart.components.ServiceItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var emptyCartView: LinearLayout
    private val db = Firebase.firestore

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

        lifecycleScope.launch {
            val cartItems = getCartItems("DH8j7CdzJHioSBFlrPav")

            if (cartItems.isNotEmpty()) {
                hideEmptyCartView()
                recyclerViewCart.layoutManager = LinearLayoutManager(this@CartActivity)
                recyclerViewCart.adapter = CartAdapter(this@CartActivity, cartItems)
            } else {
                showEmptyCartView()
            }
        }
    }

    private suspend fun getCartItems(userId: String): MutableList<CartItem> = withContext(Dispatchers.IO) {
        val list = mutableListOf<CartItem>()

        try {
            val doc = db.collection("users")
                .document(userId)
                .collection("cart")
                .document("current_cart")
                .get()
                .await()

            if (doc.exists()) {
                val data = doc.data ?: return@withContext list
                val service_subcategories = db
                    .collection("service_subcategories")

                for ((serviceId, services) in data) {
                    if (services is Map<*, *>) {
                        val serviceDetails = service_subcategories.document(serviceId).get().await()
                        val svcItemList = mutableListOf<ServiceItem>()
                        val categoryId: String? = services["category_id"] as? String
                        var total = 0
                        var image_url = ""
                        for ((_, serviceItems) in services) {
                            if (serviceItems is Map<*, *>) {
                                val id = serviceItems["id"] as? String ?: "unknown_id"
                                val name = serviceItems["service_name"] as? String ?: "Unknown Service"
                                val count = (serviceItems["items_count"] as? Long)?.toInt() ?: 0
                                val svcItem = ServiceItem(id, name, count)
                                val items = serviceDetails.get("items") as? List<Map<String, Any>>
                                if (items != null) {
                                    image_url = items[0]["imageUrl"].toString()
                                    for (item in items) {
                                        if(item["id"] == id){
                                            total += item["price"].toString().toInt() * count
                                            break
                                        }
                                    }
                                }
                                svcItemList.add(svcItem)
                            }
                        }

                        val cartItem = CartItem(
                            categoryId ?: "unknown_category",
                            serviceId.toString(),
                            serviceDetails.get("name").toString(),
                            image_url,
                            svcItemList.size,
                            total,
                            svcItemList
                        )
                        list.add(cartItem)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("CartActivity", "Error fetching cart items", e)
        }

        return@withContext list
    }

    fun showEmptyCartView() {
        recyclerViewCart.visibility = View.GONE
        emptyCartView.visibility = View.VISIBLE
    }

    private fun hideEmptyCartView() {
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

    fun addToFirebase(serviceId: String, serviceItemsList: MutableList<ServiceItem>, userId: String, categoryId: String) {
        val cartRef = db.collection("users")
            .document(userId)
            .collection("cart")
            .document("current_cart")

        val itemMap = serviceItemsList.associateBy { it.id }

        cartRef.get().addOnSuccessListener { document ->
            val existingItems = if (document.exists()) {
                document.get(serviceId) as? Map<String, Any> ?: emptyMap()
            } else {
                emptyMap()
            }

            val updatedItems = existingItems.toMutableMap().apply {
                putAll(itemMap)
                put("category_id", categoryId)
            }

            cartRef.set(mapOf(serviceId to updatedItems), SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("CartActivity", "Items added successfully with category_id")
                }
                .addOnFailureListener { e ->
                    Log.e("CartActivity", "Error adding items", e)
                }
        }.addOnFailureListener { e ->
            Log.e("CartActivity", "Error fetching existing cart data", e)
        }
    }

    fun removeFromFirebase(itemId: String, serviceId: String, userId: String) {
        val cartRef = db.collection("users")
            .document(userId)
            .collection("cart")
            .document("current_cart")

        cartRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val cartData = document.data ?: return@addOnSuccessListener

                if (!cartData.containsKey(serviceId)) return@addOnSuccessListener

                val serviceData = (cartData[serviceId] as? MutableMap<String, Any>) ?: mutableMapOf()
                if (itemId == "-1" || serviceData.size == 2) {
                    cartRef.update(mapOf(serviceId to FieldValue.delete()))
                } else {
                    serviceData.remove(itemId)
                    cartRef.update(mapOf(serviceId to serviceData))
                }
            }
        }.addOnFailureListener { e ->
            Log.e("CartActivity", "Error fetching cart data", e)
        }
    }
}
