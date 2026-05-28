package com.am.lapcart

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
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class CartFragment : Fragment(), CartAdapter.CartListener, PaymentResultListener {

    private lateinit var db: DBHelper
    private lateinit var adapter: CartAdapter
    private lateinit var txtTotalAmount: TextView
    private lateinit var btnCheckOut: AppCompatButton
    var totalAmount = 0.0

    private lateinit var list: ArrayList<CartModel>


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
            startPayment()
        }


        return view

    }

    fun startPayment() {

        FirebaseCrashlytics.getInstance()
            .log("checkout started")

        val checkout = Checkout()
        checkout.setKeyID("rzp_test_Sg0tQZLis21V4G")

        try {
            val options = JSONObject()
            options.put("name", "Lab Cart")
            options.put("description", "Test Payment")
            options.put("currency", "INR")
            options.put("amount", totalAmount * 100) // ₹500 = 50000 paise

            val prefill = JSONObject()
            prefill.put("email", "test@example.com")
            prefill.put("contact", "9999999999")

            options.put("prefill", prefill)

            checkout.open(activity, options)

        } catch (e: Exception) {
            e.printStackTrace()

            FirebaseCrashlytics.getInstance()
                .recordException(e)
        }
    }

    override fun onDelete(item: CartModel) {
        TODO("Not yet implemented")
    }

    override fun onPlus(item: CartModel) {
        TODO("Not yet implemented")
    }

    override fun onMinus(item: CartModel) {
        TODO("Not yet implemented")
    }

    override fun refreshCart() {
        list.clear()
        list.addAll(db.getProducts())
        adapter.notifyDataSetChanged()
        calculateTotalAmount()
    }

    override fun onResume() {
        super.onResume()
        list.clear()
        list.addAll(db.getProducts())
        adapter.notifyDataSetChanged()
        calculateTotalAmount()

}

    fun calculateTotalAmount() {
        totalAmount = 0.0
        for (item in list)
            totalAmount += item.price.toDouble() * item.quantity

        txtTotalAmount.text = "₹$totalAmount"
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(activity, "Payment Success: $p0", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(activity, "Payment Failed: $p1", Toast.LENGTH_LONG).show()
    }
}