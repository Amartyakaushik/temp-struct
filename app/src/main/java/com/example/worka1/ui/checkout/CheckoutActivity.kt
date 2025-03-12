package com.example.worka1.ui.checkout

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R
import com.example.worka1.ui.checkout.components.CheckoutAdapter
import com.example.worka1.ui.checkout.components.CheckoutItems
import com.example.worka1.ui.checkout.components.FATAdapter
import com.example.worka1.ui.checkout.components.FATItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CheckoutActivity : AppCompatActivity() {
    lateinit var recyclerViewItems: RecyclerView
    private lateinit var recyclerViewFATItems: RecyclerView
    lateinit var checkoutItems: MutableList<CheckoutItems>
    lateinit var fatItems: MutableList<FATItem>
    lateinit var serviceId: String
    lateinit var userId: String
    private val db = Firebase.firestore
    private var itemTotal = 0
    private var taxes = 90

    private lateinit var totalContainer: TextView
    private lateinit var taxesContainer: TextView
    private lateinit var overallTotalContainer: TextView
    private lateinit var payableTotalContainer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)

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
            title = "Checkout"
        }

        recyclerViewItems = findViewById(R.id.recyclerView_items)
        recyclerViewFATItems = findViewById(R.id.recyclerView_fat_items)

        totalContainer = findViewById(R.id.item_total_checkout)
        taxesContainer = findViewById(R.id.taxes_checkout)
        overallTotalContainer = findViewById(R.id.overall_total_checkout)
        payableTotalContainer = findViewById(R.id.payable_total_checkout)

        userId = intent.getStringExtra("user_id").toString()
        serviceId = intent.getStringExtra("service_id").toString()
        if (userId.isBlank()) {
            Toast.makeText(this, "User not logged in. Cannot proceed.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        lifecycleScope.launch {
            checkoutItems = getCheckoutItems(userId, serviceId)
            val adapter = CheckoutAdapter(this@CheckoutActivity, checkoutItems, userId, serviceId) {
                updateTotal()
            }
            recyclerViewItems.layoutManager = LinearLayoutManager(this@CheckoutActivity)
            recyclerViewItems.adapter = adapter

            updateTotal()
        }

        lifecycleScope.launch {
            fatItems = getFatItems(serviceId)
            val fatAdapter = FATAdapter(this@CheckoutActivity, fatItems, serviceId) {
                updateTotal()
            }
            recyclerViewFATItems.layoutManager = LinearLayoutManager(this@CheckoutActivity, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewFATItems.adapter = fatAdapter
        }
    }

    private fun updateTotal() {
        itemTotal = checkoutItems.sumOf { it.price * it.count }
        val overallTotal = itemTotal + taxes

        totalContainer.text = "₹$itemTotal"
        taxesContainer.text = "₹$taxes"
        overallTotalContainer.text = "₹$overallTotal"
        payableTotalContainer.text = "₹$overallTotal"
    }

    private suspend fun getCheckoutItems(userId: String, serviceId: String): MutableList<CheckoutItems> = withContext(Dispatchers.IO) {
        val list = mutableListOf<CheckoutItems>()

        try {
            val doc = db.collection("users")
                .document(userId)
                .collection("cart")
                .document("current_cart")
                .get()
                .await()

            if (doc.exists()) {
                val data = doc.data ?: return@withContext list
                val serviceSubcategories = db.collection("service_subcategories")

                val services = data[serviceId]
                if (services is Map<*, *>) {
                    val serviceDetails = serviceSubcategories.document(serviceId).get().await()
                    val items = serviceDetails.get("items") as? List<Map<String, Any>> ?: emptyList()

                    for ((_, serviceItems) in services) {
                        if (serviceItems is Map<*, *>) {
                            val id = serviceItems["id"] as? String ?: continue
                            val name = serviceItems["service_name"] as? String ?: "Unknown Service"
                            val count = (serviceItems["items_count"] as? Long)?.toInt() ?: 0
                            var price = 0

                            for (item in items) {
                                if (item["id"] == id) {
                                    price = item["price"].toString().toInt()
                                    break
                                }
                            }

                            list.add(CheckoutItems(id, name, count, price, serviceId, userId))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("CheckoutActivity", "Error fetching checkout items", e)
        }

        return@withContext list
    }

    private suspend fun getFatItems(serviceId: String): MutableList<FATItem> = withContext(Dispatchers.IO) {
        val list = mutableListOf<FATItem>()
        try {
            val serviceSubcategories = db.collection("service_subcategories")
            val serviceDetails = serviceSubcategories.document(serviceId).get().await()
            val items = serviceDetails.get("items") as? List<Map<String, Any>> ?: emptyList()
            for (item in items) {
                val id = item["id"] as? String ?: continue
                val name = item["itemName"] as? String ?: "Unknown Item"
                val price = item["price"].toString().toInt()
                val imageUrl = item["imageUrl"] as? String ?: ""
                list.add(FATItem(id, name, imageUrl, price))
            }
        } catch (e: Exception) {
            Log.e("CheckoutActivity", "Error fetching fat items", e)
        }
        return@withContext list
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
