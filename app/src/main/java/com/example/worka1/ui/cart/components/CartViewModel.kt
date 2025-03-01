import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CartViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _cartTotal = MutableLiveData(0)
    val cartTotal: LiveData<Int> get() = _cartTotal

    private val _itemCount = MutableLiveData<Map<String, Int>>()
    val itemCount: LiveData<Map<String, Int>> get() = _itemCount

    // Fetch total price
    fun fetchCartTotal(userId: String) {
        viewModelScope.launch {
            val total = calculateCartTotal(userId)
            _cartTotal.value = total
        }
    }

    private suspend fun calculateCartTotal(userId: String): Int = withContext(Dispatchers.IO) {
        var totalPrice = 0
        try {
            val doc = db.collection("users").document(userId)
                .collection("cart").document("current_cart").get().await()

            if (doc.exists()) {
                val data = doc.data ?: return@withContext 0
                val serviceSubcategories = db.collection("service_subcategories")

                for ((serviceId, services) in data) {
                    if (services is Map<*, *>) {
                        val serviceDetails = serviceSubcategories.document(serviceId).get().await()
                        val items = serviceDetails.get("items") as? List<Map<String, Any>>

                        if (items != null) {
                            for ((_, serviceItems) in services) {
                                if (serviceItems is Map<*, *>) {
                                    val id = serviceItems["id"] as? String ?: continue
                                    val count = (serviceItems["items_count"] as? Long)?.toInt() ?: 0

                                    for (item in items) {
                                        if (item["id"] == id) {
                                            totalPrice += item["price"].toString().toInt() * count
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext totalPrice
    }

    fun fetchItemCount(userId: String, subcategoryId: String, itemId: String) {
        val cartRef = db.collection("users").document(userId)
            .collection("cart").document("current_cart")

        cartRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val cartData = documentSnapshot.data
                val subcategoryData = cartData?.get(subcategoryId) as? Map<*, *>
                val itemData = subcategoryData?.get(itemId) as? Map<*, *>
                val count = (itemData?.get("items_count") as? Long)?.toInt() ?: 0

                _itemCount.value = _itemCount.value?.toMutableMap()?.apply {
                    put(itemId, count)
                } ?: mapOf(itemId to count)
            }
        }
    }
}
