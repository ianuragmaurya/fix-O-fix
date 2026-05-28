package com.am.lapcart

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class ServiceDetailsActivity : AppCompatActivity(), PaymentResultListener {

    lateinit var imgService: ImageView
    lateinit var txtTitle: TextView
    lateinit var txtPrice: TextView
    lateinit var txtDuration: TextView
    lateinit var txtDescription: TextView

    lateinit var btnAddToCart: Button
    lateinit var btnBuyNow: Button
    var servicePrice :String ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_details)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarDetails)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Service Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        imgService = findViewById(R.id.imgService)
        txtTitle = findViewById(R.id.txtDetailTitle)
        txtPrice = findViewById(R.id.txtDetailPrice)
        txtDuration = findViewById(R.id.txtDetailDuration)
        txtDescription = findViewById(R.id.txtDetailDescription)

        btnAddToCart = findViewById(R.id.btnAddToCart)
        btnBuyNow = findViewById(R.id.btnBuyNow)


        val title = intent.getStringExtra("title")
         servicePrice = intent.getStringExtra("price")
        val duration = intent.getIntExtra("duration", 0)
        val imageUrl = intent.getStringExtra("image_url")
        val description = intent.getStringExtra("description")
        val product_id = intent.getIntExtra("id", 0)



        txtTitle.text = title
        txtPrice.text = "₹ $servicePrice"
        txtDuration.text = "$duration min"
        txtDescription.text = description

        Glide.with(this)
            .load(imageUrl)
            .into(imgService)

        btnBuyNow.setOnClickListener {
            startPayment()
        }

        btnAddToCart.setOnClickListener {

            val db = DBHelper(this)
            if (db.isProductInCart(product_id)) {

                db.increaseQuantity(product_id)

                Toast.makeText(this, "Update quantity", Toast.LENGTH_SHORT).show()


            } else {

                val item = CartModel(
                    product_id = product_id,
                    img_url = imageUrl ?: "",
                    title = title ?: "",
                    price = servicePrice ?: "",
                    quantity = 1
                )


                db.insertProduct(item)
                Toast.makeText(this, "Add to cart", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            btnBuyNow.setOnClickListener {
                startPayment()
            }

        }
    }

    fun startPayment() {
            val checkout = Checkout()
            checkout.setKeyID("rzp_test_Sg0tQZLis21V4G")


            try {
                val options = JSONObject()
                options.put("name", "FixoFix")
                options.put("description", "Test Payment")
                options.put("currency", "INR")
                val cleanPrice = servicePrice?.replace("[^0-9.]".toRegex(), "") ?: "0"
                val amount = (cleanPrice.toDouble() * 100).toInt()
                options.put("amount", amount)
                val prefill = JSONObject()
                prefill.put("email", "test@example.com")
                prefill.put("contact", "6355569856")
                options.put("prefill", prefill)

                checkout.open(this, options)

            } catch (e: Exception){
                e.printStackTrace()
            }
        }

    override fun onSupportNavigateUp() : Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
        }

    override fun onPaymentSuccess(razorPaymentID: String?) {
        Toast.makeText(this, "Payment Success: $razorPaymentID",Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "payment Failed: $response", Toast.LENGTH_LONG).show()
    }
}

