package com.amoure.amoure.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.amoure.amoure.R
import com.amoure.amoure.databinding.ActivityMainBinding
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.search.SearchFragment
import com.amoure.amoure.ui.start.StartActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var role: String = "USER"

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var appBarConfiguration: AppBarConfiguration
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast(getString(R.string.permission_granted))
            } else {
                showToast(getString(R.string.permission_denied))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin || user.accessToken.isEmpty()) {
                val intent = Intent(this, StartActivity::class.java)
                startActivity(intent)
                finish()
            }
            if (role != user.role) {
                role = user.role
                if (role == "OWNER") {
                    navView.menu.clear()
                    navView.inflateMenu(R.menu.bottom_nav_menu_owner)
                } else {
                    navView.menu.clear()
                    navView.inflateMenu(R.menu.bottom_nav_menu_user)
                }
            }

            setupNavigation(navController, navView)
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                val bundle = Bundle()
                bundle.putBoolean(SearchFragment.IS_VIS_SEARCH, true)
                bundle.putString(SearchFragment.IMAGE_URI, imageUri.toString())
                navController.navigate(R.id.navigation_search, bundle)
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val imageUri: Uri = uri
                val bundle = Bundle()
                bundle.putBoolean(SearchFragment.IS_VIS_SEARCH, true)
                bundle.putString(SearchFragment.IMAGE_URI, imageUri.toString())
                navController.navigate(R.id.navigation_search, bundle)
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_search) {
                navView.visibility = View.GONE
            } else {
                navView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupNavigation(navController: NavController, navView: BottomNavigationView) {
        appBarConfiguration = if (role == "OWNER") {
            AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_category,
                    R.id.navigation_add,
                    R.id.navigation_profile
                )
            )
        } else {
            AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_category,
                    R.id.navigation_vis_search,
                    R.id.navigation_wishlist,
                    R.id.navigation_profile
                )
            )
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_vis_search -> showCameraGalleryDialog()
                R.id.navigation_home -> navController.navigate(R.id.navigation_home)
                R.id.navigation_category -> navController.navigate(R.id.navigation_category)
                R.id.navigation_wishlist -> navController.navigate(R.id.navigation_wishlist)
                R.id.navigation_profile -> navController.navigate(R.id.navigation_profile)
                R.id.navigation_add -> navController.navigate(R.id.navigation_add)
            }
            false
        }
    }

    private fun showCameraGalleryDialog() {
        val options = arrayOf("Camera", "Gallery")
        MaterialAlertDialogBuilder(this).apply {
            setTitle("Choose an option")
            setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            create()
            show()
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(cameraIntent)
    }

    private fun openGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}