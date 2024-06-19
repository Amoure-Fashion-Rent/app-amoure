package com.amoure.amoure.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amoure.amoure.R
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.databinding.ItemProductMediumBinding
import com.amoure.amoure.withCurrencyFormat
import com.bumptech.glide.Glide

class ProductMedium2Adapter : ListAdapter<ProductItem, ProductMedium2Adapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemProductMediumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user, onItemClickCallback, holder.itemView.context)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(private val binding: ItemProductMediumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductItem, onItemClickCallback: OnItemClickCallback, context: Context) {
            with(binding) {
                tvPrice.text = String.format(context.resources.getString(R.string.rent_price_product), product.rentPrice?.withCurrencyFormat())
                tvName.text = product.name
                tvOwner.text = product.owner?.fullName
                Glide.with(ivProduct.context)
                    .load(product.images?.get(0))
                    .into(ivProduct)
                itemProductSmall.setOnClickListener {
                    product.id?.let { onItemClickCallback.onItemClicked(it) }
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: Int)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductItem>() {
            override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}