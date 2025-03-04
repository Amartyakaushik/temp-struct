package com.example.worka1.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.worka1.databinding.ActivityOnBoardingBinding
import com.example.worka1.ui.authentication.LogInActivity
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class onBoardingActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOnBoardingBinding
    private lateinit var dotsIndicator: WormDotsIndicator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nextBtn = binding.buttonNext
        val skipBtn = binding.buttonSkip
        nextBtn.setOnClickListener{
            val intent = Intent(this, onBoardingActivity2::class.java)
            startActivity(intent)
        }
        skipBtn.setOnClickListener{
            // Set the onboarding completion flag to true when the user skips the onBoarding screen
            val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("is_onboarding_completed", true).apply()
            startActivity(Intent(this, LogInActivity::class.java))
        }

    }
}