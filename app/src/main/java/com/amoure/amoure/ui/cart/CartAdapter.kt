package com.amoure.amoure.ui.cart

import android.annotation.SuppressLint
import android.content.Context
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amoure.amoure.R
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.databinding.ItemCartBinding
import com.amoure.amoure.withCurrencyFormat
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat


class CartAdapter : ListAdapter<ProductItem, CartAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user, onItemClickCallback, holder.itemView.context)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility", "SimpleDateFormat")
        fun bind(product: ProductItem, onItemClickCallback: OnItemClickCallback, context: Context) {
            with(binding) {
                tvPrice.text = String.format(context.resources.getString(R.string.rent_price_cart), product.rentPrice?.withCurrencyFormat())
                tvName.text = product.name
                tvOwner.text = product.ownerName
                tvSizeColor.text = String.format(context.resources.getString(R.string.size_color_cart), product.size, product.color)
                Glide.with(ivProduct.context)
                    .load(product.imgProduct)
                    .into(ivProduct)
                btRemove.setOnClickListener {
                    product.id?.let { onItemClickCallback.onItemClicked(it) }
                }
                edRentalPeriod.setOnTouchListener { _, motionEvent ->
                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        val constraintsBuilder =
                            CalendarConstraints.Builder()
                                .setFirstDayOfWeek(Calendar.MONDAY)
                                .setValidator(DateValidatorPointForward.now())

                        val datePicker =
                            MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Select rental period")
                                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()

                        datePicker.show(
                            (context as AppCompatActivity).supportFragmentManager,
                            "RENTAL_PERIOD_DATE_PICKER"
                        )

                        datePicker.addOnPositiveButtonClickListener {
                            val cal = Calendar.getInstance()
                            cal.timeInMillis = it
                            val format = SimpleDateFormat("dd/MM/yyyy")
                            val formattedDate: String = format.format(cal.time)
                            binding.edlRentalPeriod.editText?.setText(formattedDate)
                        }
                    }
                    false
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: String)
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