package com.am.lapcart

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceActivity : AppCompatActivity() {

    lateinit var adapter: ServiceAdapter
    lateinit var rvService: RecyclerView
    lateinit var loaderLayout: FrameLayout
    lateinit var lottieLoaders : LottieAnimationView

var list = ArrayList<ServiceModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_service)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarServices)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Services"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        rvService = findViewById(R.id.rvService)
        adapter = ServiceAdapter(list)
        loaderLayout = findViewById(R.id.loaderLayouts)
        lottieLoaders = findViewById(R.id.lottieLoaders)


        rvService.layoutManager = GridLayoutManager(this, 2)
        rvService.adapter = adapter

        val categoryId = intent.getIntExtra("category_id", 0)


        loadServices(categoryId)

    }

    private fun loadServices(categoryId: Int) {

        loaderLayout.visibility = View.VISIBLE
        lottieLoaders.visibility = View.VISIBLE
        lottieLoaders.playAnimation()


        ApiClient.getApi().getServices(categoryId).enqueue(object : Callback<ServiceResponse>{

            override fun onResponse(
                call: Call<ServiceResponse>,
                response: Response<ServiceResponse>
            ) {
                if (response.isSuccessful) {


                    list.clear()
                    list.addAll(response.body()!!.data)
                    adapter.notifyDataSetChanged()

                    loaderLayout.visibility = View.GONE
                    lottieLoaders.visibility = View.GONE
                    lottieLoaders.pauseAnimation()

                }
            }
            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {

                loaderLayout.visibility = View.GONE
                lottieLoaders.visibility = View.GONE
                lottieLoaders.pauseAnimation()

                Toast.makeText(this@ServiceActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    }
