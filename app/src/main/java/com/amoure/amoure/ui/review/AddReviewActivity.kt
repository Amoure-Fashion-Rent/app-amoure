package com.amoure.amoure.ui.review

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amoure.amoure.R
import com.amoure.amoure.data.request.PostReviewRequest
import com.amoure.amoure.databinding.ActivityAddReviewBinding
import com.amoure.amoure.ui.ViewModelFactory
import com.bumptech.glide.Glide
import kotlin.properties.Delegates

class AddReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReviewBinding
    private val reviewViewModel by viewModels<ReviewViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var productId by Delegates.notNull<Int>()
    private lateinit var productName: String
    private lateinit var ownerName: String
    private lateinit var imageUrl: String
    private var rating by Delegates.notNull<Double>()
    private var putRating: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productId = intent.getStringExtra(PRODUCT_ID).toString().toInt()
        productName = intent.getStringExtra(PRODUCT_NAME).toString()
        ownerName = intent.getStringExtra(OWNER_NAME).toString()
        rating = intent.getStringExtra(RATING).toString().toDouble()
        imageUrl = intent.getStringExtra(IMAGE_URL).toString()

        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reviewViewModel.isError.observe(this) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        reviewViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.topAppBarSecond.setOnClickListener {
            finish()
        }

        with(binding) {
            star1.setOnClickListener {
                putRating = 1
                setStarColor(listOf(R.color.yellow_500, R.color.gray_200, R.color.gray_200, R.color.gray_200, R.color.gray_200))
            }
            star2.setOnClickListener {
                putRating = 2
                setStarColor(listOf(R.color.yellow_500, R.color.yellow_500, R.color.gray_200, R.color.gray_200, R.color.gray_200))
            }
            star3.setOnClickListener {
                putRating = 3
                setStarColor(listOf(R.color.yellow_500, R.color.yellow_500, R.color.yellow_500, R.color.gray_200, R.color.gray_200))
            }
            star4.setOnClickListener {
                putRating = 4
                setStarColor(listOf(R.color.yellow_500, R.color.yellow_500, R.color.yellow_500, R.color.yellow_500, R.color.gray_200))
            }
            star5.setOnClickListener {
                putRating = 5
                setStarColor(listOf(R.color.yellow_500, R.color.yellow_500, R.color.yellow_500, R.color.yellow_500, R.color.yellow_500))
            }

            postReview()
            setReview()
        }
    }

    private fun postReview() {
        with(binding) {
            btWriteReview.setOnClickListener {
                if (putRating == -1) {
                    showToast(String.format(getString(R.string.input_required_2), "rating"))
                    return@setOnClickListener
                }
                val comment = edReviewComment.text.toString()
                if (comment.isEmpty()) {
                    edlReviewComment.error = String.format(getString(R.string.input_required_2), "comment")
                    return@setOnClickListener
                } else {
                    edlReviewComment.isErrorEnabled = false
                }
                reviewViewModel.postReview(PostReviewRequest(productId, putRating, comment))
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
        }
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    private fun setStarColor(color: List<Int>) {
        with(binding) {
            star1.compoundDrawableTintList =
                ColorStateList.valueOf(ContextCompat.getColor(baseContext, color[0]))
            star2.compoundDrawableTintList =
                ColorStateList.valueOf(ContextCompat.getColor(baseContext, color[1]))
            star3.compoundDrawableTintList =
                ColorStateList.valueOf(ContextCompat.getColor(baseContext, color[2]))
            star4.compoundDrawableTintList =
                ColorStateList.valueOf(ContextCompat.getColor(baseContext, color[3]))
            star5.compoundDrawableTintList =
                ColorStateList.valueOf(ContextCompat.getColor(baseContext, color[4]))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    companion object {
        const val PRODUCT_ID = "product_id"
        const val PRODUCT_NAME = "product_name"
        const val OWNER_NAME = "owner_name"
        const val RATING = "rating"
        const val IMAGE_URL = "image_url"
    }
}