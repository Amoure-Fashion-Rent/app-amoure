package com.amoure.amoure.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.amoure.amoure.R
import com.amoure.amoure.data.request.RegisterRequest
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.databinding.ActivityRegisterBinding
import com.amoure.amoure.isEmailValid
import com.amoure.amoure.isPasswordValid
import com.amoure.amoure.ui.login.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()

        registerViewModel.response.observe(this) { response ->
            showAlert(response)
        }

        registerViewModel.isLoading.observe(this) {
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
        val userTypeItems = resources.getStringArray(R.array.user_type_items)
        val userTypeAdapter = ArrayAdapter(this, R.layout.list_item_delivery, userTypeItems)
        binding.edRegisterType.setAdapter(userTypeAdapter)
        with(binding) {
            btRegister.setOnClickListener {
                val fullName = edRegisterName.text.toString()
                if (fullName.isEmpty()) {
                    edlRegisterName.error = String.format(getString(R.string.input_required), "name")
                    return@setOnClickListener
                } else {
                    edlRegisterName.isErrorEnabled = false
                }
                val email = edRegisterEmail.text.toString()
                if (email.isEmpty() || !isEmailValid(email)) {
                    edlRegisterEmail.error = String.format(getString(R.string.input_required), "email")
                    return@setOnClickListener
                } else {
                    edlRegisterEmail.isErrorEnabled = false
                }
                val password = edRegisterPassword.text.toString()
                if (password.isEmpty() || !isPasswordValid(password)) {
                    edlRegisterPassword.error = String.format(getString(R.string.input_required), "password")
                    return@setOnClickListener
                } else {
                    edlRegisterPassword.isErrorEnabled = false
                }
                val passwordConf = edRegisterPasswordConf.text.toString()
                if (passwordConf.isEmpty() || !isPasswordValid(passwordConf)) {
                    edlRegisterPasswordConf.error = String.format(getString(R.string.input_required), "password")
                    return@setOnClickListener
                } else if (passwordConf != password) {
                    edlRegisterPasswordConf.error = getString(R.string.password_not_match_error)
                    return@setOnClickListener
                } else {
                    edlRegisterPasswordConf.isErrorEnabled = false
                }
                val type = edRegisterType.text.toString().uppercase()

                registerViewModel.register(RegisterRequest(fullName, email, password, type))
            }
            btBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun showAlert(response: InitialResponse<IdResponse>) {
        if (response.message == "OK") {
            Toast.makeText(
                this,
                resources.getString(R.string.register_alert_title_success),
                Toast.LENGTH_SHORT
            ).show()
            val moveIntent = Intent(baseContext, LoginActivity::class.java)
            startActivity(moveIntent)
        } else {
            MaterialAlertDialogBuilder(this).apply {
                setTitle(resources.getString(R.string.register_alert_title_error))
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