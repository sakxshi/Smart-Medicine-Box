package com.example.smartmedicinebox.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmedicinebox.R
import com.example.smartmedicinebox.firebase.FirestoreClass

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //post delayed is used to run the runnable after a specified amount of delay
        Handler().postDelayed({

            var currentUserID = FirestoreClass().getCurrentUserID()

            //auto login
            if(currentUserID.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))  //if the user is already registered go directly to the main activity
            }
            else{
                startActivity(Intent(this, IntroActivity::class.java)) 
            }

            finish()                     //so that the user is not able to come back to the splash activity

        }, 3500)
    }
}