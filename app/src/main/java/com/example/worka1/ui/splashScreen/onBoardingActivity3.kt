package com.example.worka1.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.worka1.R
import com.example.worka1.databinding.ActivityOnBoarding3Binding
import com.example.worka1.ui.authentication.LogInActivity

class onBoardingActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoarding3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoarding3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val nextBtn = binding.buttonNext
        nextBtn.setOnClickListener{
            // Set the onboarding completion flag to true
            val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("is_onboarding_completed", true).apply()
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }
}