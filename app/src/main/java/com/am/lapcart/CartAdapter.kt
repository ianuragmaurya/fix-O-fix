package com.am.lapcart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(val list: ArrayList<CartModel>, val context: Context, val listener: CartListener):
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

        interface CartListener{
            fun onDelete(item: CartModel)
            fun onPlus(item: CartModel)
            fun onMinus(item: CartModel)
        fun refreshCart()

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val imgCartProduct = itemView.findViewById<ImageView>(R.id.imgCartProduct)
        val txtItemName = itemView.findViewById<TextView>(R.id.txtItemName)
        val txtCartPrice = itemView.findViewById<TextView>(R.id.txtCartPrice)
        val txtQty = itemView.findViewById<TextView>(R.id.txtQty)
        val btnPlus = itemView.findViewById<ImageView>(R.id.btnPlus)
        val btnMinus = itemView.findViewById<ImageView>(R.id.btnMinus)
        val btnDeleteItem = itemView.findViewById<ImageView>(R.id.btnDeleteItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_cart, parent, false)

        return ViewHolder(view)

    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = list[position]

        Glide.with(context)
            .load(model.img_url)
            .into(holder.imgCartProduct)

        holder.txtItemName.text = model.title
        holder.txtCartPrice.text = "₹${model.price}"
        holder.txtQty.text = model.quantity.toString()

        val db = DBHelper(context)


        holder.btnDeleteItem.setOnClickListener {

            val pos = holder.adapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

            val item = list[pos]


            db.deleteProduct(model.product_id)
            list.removeAt(pos)

            notifyItemRemoved(pos)
            notifyItemRangeChanged(pos,list.size)
            listener.refreshCart()

        }

        holder.btnPlus.setOnClickListener {

            val pos = holder.adapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener
                val model =list[pos]

            val item =  list[pos]

            item.quantity++

            db.updateQuantity(model.product_id, model.quantity)

            notifyItemChanged(pos)
            listener.refreshCart()

        }

        holder.btnMinus.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

            val item = list[pos]

            if (item.quantity > 1) {

                item.quantity--

                db.updateQuantity(model.product_id, model.quantity)

                notifyItemChanged(pos)
                listener.refreshCart()
            }
        }
    }
}