package com.example.smartmedicinebox.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmedicinebox.R
import com.example.smartmedicinebox.databinding.ActivityMyProfileBinding
import com.example.smartmedicinebox.firebase.FirestoreClass
import com.example.smartmedicinebox.models.User

class MyProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        setUpActionBar()

        FirestoreClass().loadUserData(this)
    }


    private fun setUpActionBar(){

        val toolbarMyProfileActivity = binding.toolbarMyProfileActivity

        setSupportActionBar(toolbarMyProfileActivity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_colour_back_24dp)
            actionBar.title = resources.getString(R.string.my_profile_title)
        }

        toolbarMyProfileActivity.setNavigationOnClickListener{onBackPressed()}

    }

    //loading the user details in MyProfileActivity
    fun setUserDataInUI(user: User){

        binding.etName.setText(user.name)
        binding.etEmail.setText(user.email)



    }
}