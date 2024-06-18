package com.amoure.amoure.ui.category

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amoure.amoure.data.response.CategoryItem
import com.amoure.amoure.data.dataclass.Category
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.databinding.ItemCategoryBinding
import com.bumptech.glide.Glide

class CategoryAdapter : ListAdapter<CategoryItem, CategoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user, onItemClickCallback, holder.itemView.context)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility", "SimpleDateFormat")
        fun bind(category: CategoryItem, onItemClickCallback: OnItemClickCallback, context: Context) {
            with(binding) {
                tvName.text = category.categoriesName.toString()
//                Glide.with(ivProduct.context)
//                    .load(category.imgProduct)
//                    .into(ivProduct)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: String)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategoryItem>() {
            override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}