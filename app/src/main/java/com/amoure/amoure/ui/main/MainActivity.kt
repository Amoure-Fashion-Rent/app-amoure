package com.amoure.amoure.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.amoure.amoure.R
import com.amoure.amoure.databinding.ActivityMainBinding
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.start.StartActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var role: String = "USER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)

        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin || user.accessToken.isEmpty()) {
                val intent = Intent(this, StartActivity::class.java)
                startActivity(intent)
                finish()
            }
            role = user.role
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        var appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_category,
                R.id.navigation_vis_search,
                R.id.navigation_wishlist,
                R.id.navigation_profile
            )
        )
        if (role == "OWNER") {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_category,
                    R.id.navigation_add,
                    R.id.navigation_profile
                )
            )
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.navigation_search) {
                navView.visibility = View.GONE
            } else {
                navView.visibility = View.VISIBLE
            }
        }
    }
}