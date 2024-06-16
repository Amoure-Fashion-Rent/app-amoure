package com.amoure.amoure.ui.renthistory

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.data.response.RentItem
import com.amoure.amoure.databinding.ActivityRentHistoryBinding
import com.amoure.amoure.ui.ViewModelFactory

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

        rentHistoryViewModel.rents.observe(this) {
            it?.let {
                setRents(it)
            }
        }

        rentHistoryViewModel.isError.observe(this) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        rentHistoryViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.topAppBarSecond.setNavigationOnClickListener {
            finish()
        }
    }


    private fun setRents(rents: List<RentItem?>) {
        val adapter = RentHistoryAdapter()
        adapter.submitList(rents)
        binding.rvRents.adapter = adapter
        // TODO: match with backend is it possible to click the rent item or not
//        adapter.setOnItemClickCallback(object : ProductMediumAdapter.OnItemClickCallback {
//            override fun onItemClicked(id: String) {
//                val moveIntent = Intent(this, ProductActivity::class.java)
//                moveIntent.putExtra(ProductActivity.ID, id)
//                startActivity(moveIntent)
//            }
//        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}