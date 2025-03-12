package com.example.worka1.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.worka1.MainActivity
import com.example.worka1.databinding.ActivitySplashScreenBinding
import com.example.worka1.ui.authentication.LogInActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val isOnboardingCompleted = sharedPreferences.getBoolean("is_onboarding_completed", false)

        lifecycleScope.launch {
            delay(3000)
            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // If user is logged in, redirect to MainActivity
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            } else {
                // If user is not logged in, go to LogInActivity
                if(isOnboardingCompleted){
                    startActivity(Intent(this@SplashScreen, LogInActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this@SplashScreen, OnBoardingActivity::class.java))
                    finish()
                }
            }
        }
    }
}