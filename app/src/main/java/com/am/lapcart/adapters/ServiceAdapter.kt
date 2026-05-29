package com.am.lapcart.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.am.lapcart.R
import com.am.lapcart.models.ServiceModel
import com.am.lapcart.activities.ServiceDetailsActivity
import com.bumptech.glide.Glide

class ServiceAdapter(val list: ArrayList<ServiceModel>):
RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val imgService = itemView.findViewById<ImageView>(R.id.imgService)
        val txtTitle = itemView.findViewById<TextView>(R.id.txtTitle)
        val txtPrice = itemView.findViewById<TextView>(R.id.txtPrice)
        val txtDuration = itemView.findViewById<TextView>(R.id.txtDuration)
        val btnAdd = itemView.findViewById<Button>(R.id.btnAdd)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)

        return ViewHolder(view)

    }


    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = list[position]

        holder.txtTitle.text = model.title
        holder.txtPrice.text = "₹ "+ model.price
        holder.txtDuration.text = model.duration_minutes.toString() + " min"
        Glide.with(holder.itemView.context)
            .load(model.img_url)
            .into(holder.imgService)



        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ServiceDetailsActivity::class.java)

            intent.putExtra("id", model.id)
            intent.putExtra("title", model.title)
            intent.putExtra("price", model.price)
            intent.putExtra("duration", model.duration_minutes)
            intent.putExtra("image_url", model.img_url)
            intent.putExtra("description", model.description)

            holder.itemView.context.startActivity(intent)

        }

    }

}