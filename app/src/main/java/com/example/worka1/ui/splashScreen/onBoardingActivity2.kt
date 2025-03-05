package com.example.worka1.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.worka1.MainActivity
import com.example.worka1.databinding.ActivityOnBoarding2Binding
import com.example.worka1.ui.authentication.LogInActivity

class OnBoardingActivity2 : AppCompatActivity() {
    private lateinit var binding : ActivityOnBoarding2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoarding2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val nextBtn = binding.buttonNext
        val skipBtn = binding.buttonSkip
        nextBtn.setOnClickListener{val intent = Intent(this, OnBoardingActivity3::class.java)
            startActivity(intent)
        }
        skipBtn.setOnClickListener{
            // Set the onboarding completion flag to true
            val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("is_onboarding_completed", true).apply()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}