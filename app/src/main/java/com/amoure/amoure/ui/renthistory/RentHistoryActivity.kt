package com.amoure.amoure.ui.renthistory

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.amoure.amoure.databinding.ActivityRentHistoryBinding
import com.amoure.amoure.ui.LoadingStateAdapter
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.product.ProductActivity

class RentHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRentHistoryBinding
    private val rentHistoryViewModel by viewModels<RentHistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRentHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvRents.layoutManager = LinearLayoutManager(this)

        binding.topAppBarSecond.setNavigationOnClickListener {
            finish()
        }
        setRents()
    }

    private fun setRents() {
        val adapter = RentHistoryAdapter()
        binding.rvRents.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        adapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.tvNoDataRent.isVisible = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
        }
        rentHistoryViewModel.rents.observe(this) {
            it?.let {
                adapter.submitData(lifecycle, it)
            }
        }
        adapter.setOnItemClickCallback(object : RentHistoryAdapter.OnItemClickCallback {
            override fun onItemClicked(id: Int) {
                val moveIntent = Intent(baseContext, ProductActivity::class.java)
                moveIntent.putExtra(ProductActivity.ID, id.toString())
                startActivity(moveIntent)
            }
        })
    }
}