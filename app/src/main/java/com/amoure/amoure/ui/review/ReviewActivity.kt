package com.amoure.amoure.ui.review

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.databinding.ActivityReviewBinding
import com.amoure.amoure.ui.LoadingStateAdapter
import com.amoure.amoure.ui.ViewModelFactory
import com.bumptech.glide.Glide
import kotlin.properties.Delegates

class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding
    private val reviewViewModel by viewModels<ReviewViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var productId by Delegates.notNull<Int>()
    private lateinit var productName: String
    private lateinit var ownerName: String
    private lateinit var imageUrl: String
    private var rating by Delegates.notNull<Double>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productId = intent.getStringExtra(PRODUCT_ID).toString().toInt()
        productName = intent.getStringExtra(PRODUCT_NAME).toString()
        ownerName = intent.getStringExtra(OWNER_NAME).toString()
        rating = intent.getStringExtra(RATING).toString().toDouble()
        imageUrl = intent.getStringExtra(IMAGE_URL).toString()

        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvReviews.layoutManager = LinearLayoutManager(this)

        reviewViewModel.getReviews(productId)
        reviewViewModel.isError.observe(this) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        reviewViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        setReview()
        setReviews()
        setupAction()
    }

    private fun setupAction() {
        with(binding) {
            btWriteReview.setOnClickListener {
                val moveIntent = Intent(baseContext, AddReviewActivity::class.java)
                moveIntent.putExtra(AddReviewActivity.PRODUCT_ID, productId.toString())
                moveIntent.putExtra(AddReviewActivity.PRODUCT_NAME, productName)
                moveIntent.putExtra(AddReviewActivity.OWNER_NAME, ownerName)
                moveIntent.putExtra(AddReviewActivity.RATING, rating.toString())
                moveIntent.putExtra(AddReviewActivity.IMAGE_URL, imageUrl)
                startActivity(moveIntent)
            }

            topAppBarSecond.setOnClickListener {
                finish()
            }
        }
    }

    private fun setReview() {
        with(binding) {
            tvName.text = productName
            tvOwner.text = ownerName
            Glide.with(ivProduct.context)
                .load(imageUrl)
                .into(ivProduct)
            if (rating >= 1.0) {
                setStarColor(star1)
            }
            if (rating >= 2.0) {
                setStarColor(star2)
            }
            if (rating >= 3.0) {
                setStarColor(star3)
            }
            if (rating >= 4.0) {
                setStarColor(star4)
            }
            if (rating >= 5.0) {
                setStarColor(star5)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        reviewViewModel.getReviews(productId)
    }

    private fun setStarColor(star: ImageView) {
        star.setColorFilter(ContextCompat.getColor(baseContext, R.color.yellow_500))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setReviews() {
        val adapter = ReviewAdapter()
        binding.rvReviews.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        reviewViewModel.reviews.observe(this) {
            it?.let {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    companion object {
        const val PRODUCT_ID = "product_id"
        const val PRODUCT_NAME = "product_name"
        const val OWNER_NAME = "owner_name"
        const val RATING = "rating"
        const val IMAGE_URL = "image_url"
    }
}