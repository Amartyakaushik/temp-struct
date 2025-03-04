package com.example.worka1.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.worka1.R
import com.example.worka1.databinding.ActivityOnBoarding2Binding
import com.example.worka1.ui.authentication.LogInActivity
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class onBoardingActivity2 : AppCompatActivity() {
    private lateinit var binding : ActivityOnBoarding2Binding
    private lateinit var dotsIndicator: WormDotsIndicator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoarding2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val nextBtn = binding.buttonNext
        val skipBtn = binding.buttonSkip
        nextBtn.setOnClickListener{val intent = Intent(this, onBoardingActivity3::class.java)
            startActivity(intent)
        }
        skipBtn.setOnClickListener{
            // Set the onboarding completion flag to true
            val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("is_onboarding_completed", true).apply()
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }
}