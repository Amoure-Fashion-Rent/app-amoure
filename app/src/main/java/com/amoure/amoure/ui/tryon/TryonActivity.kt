package com.amoure.amoure.ui.tryon

import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amoure.amoure.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout

class TryonActivity : AppCompatActivity() {

    private lateinit var tvCategory: TextView
    private lateinit var edlCategory: TextInputLayout
    private lateinit var edCategory: AutoCompleteTextView
    private lateinit var ivAvatar: ShapeableImageView
    private lateinit var btCamera: Button
    private lateinit var btImage: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var topAppBarSecond: MaterialToolbar
    private lateinit var btAddProduct: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tryon_info) // Use the correct layout file

        // Initialize views
        tvCategory = findViewById(R.id.tv_category)
        edlCategory = findViewById(R.id.edl_category)
        edCategory = findViewById(R.id.ed_category)
        ivAvatar = findViewById(R.id.iv_avatar)
        btCamera = findViewById(R.id.bt_camera)
        btImage = findViewById(R.id.bt_image)
        progressBar = findViewById(R.id.progress_bar)
        topAppBar = findViewById(R.id.top_app_bar)
        topAppBarSecond = findViewById(R.id.top_app_bar_second)
        btAddProduct = findViewById(R.id.bt_add_product)

        // Set up the top app bar
        topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
            onBackPressed()
        }

        topAppBarSecond.setNavigationOnClickListener {
            // Handle back icon press
            onBackPressed()
        }

        // Set listeners for buttons
        btCamera.setOnClickListener {
            // Handle camera button click
        }

        btImage.setOnClickListener {
            // Handle image button click
        }

        btAddProduct.setOnClickListener {
            // Handle add product button click
        }


    }

//    private fun setupDropdown() {
//        val tryOnItems = resources.getStringArray(R.array.try_on)
//        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tryOnItems)
//        edCategory.setAdapter(adapter)
//    }

    private fun showProgress(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}