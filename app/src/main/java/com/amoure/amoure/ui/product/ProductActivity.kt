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
import com.amoure.amoure.ui.ProductSmallAdapter
import com.amoure.amoure.ui.ViewModelFactory
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper

class ProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBinding
    private val productViewModel by viewModels<ProductViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra(ID).toString()

        productViewModel.product.observe(this) {
            setProduct(it)
        }

        productViewModel.similarItems.observe(this) {
            setSimilarItems(it)
        }

        productViewModel.isError.observe(this) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        productViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setProduct(product: ProductItem) {
        with(binding) {
            rvProductImage.setLayoutManager(CarouselLayoutManager())
            val snapHelper = CarouselSnapHelper()
            snapHelper.attachToRecyclerView(rvProductImage)
            val productImageAdapter = ProductImageAdapter()
            productImageAdapter.submitList(product.imgProduct)
            rvProductImage.adapter = productImageAdapter

            tvProductName.text = product.productName
            tvProductOwner.text = product.ownerName
            tvProductColor.text = product.color
            if (product.available == true) {
                tvProductIsAvailable.text = getString(R.string.available)
                tvProductIsAvailable.setTextColor(getColor(R.color.black))
            } else {
                tvProductIsAvailable.text = getString(R.string.not_available)
                tvProductIsAvailable.setTextColor(getColor(R.color.red_500))
            }
            tvProductRetailPrice.text = product.retailPrice.toString()
            tvProductRetailPrice.paintFlags = tvProductRetailPrice.paintFlags or STRIKE_THRU_TEXT_FLAG
            tvProductRentPrice.text = product.rentPrice.toString()
            tvProductDetails.text = product.description
            tvStylishNotes.text = product.styleNotes

            btSeeAllReviews.setOnClickListener {
                // TODO: Go to review page
//                val moveIntent = Intent(context, DetailActivity::class.java)
//                moveIntent.putExtra(DetailActivity.ID, id)
//                startActivity(moveIntent)
            }

            btSeeAllItems.setOnClickListener {
                // TODO: Go to designer page
//                val moveIntent = Intent(context, DetailActivity::class.java)
//                moveIntent.putExtra(DetailActivity.ID, id)
//                startActivity(moveIntent)
            }

            btBack.setOnClickListener {
                finish()
            }
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