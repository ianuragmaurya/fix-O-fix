package com.am.lapcart

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CategoryAdapter (val list: ArrayList<CategoryModel>) :
RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val imgLogo = itemView.findViewById<ImageView>(R.id.imgLogo)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {

        return list.size
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = list[position]
        holder.txtName.text = model.name
        Glide.with(holder.itemView.context)
            .load(model.logo_path)
            .into(holder.imgLogo)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ServiceActivity::class.java)
            intent.putExtra("category_id", model.id)
            holder.itemView.context.startActivity(intent)

        }
    }

}