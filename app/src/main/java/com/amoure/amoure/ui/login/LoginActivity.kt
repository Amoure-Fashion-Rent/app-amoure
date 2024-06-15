package com.amoure.amoure.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.amoure.amoure.R
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.LoginResponse
import com.amoure.amoure.databinding.ActivityLoginBinding
import com.amoure.amoure.isEmailValid
import com.amoure.amoure.isPasswordValid
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()

        loginViewModel.response.observe(this) { response ->
            showAlert(response)
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        with(binding) {
            btLogin.setOnClickListener {
                val email = edLoginEmail.text.toString()
                if (email.isEmpty() || !isEmailValid(email)) {
                    edlLoginEmail.error = String.format(getString(R.string.input_required), "email")
                }
                val password = edLoginPassword.text.toString()
                if (password.isEmpty() || !isPasswordValid(password)) {
                    edlLoginPassword.error = String.format(getString(R.string.input_required), "password")
                }
                Toast.makeText(baseContext, email+password, Toast.LENGTH_SHORT).show()
//                loginViewModel.login(LoginRequest(email, password))
            }
            btBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun showAlert(response: InitialResponse<LoginResponse>) {
        if (response.status == "success") {
            Toast.makeText(
                this,
                resources.getString(R.string.login_alert_title_success),
                Toast.LENGTH_SHORT
            ).show()
            val moveIntent = Intent(baseContext, MainActivity::class.java)
            startActivity(moveIntent)
        } else {
            MaterialAlertDialogBuilder(this).apply {
                setTitle(resources.getString(R.string.login_alert_title_error))
                setMessage(response.message ?: resources.getString(R.string.alert_error))
                setPositiveButton(resources.getString(R.string.alert_ok)) { _, _ ->
                }
                create()
                show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}