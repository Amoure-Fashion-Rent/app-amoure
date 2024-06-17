package com.amoure.amoure.ui.renthistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amoure.amoure.data.response.RentItem
import com.amoure.amoure.databinding.ItemRentHistoryBinding
import com.amoure.amoure.formatDate
import com.amoure.amoure.removeUnderscoreAndCapitalize
import com.bumptech.glide.Glide

class RentHistoryAdapter : PagingDataAdapter<RentItem, RentHistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRentHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val rent = getItem(position)
        if (rent != null) {
            holder.bind(rent, onItemClickCallback)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(private val binding: ItemRentHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rent: RentItem, onItemClickCallback: OnItemClickCallback) {
            with(binding) {
                tvName.text = rent.productName.toString()
                tvOwner.text = rent.product?.owner?.fullName.toString()
                tvStatus.text = rent.status?.removeUnderscoreAndCapitalize()
                tvArrives.text = rent.rentalStartDate?.formatDate()
                tvReturn.text = rent.rentalEndDate?.formatDate()
                Glide.with(ivProduct.context)
                    .load(rent.product?.images?.get(0))
                    .into(ivProduct)
                itemRentHistory.setOnClickListener {
                    rent.productId?.let { onItemClickCallback.onItemClicked(it) }
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: Int)
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