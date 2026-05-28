package com.am.lapcart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment () {

    lateinit var rvRecycler: RecyclerView
    lateinit var category: CategoryAdapter
    lateinit var loaderLayout: FrameLayout
    lateinit var lottieLoader: LottieAnimationView



    val list = ArrayList<CategoryModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        requireActivity().title = "Home"

        val view = inflater.inflate(R.layout.fragment_home, container, false)


        rvRecycler = view.findViewById(R.id.rvRecycler)
        loaderLayout = view.findViewById(R.id.loaderLayout)
        lottieLoader = view.findViewById(R.id.lottieLoader)

        category = CategoryAdapter(list)
        rvRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        rvRecycler.adapter = category

        loadCategories()
        return view
    }

    private fun loadCategories() {

        loaderLayout.visibility = View.VISIBLE
        lottieLoader.visibility = View.VISIBLE
        lottieLoader.playAnimation()

        FirebaseCrashlytics.getInstance()
            .log("home fragment Category API request started")


        ApiClient.getApi().getCategories().enqueue(object : Callback<CategoryResponse> {

            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>
            ) {


                if (response.isSuccessful) {

                    list.clear()
                    list.addAll(response.body()!!.data)
                    category.notifyDataSetChanged()


                    loaderLayout.visibility = View.GONE
                    lottieLoader.visibility = View.GONE
                    lottieLoader.pauseAnimation()


                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {

                loaderLayout.visibility = View.GONE
                lottieLoader.visibility = View.GONE
                lottieLoader.pauseAnimation()


                FirebaseCrashlytics.getInstance()
                    .recordException(Exception("Home fragment Category API failed ${t.message}"))

            }
        })
    }
}