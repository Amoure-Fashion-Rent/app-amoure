package com.amoure.amoure.ui.tryon

import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.amoure.amoure.R
import com.amoure.amoure.databinding.ActivityTryonResultBinding
import com.amoure.amoure.reduceFileImage
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.review.TryOnViewModel
import com.amoure.amoure.uriToFile
import com.amoure.amoure.withCurrencyFormat
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import kotlin.properties.Delegates

class TryOnResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTryonResultBinding
    private val tryOnActivity by viewModels<TryOnViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var imageUri: Uri? = null
    private var productId by Delegates.notNull<Int>()
    private lateinit var productName: String
    private lateinit var ownerName: String
    private var rentPrice by Delegates.notNull<Int>()
    private var retailPrice by Delegates.notNull<Int>()
    private lateinit var imageUrl: String
    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        productName = intent.getStringExtra(PRODUCT_NAME).toString()
        productId = intent.getStringExtra(PRODUCT_ID).toString().toInt()
        productName = intent.getStringExtra(PRODUCT_NAME).toString()
        ownerName = intent.getStringExtra(OWNER_NAME).toString()
        rentPrice = intent.getStringExtra(RENT_PRICE).toString().toInt()
        retailPrice = intent.getStringExtra(RETAIL_PRICE).toString().toInt()
        imageUrl = intent.getStringExtra(IMAGE_URL).toString()
        imageUri = intent.getStringExtra(IMAGE_URI)?.toUri()
        category = intent.getStringExtra(CATEGORY).toString()

        binding = ActivityTryonResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageUri?.let {
            postSearchVis(it)
        }

        tryOnActivity.tryOn.observe(this) {
            it?.let {
                Glide.with(binding.ivResult.context)
                    .load(imageUrl)
                    .into(binding.ivResult)
            }
        }

        tryOnActivity.isLoading.observe(this) {
            it?.let {
                showLoading(it)
            }
        }

        with(binding) {
            topAppBarSecond.setNavigationOnClickListener {
                finish()
            }

            setItem()

            btRetry.setOnClickListener {
                finish()
            }
        }
    }

    private fun postSearchVis(imageUri: Uri) {
        val vtonImageFile = uriToFile(imageUri, this).reduceFileImage()
        val garmImageFile = uriToFile(Uri.parse(imageUrl), this).reduceFileImage()
        showLoading(true)

        val requestVtonImageFile = vtonImageFile.asRequestBody("image/jpeg".toMediaType())
        val vtonImage = MultipartBody.Part.createFormData(
            "photo",
            garmImageFile.name,
            requestVtonImageFile
        )

        val requestGarmImageFile = garmImageFile.asRequestBody("image/jpeg".toMediaType())
        val garmImage = MultipartBody.Part.createFormData(
            "photo",
            garmImageFile.name,
            requestGarmImageFile
        )
        tryOnActivity.postTryOn(vtonImage, garmImage, category)
    }

    private fun setItem() {
        with(binding) {
            tvName.text = productName
            tvOwner.text = ownerName
            tvRetailPrice.paintFlags = tvRetailPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            tvRetailPrice.text = String.format(getString(R.string.retail_price_cart), retailPrice.withCurrencyFormat())
            tvPrice.text = String.format(getString(R.string.rent_price_cart), rentPrice.withCurrencyFormat())
            Glide.with(ivProduct.context)
                .load(imageUrl)
                .into(ivProduct)
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    companion object {
        const val IMAGE_URI = "image_uri"
        const val PRODUCT_ID = "product_id"
        const val PRODUCT_NAME = "product_name"
        const val OWNER_NAME = "owner_name"
        const val RENT_PRICE = "rent_price"
        const val RETAIL_PRICE = "retail_price"
        const val IMAGE_URL = "image_url"
        const val CATEGORY = "category"
    }
}
