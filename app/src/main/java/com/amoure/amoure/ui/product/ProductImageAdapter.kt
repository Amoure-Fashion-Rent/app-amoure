package com.amoure.amoure.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amoure.amoure.databinding.ItemProductImageBinding
import com.bumptech.glide.Glide

class ProductImageAdapter : ListAdapter<String, ProductImageAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemProductImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imgURL = getItem(position)
        holder.bind(imgURL)
    }

    class MyViewHolder(private val binding: ItemProductImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgURL: String) {
            with(binding) {
                Glide.with(ivProduct.context)
                    .load(imgURL)
                    .into(ivProduct)
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}