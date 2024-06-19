package com.amoure.amoure.ui.addproduct

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amoure.amoure.databinding.ItemImageAddBinding
import com.bumptech.glide.Glide

class ImageAddAdapter : ListAdapter<Uri, ImageAddAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemImageAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val image = getItem(position)
        if (image != null) {
            holder.bind(image, onItemClickCallback, holder.itemView.context)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(private val binding: ItemImageAddBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Uri, onItemClickCallback: OnItemClickCallback, context: Context) {
            with(binding) {
                if (image.toString() == "0" || image.toString() == "1" ) {
                    itemImage.setOnClickListener {
                        ivAdd.visibility = View.VISIBLE
                        onItemClickCallback.onItemClicked(image.toString())
                    }
                } else {
                    Glide.with(ivImage.context)
                        .load(image)
                        .into(ivImage)
                    ivAdd.visibility = View.GONE
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(flag: String)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem == newItem
            }
        }
    }
}