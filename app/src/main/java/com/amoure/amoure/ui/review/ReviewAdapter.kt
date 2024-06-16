package com.amoure.amoure.ui.review

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amoure.amoure.R
import com.amoure.amoure.data.response.ReviewItem
import com.amoure.amoure.databinding.ItemReviewBinding

class ReviewAdapter : PagingDataAdapter<ReviewItem, ReviewAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        if (review != null) {
            holder.bind(review, holder.itemView.context)
        }
    }

    class MyViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ReviewItem, context: Context) {
            with(binding) {
                tvName.text = review.user?.fullName
                tvComment.text = review.comment.toString()
                if (review.rating == null) {
                    return
                }
                if (review.rating >= 1.0) {
                    setStarColor(star1, context)
                }
                if (review.rating >= 2.0) {
                    setStarColor(star2, context)
                }
                if (review.rating >= 3.0) {
                    setStarColor(star3, context)
                }
                if (review.rating >= 4.0) {
                    setStarColor(star4, context)
                }
                if (review.rating >= 5.0) {
                    setStarColor(star5, context)
                }
            }
        }

        private fun setStarColor(star: ImageView, context: Context) {
            star.setColorFilter(ContextCompat.getColor(context, R.color.yellow_500))
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReviewItem>() {
            override fun areItemsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}