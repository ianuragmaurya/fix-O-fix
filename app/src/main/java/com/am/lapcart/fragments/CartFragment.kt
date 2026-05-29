package com.am.lapcart.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.am.lapcart.adapters.CartAdapter
import com.am.lapcart.models.CartModel
import com.am.lapcart.database.DBHelper
import com.am.lapcart.R
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class CartFragment : Fragment(), CartAdapter.CartListener, PaymentResultListener {

    private lateinit var db: DBHelper
    private lateinit var adapter: CartAdapter
    private lateinit var txtTotalAmount: TextView
    private lateinit var btnCheckOut: AppCompatButton
    private var totalAmount = 0.0

    private var list = ArrayList<CartModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "Cart"
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        val rvCart = view.findViewById<RecyclerView>(R.id.rvCart)
        val btnCheckOut = view.findViewById<AppCompatButton>(R.id.btnCheckOut)
        txtTotalAmount = view.findViewById(R.id.txtTotalAmount)

        db = DBHelper(requireContext())
        list = db.getProducts()

        adapter = CartAdapter(list, requireContext(), this)
        rvCart.layoutManager = LinearLayoutManager(requireContext())
        rvCart.adapter = adapter
        
        calculateTotalAmount()

        btnCheckOut.setOnClickListener {
            if (list.isNotEmpty()) {
                startPayment()
            } else {
                Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    fun startPayment() {
        FirebaseCrashlytics.getInstance().log("checkout started")
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_Sg0tQZLis21V4G")

        try {
            val options = JSONObject()
            options.put("name", "Lab Cart")
            options.put("description", "Test Payment")
            options.put("currency", "INR")
            options.put("amount", totalAmount * 100)

            val prefill = JSONObject()
            prefill.put("email", "test@example.com")
            prefill.put("contact", "9999999999")

            options.put("prefill", prefill)
            checkout.open(activity, options)
        } catch (e: Exception) {
            e.printStackTrace()
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    override fun onDelete(item: CartModel) {
        // Logic handled in Adapter for now, but implemented here for safety
        refreshCart()
    }

    override fun onPlus(item: CartModel) {
        // Logic handled in Adapter for now
        refreshCart()
    }

    override fun onMinus(item: CartModel) {
        // Logic handled in Adapter for now
        refreshCart()
    }

    override fun refreshCart() {
        list.clear()
        list.addAll(db.getProducts())
        adapter.notifyDataSetChanged()
        calculateTotalAmount()
    }

    override fun onResume() {
        super.onResume()
        refreshCart()
    }

    private fun calculateTotalAmount() {
        totalAmount = 0.0
        for (item in list) {
            try {
                totalAmount += item.price.toDouble() * item.quantity
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        txtTotalAmount.text = "₹$totalAmount"
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(activity, "Payment Success: $p0", Toast.LENGTH_LONG).show()
        // Here you might want to clear the cart in DB
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(activity, "Payment Failed: $p1", Toast.LENGTH_LONG).show()
    }
}
