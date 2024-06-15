package com.amoure.amoure.ui.product

import android.content.Intent
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.databinding.ActivityProductBinding
import com.amoure.amoure.getDummyProduct
import com.amoure.amoure.getDummyProducts
import com.amoure.amoure.ui.ProductSmallAdapter
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.cart.CartActivity
import com.amoure.amoure.ui.designer.DesignerActivity
import com.amoure.amoure.ui.review.ReviewActivity
import com.amoure.amoure.withCurrencyFormat
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper

class ProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBinding
    private val productViewModel by viewModels<ProductViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var id: String
    private lateinit var thisProduct: ProductItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra(ID).toString()

        productViewModel.product.observe(this) {
            setProduct(it)
        }
        // TODO: Remove!
        setProduct(getDummyProduct())
        setSimilarItems(getDummyProducts())

        productViewModel.similarItems.observe(this) {
            setSimilarItems(it)
        }

        productViewModel.isError.observe(this) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        productViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        setupAction()
    }

    private fun setupAction() {
        with(binding) {
            btBack.setOnClickListener {
                finish()
            }

            btSeeAllReviews.setOnClickListener {
                val moveIntent = Intent(baseContext, ReviewActivity::class.java)
                moveIntent.putExtra(ReviewActivity.PRODUCT_ID, thisProduct.id)
                moveIntent.putExtra(ReviewActivity.PRODUCT_NAME, thisProduct.productName)
                moveIntent.putExtra(ReviewActivity.OWNER_NAME, thisProduct.ownerName)
                moveIntent.putExtra(ReviewActivity.RATING, thisProduct.rating.toString())
                moveIntent.putExtra(ReviewActivity.IMAGE_URL, thisProduct.imgProduct?.get(0))
                startActivity(moveIntent)
            }

            btSeeAllItems.setOnClickListener {
                val moveIntent = Intent(baseContext, DesignerActivity::class.java)
                moveIntent.putExtra(DesignerActivity.DESIGNER_NAME, thisProduct.ownerName)
                moveIntent.putExtra(DesignerActivity.OWNER_ID, thisProduct.userId)
                startActivity(moveIntent)
            }

            btRentNow.setOnClickListener {
                val moveIntent = Intent(baseContext, CartActivity::class.java)
                moveIntent.putExtra(CartActivity.PRODUCT_ID, thisProduct.id)
                moveIntent.putExtra(CartActivity.PRODUCT_NAME, thisProduct.productName)
                moveIntent.putExtra(CartActivity.OWNER_NAME, thisProduct.ownerName)
                moveIntent.putExtra(CartActivity.RENT_PRICE, thisProduct.rentPrice.toString())
                moveIntent.putExtra(CartActivity.IMAGE_URL, thisProduct.imgProduct?.get(0))
                startActivity(moveIntent)
            }
        }
    }

    private fun setProduct(product: ProductItem) {
        with(binding) {
            thisProduct = product
            val carouselLayoutManager = CarouselLayoutManager()
            carouselLayoutManager.setCarouselAlignment(CarouselLayoutManager.ALIGNMENT_CENTER)
            rvProductImage.setLayoutManager(carouselLayoutManager)
            val snapHelper = CarouselSnapHelper()
            snapHelper.attachToRecyclerView(rvProductImage)
            val productImageAdapter = ProductImageAdapter()
            productImageAdapter.submitList(product.imgProduct)
            rvProductImage.adapter = productImageAdapter

            tvProductName.text = product.productName
            tvProductOwner.text = product.ownerName
            if (product.available == true) {
                tvProductIsAvailable.text = getString(R.string.available)
                tvProductIsAvailable.setTextColor(getColor(R.color.black))
            } else {
                tvProductIsAvailable.text = getString(R.string.not_available)
                tvProductIsAvailable.setTextColor(getColor(R.color.red_500))
            }
            tvProductRetailPrice.text = product.retailPrice?.withCurrencyFormat()
            tvProductRetailPrice.paintFlags = tvProductRetailPrice.paintFlags or STRIKE_THRU_TEXT_FLAG
            tvProductRentPrice.text = product.rentPrice?.withCurrencyFormat()
            tvProductDetails.text = product.description
            tvStylishNotes.text = product.styleNotes
        }
    }

    private fun setSimilarItems(products: List<ProductItem?>) {
        binding.rvSimilarItems.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        val productSmallAdapter = ProductSmallAdapter()
        productSmallAdapter.submitList(products)
        binding.rvSimilarItems.adapter = productSmallAdapter
        productSmallAdapter.setOnItemClickCallback(object : ProductSmallAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
            val moveIntent = Intent(baseContext, ProductActivity::class.java)
            moveIntent.putExtra(ID, id)
            startActivity(moveIntent)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ID = "id"
    }
}