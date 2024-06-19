package com.amoure.amoure.ui.wishlist

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amoure.amoure.R
import com.amoure.amoure.data.response.WishlistItem
import com.amoure.amoure.databinding.ItemWishlistBinding
import com.amoure.amoure.withCurrencyFormat
import com.bumptech.glide.Glide

class WishlistAdapter : ListAdapter<WishlistItem, WishlistAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user, onItemClickCallback, holder.itemView.context)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(private val binding: ItemWishlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility", "SimpleDateFormat")
        fun bind(wishlist: WishlistItem, onItemClickCallback: OnItemClickCallback, context: Context) {
            val product = wishlist.product
            with(binding) {
                tvRetailPrice.paintFlags = tvRetailPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvRetailPrice.text = String.format(context.resources.getString(R.string.retail_price_cart), product?.retailPrice?.withCurrencyFormat())
                tvPrice.text = String.format(context.resources.getString(R.string.rent_price_cart), product?.rentPrice?.withCurrencyFormat())
                tvName.text = product?.name
                Glide.with(ivProduct.context)
                    .load(product?.images?.get(0))
                    .into(ivProduct)
                itemProductSmall.setOnClickListener {
                    product?.id?.let { onItemClickCallback.onItemClicked(it) }
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: Int)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WishlistItem>() {
            override fun areItemsTheSame(oldItem: WishlistItem, newItem: WishlistItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: WishlistItem, newItem: WishlistItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}