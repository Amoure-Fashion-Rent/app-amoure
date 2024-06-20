package com.amoure.amoure.ui.tryon

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.amoure.amoure.R
import com.amoure.amoure.databinding.ActivityTryonInfoBinding
import com.amoure.amoure.withCurrencyFormat
import com.bumptech.glide.Glide

class TryOnInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTryonInfoBinding

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private var imageUri: Uri? = null
    private var productId: Int = 0
    private var productName: String = ""
    private var ownerName: String = ""
    private var rentPrice: Int = 0
    private var retailPrice: Int = 0
    private var imageUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productId = intent.getStringExtra(PRODUCT_ID).toString().toInt()
        productName = intent.getStringExtra(PRODUCT_NAME).toString()
        ownerName = intent.getStringExtra(OWNER_NAME).toString()
        rentPrice = intent.getStringExtra(RENT_PRICE).toString().toInt()
        retailPrice = intent.getStringExtra(RETAIL_PRICE).toString().toInt()
        imageUrl = intent.getStringExtra(IMAGE_URL).toString()

        binding = ActivityTryonInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                imageUri = result.data?.data
                Glide.with(binding.ivPhoto.context)
                    .load(imageUri)
                    .into(binding.ivPhoto)
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUri = uri
                Glide.with(binding.ivPhoto.context)
                    .load(imageUri)
                    .into(binding.ivPhoto)
            }
        }


        with(binding) {
            topAppBarSecond.setNavigationOnClickListener {
                finish()
            }

            btCamera.setOnClickListener {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraLauncher.launch(cameraIntent)
            }

            btImage.setOnClickListener {
                galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            val categoryItems = resources.getStringArray(R.array.try_on)
            val categoryAdapter = ArrayAdapter(baseContext, R.layout.list_item_delivery, categoryItems)
            edCategory.setAdapter(categoryAdapter)
            setItem()

            btRun.setOnClickListener {
                if (imageUri == null) {
                    showToast("Need to upload first!")
                    return@setOnClickListener
                }
                val category = edCategory.text.toString()
                val moveIntent = Intent(baseContext, TryOnResultActivity::class.java)
                if (category.isEmpty()) {
                    showToast("Need to choose category first!")
                    return@setOnClickListener
                }
                moveIntent.putExtra(TryOnResultActivity.PRODUCT_ID, productId.toString())
                moveIntent.putExtra(TryOnResultActivity.PRODUCT_NAME, productName)
                moveIntent.putExtra(TryOnResultActivity.OWNER_NAME, ownerName)
                moveIntent.putExtra(TryOnResultActivity.RENT_PRICE, rentPrice.toString())
                moveIntent.putExtra(TryOnResultActivity.RETAIL_PRICE, rentPrice.toString())
                moveIntent.putExtra(TryOnResultActivity.IMAGE_URL, imageUrl)
                moveIntent.putExtra(TryOnResultActivity.IMAGE_URI, imageUri.toString())
                moveIntent.putExtra(TryOnResultActivity.CATEGORY, category)
                startActivity(moveIntent)
            }
        }
    }

    private fun setItem() {
        with(binding) {
            tvName.text = productName
            tvOwner.text = ownerName
            tvRetailPrice.paintFlags = tvRetailPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            tvRetailPrice.text = String.format(getString(R.string.retail_price_cart), retailPrice.withCurrencyFormat())
            tvPrice.text = String.format(getString(R.string.rent_price_cart), rentPrice.withCurrencyFormat())
            Glide.with(ivPhoto.context)
                .load("https://storage.googleapis.com/img-product-amoure/13_0.jpg")
                .into(ivPhoto)
            Glide.with(ivProduct.context)
                .load(imageUrl)
                .into(ivProduct)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    companion object {
        const val PRODUCT_ID = "product_id"
        const val PRODUCT_NAME = "product_name"
        const val OWNER_NAME = "owner_name"
        const val RENT_PRICE = "rent_price"
        const val RETAIL_PRICE = "retail_price"
        const val IMAGE_URL = "image_url"
    }
}