package com.example.worka1.ui.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.worka1.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val signUpBtn = binding.signUpBtn
        signUpBtn.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }

    }
}