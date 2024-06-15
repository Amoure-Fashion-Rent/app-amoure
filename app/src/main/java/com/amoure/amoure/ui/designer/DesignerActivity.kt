package com.amoure.amoure.ui.designer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.databinding.ActivityDesignerBinding
import com.amoure.amoure.ui.ProductMediumAdapter
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.product.ProductActivity

class DesignerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDesignerBinding
    private val designerViewModel by viewModels<DesignerViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var designerName: String
    private lateinit var ownerId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        designerName = intent.getStringExtra(DESIGNER_NAME).toString()
        ownerId = intent.getStringExtra(OWNER_ID).toString()

        binding = ActivityDesignerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        designerViewModel.products.observe(this) {
            it?.let {
                setProducts(it)
            }
        }

        designerViewModel.isError.observe(this) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        designerViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setProducts(products: List<ProductItem?>) {
        binding.rvDesigner.layoutManager = GridLayoutManager(this, 2)
        binding.topAppBarSecond.title = designerName
        val adapter = ProductMediumAdapter()
        adapter.submitList(products)
        binding.rvDesigner.adapter = adapter
        adapter.setOnItemClickCallback(object : ProductMediumAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                val moveIntent = Intent(baseContext, ProductActivity::class.java)
                moveIntent.putExtra(ProductActivity.ID, id)
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
        const val DESIGNER_NAME = "designer_name"
        const val OWNER_ID = "owner_id"
    }
}