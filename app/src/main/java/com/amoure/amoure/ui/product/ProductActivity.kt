package com.amoure.amoure.ui.product

import android.content.Intent
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.databinding.ActivityProductBinding
import com.amoure.amoure.ui.ProductSmallAdapter
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.cart.CartActivity
import com.amoure.amoure.ui.designer.DesignerActivity
import com.amoure.amoure.ui.review.ReviewActivity
import com.amoure.amoure.ui.tryon.TryOnInfoActivity
import com.amoure.amoure.withCurrencyFormat
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.properties.Delegates

class ProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBinding
    private val productViewModel by viewModels<ProductViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var id by Delegates.notNull<Int>()
    private lateinit var thisProduct: ProductItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setVisibility(false)

        id = intent.getStringExtra(ID).toString().toInt()

        productViewModel.getProduct(id)
        productViewModel.product.observe(this) {
            setProduct(it)
            setVisibility(true)
            it.similarProducts?.products?.let { it1 -> setSimilarItems(it1) }
        }

        productViewModel.isError.observe(this) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        productViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        productViewModel.wishlistResponse.observe(this) {
            showAlertWishlist(it)
        }
        setupAction()
    }

    private fun setVisibility(visible: Boolean) {
        binding.svProduct.isVisible = visible
    }

    private fun setupAction() {
        with(binding) {
            btBack.setOnClickListener {
                finish()
            }

            btRentNow.setOnClickListener {
                val moveIntent = Intent(baseContext, CartActivity::class.java)
                moveIntent.putExtra(CartActivity.PRODUCT_ID, thisProduct.id.toString())
                moveIntent.putExtra(CartActivity.PRODUCT_NAME, thisProduct.name)
                moveIntent.putExtra(CartActivity.OWNER_NAME, thisProduct.owner?.fullName)
                moveIntent.putExtra(CartActivity.RENT_PRICE, thisProduct.rentPrice.toString())
                moveIntent.putExtra(CartActivity.IMAGE_URL, thisProduct.images?.get(0))
                moveIntent.putExtra(CartActivity.SIZE, thisProduct.size)
                moveIntent.putExtra(CartActivity.COLOR, thisProduct.color)
                startActivity(moveIntent)
            }

            btSeeAllReviews.setOnClickListener {
                val moveIntent = Intent(baseContext, ReviewActivity::class.java)
                moveIntent.putExtra(ReviewActivity.PRODUCT_ID, thisProduct.id.toString())
                moveIntent.putExtra(ReviewActivity.PRODUCT_NAME, thisProduct.name)
                moveIntent.putExtra(ReviewActivity.OWNER_NAME, thisProduct.owner?.fullName)
                moveIntent.putExtra(ReviewActivity.RATING, thisProduct.avg?.rating?.toString() ?: "0.0")
                moveIntent.putExtra(ReviewActivity.IMAGE_URL, thisProduct.images?.get(0))
                startActivity(moveIntent)
            }

            btSeeAllItems.setOnClickListener {
                val moveIntent = Intent(baseContext, DesignerActivity::class.java)
                moveIntent.putExtra(DesignerActivity.DESIGNER_NAME, thisProduct.owner?.fullName)
                moveIntent.putExtra(DesignerActivity.OWNER_ID, thisProduct.owner?.id.toString())
                startActivity(moveIntent)
            }

            btAddWishlist.setOnClickListener {
                productViewModel.postWishlist(id)
            }

            btTryOn.setOnClickListener {
                val moveIntent = Intent(baseContext, TryOnInfoActivity::class.java)
                moveIntent.putExtra(TryOnInfoActivity.PRODUCT_ID, thisProduct.id.toString())
                moveIntent.putExtra(TryOnInfoActivity.PRODUCT_NAME, thisProduct.name)
                moveIntent.putExtra(TryOnInfoActivity.OWNER_NAME, thisProduct.owner?.fullName)
                moveIntent.putExtra(TryOnInfoActivity.RENT_PRICE, thisProduct.rentPrice.toString())
                moveIntent.putExtra(TryOnInfoActivity.RETAIL_PRICE, thisProduct.retailPrice.toString())
                moveIntent.putExtra(TryOnInfoActivity.IMAGE_URL, thisProduct.images?.get(0))
                startActivity(moveIntent)
            }
        }
    }

    private fun showAlertWishlist(response: InitialResponse<IdResponse>) {
        if (response.message == "OK") {
            MaterialAlertDialogBuilder(this).apply {
                setTitle(resources.getString(R.string.add_wishlist))
                setPositiveButton(resources.getString(R.string.alert_ok)) { _, _ ->
                }
                create()
                show()
            }
        } else {
            MaterialAlertDialogBuilder(this).apply {
                setTitle(resources.getString(R.string.add_wishlist_failed))
                setPositiveButton(resources.getString(R.string.alert_ok)) { _, _ ->
                }
                create()
                show()
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
            productImageAdapter.submitList(product.images)
            rvProductImage.adapter = productImageAdapter
            if (product.status != "AVAILABLE") {
                btRentNow.isEnabled = false
            }

            tvProductName.text = product.name
            tvProductOwner.text = product.owner?.fullName
            if (product.status == "AVAILABLE") {
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
            tvStylishNotes.text = product.stylishNotes
            tvRatingReviewsCount.text = String.format(getString(R.string.rating_reviews_count), product.avg?.rating?.toString() ?: "-", product.count.toString())
            if (product.avg?.rating == null) {
                return
            }
            if (product.avg.rating >= 1.0) {
                setStarColor(star1)
            }
            if (product.avg.rating >= 2.0) {
                setStarColor(star2)
            }
            if (product.avg.rating >= 3.0) {
                setStarColor(star3)
            }
            if (product.avg.rating >= 4.0) {
                setStarColor(star4)
            }
            if (product.avg.rating >= 5.0) {
                setStarColor(star5)
            }
        }
    }

    private fun setStarColor(star: ImageView) {
        star.setColorFilter(ContextCompat.getColor(baseContext, R.color.yellow_500))
    }

    private fun setSimilarItems(products: List<ProductItem?>) {
        binding.rvSimilarItems.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        val productSmallAdapter = ProductSmallAdapter()
        productSmallAdapter.submitList(products)
        binding.rvSimilarItems.adapter = productSmallAdapter
        productSmallAdapter.setOnItemClickCallback(object : ProductSmallAdapter.OnItemClickCallback {
            override fun onItemClicked(id: Int) {
            val moveIntent = Intent(baseContext, ProductActivity::class.java)
            moveIntent.putExtra(ID, id.toString())
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