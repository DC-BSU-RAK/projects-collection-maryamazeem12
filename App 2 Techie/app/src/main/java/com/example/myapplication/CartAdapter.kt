package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val items: MutableList<CartManager.CartItem>,
    private val onUpdate: () -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProduct: ImageView = view.findViewById(R.id.iv_cart_image)
        val tvName: TextView = view.findViewById(R.id.tv_cart_name)
        val tvVariant: TextView = view.findViewById(R.id.tv_cart_variant)
        val tvPrice: TextView = view.findViewById(R.id.tv_cart_price)
        val tvQty: TextView = view.findViewById(R.id.tv_quantity)
        val btnPlus: ImageView = view.findViewById(R.id.btn_plus)
        val btnMinus: ImageView = view.findViewById(R.id.btn_minus)
        val btnRemove: ImageView = view.findViewById(R.id.btn_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.ivProduct.setImageResource(item.imageResId)
        holder.tvName.text = item.name
        holder.tvVariant.text = item.variant
        holder.tvPrice.text = "AED ${String.format("%.2f", item.price)}"
        holder.tvQty.text = item.quantity.toString()

        holder.btnPlus.setOnClickListener {
            val currentPos = holder.bindingAdapterPosition
            if (currentPos != RecyclerView.NO_POSITION) {
                item.quantity++
                notifyItemChanged(currentPos)
                onUpdate()
            }
        }

        holder.btnMinus.setOnClickListener {
            val currentPos = holder.bindingAdapterPosition
            if (currentPos != RecyclerView.NO_POSITION && item.quantity > 1) {
                item.quantity--
                notifyItemChanged(currentPos)
                onUpdate()
            }
        }

        holder.btnRemove.setOnClickListener {
            val currentPos = holder.bindingAdapterPosition
            if (currentPos != RecyclerView.NO_POSITION) {
                CartManager.removeItem(item)
                notifyItemRemoved(currentPos)
                notifyItemRangeChanged(currentPos, items.size)
                onUpdate()
            }
        }
    }

    override fun getItemCount() = items.size
}