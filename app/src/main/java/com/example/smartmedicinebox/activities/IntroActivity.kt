package com.example.smartmedicinebox.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.example.smartmedicinebox.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {

    private lateinit var binding: ActivityIntroBinding       //this will help us in getting rid of using findViewById everytime.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.btnSignUpIntro.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))

        }
        binding.btnSignInIntro.setOnClickListener{
           startActivity(Intent(this, SignInActivity::class.java))

       }
    }
}