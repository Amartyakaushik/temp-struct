package com.example.worka1.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.worka1.MainActivity
import com.example.worka1.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Use SharedPreferences to check if onboarding is completed
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val isOnboardingCompleted = sharedPreferences.getBoolean("is_onboarding_completed", false)

        lifecycleScope.launch {
            delay(3000)
            if(isOnboardingCompleted){
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this@SplashScreen, OnBoardingActivity::class.java))
                finish()
            }
        }

    }
}