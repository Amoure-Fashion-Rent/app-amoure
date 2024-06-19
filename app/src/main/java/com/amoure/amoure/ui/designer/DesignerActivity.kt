package com.amoure.amoure.ui.designer

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.amoure.amoure.databinding.ActivityDesignerBinding
import com.amoure.amoure.ui.LoadingStateAdapter
import com.amoure.amoure.ui.ProductMediumAdapter
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.product.ProductActivity
import kotlin.properties.Delegates

class DesignerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDesignerBinding
    private val designerViewModel by viewModels<DesignerViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var designerName: String
    private var ownerId by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        designerName = intent.getStringExtra(DESIGNER_NAME).toString()
        ownerId = intent.getStringExtra(OWNER_ID).toString().toInt()

        binding = ActivityDesignerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        designerViewModel.getProducts(ownerId)

        binding.topAppBarSecond.setNavigationOnClickListener {
            finish()
        }
        binding.topAppBarSecond.title = designerName
        setProducts()
    }

    private fun setProducts() {
        binding.rvDesigner.layoutManager = GridLayoutManager(this, 2)
        val adapter = ProductMediumAdapter()
        binding.rvDesigner.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
        )
        adapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
        }
        designerViewModel.products.observe(this) {
            it?.let {
                adapter.submitData(lifecycle, it)
            }
        }
        adapter.setOnItemClickCallback(object : ProductMediumAdapter.OnItemClickCallback {
            override fun onItemClicked(id: Int) {
                val moveIntent = Intent(baseContext, ProductActivity::class.java)
                moveIntent.putExtra(ProductActivity.ID, id.toString())
                startActivity(moveIntent)
            }
        })
    }

    companion object {
        const val DESIGNER_NAME = "designer_name"
        const val OWNER_ID = "owner_id"
    }
}