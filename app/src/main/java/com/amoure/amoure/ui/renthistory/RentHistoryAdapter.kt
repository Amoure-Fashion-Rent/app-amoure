package com.amoure.amoure.ui.renthistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amoure.amoure.data.response.RentItem
import com.amoure.amoure.databinding.ItemRentHistoryBinding

class RentHistoryAdapter : ListAdapter<RentItem, RentHistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRentHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val rent = getItem(position)
        holder.bind(rent, holder.itemView.context)
    }

    class MyViewHolder(private val binding: ItemRentHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rent: RentItem, context: Context) {
            with(binding) {
                // TODO: match the binding with backend later

            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RentItem>() {
            override fun areItemsTheSame(oldItem: RentItem, newItem: RentItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: RentItem, newItem: RentItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}