package com.amoure.amoure.ui.tryon

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amoure.amoure.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.imageview.ShapeableImageView

class TryonResultActivity : AppCompatActivity() {

    private lateinit var ivAvatar: ShapeableImageView
    private lateinit var btAddProduct: Button
    private lateinit var ivProduct: ShapeableImageView
    private lateinit var tvName: TextView
    private lateinit var tvOwner: TextView
    private lateinit var tvRetailPrice: TextView
    private lateinit var tvPrice: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var topAppBarSecond: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tryon_result)

        // Initialize views
        ivAvatar = findViewById(R.id.iv_avatar)
        btAddProduct = findViewById(R.id.bt_add_product)
        ivProduct = findViewById(R.id.iv_product)
        tvName = findViewById(R.id.tv_name)
        tvOwner = findViewById(R.id.tv_owner)
        tvRetailPrice = findViewById(R.id.tv_retail_price)
        tvPrice = findViewById(R.id.tv_price)
        progressBar = findViewById(R.id.progress_bar)
        topAppBar = findViewById(R.id.top_app_bar)
        topAppBarSecond = findViewById(R.id.top_app_bar_second)

        // Set up the top app bar
        topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
            onBackPressed()
        }

        topAppBarSecond.setNavigationOnClickListener {
            // Handle back icon press
            onBackPressed()
        }

        // Set listener for the retry button
        btAddProduct.setOnClickListener {
            // Handle retry button click
        }
    }

    private fun showProgress(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}
