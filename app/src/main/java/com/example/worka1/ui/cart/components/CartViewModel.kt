import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class CartViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _itemCount = MutableLiveData<Map<String, Int>>()
    val itemCount: LiveData<Map<String, Int>> get() = _itemCount

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
