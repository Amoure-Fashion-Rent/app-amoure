package com.amoure.amoure.ui.start

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amoure.amoure.databinding.ActivityStartBinding
import com.amoure.amoure.ui.login.LoginActivity
import com.amoure.amoure.ui.register.RegisterActivity

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btLogin.setOnClickListener {
                val moveIntent = Intent(baseContext, LoginActivity::class.java)
                startActivity(moveIntent)
            }
            btRegister.setOnClickListener {
                val moveIntent = Intent(baseContext, RegisterActivity::class.java)
                startActivity(moveIntent)
            }
        }
    }
}