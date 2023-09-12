package com.example.smartmedicinebox.activities

import android.app.Activity
import android.os.Bundle
import com.example.smartmedicinebox.R
import com.example.smartmedicinebox.databinding.ActivityMyProfileBinding
import com.example.smartmedicinebox.firebase.FirestoreClass
import com.example.smartmedicinebox.models.User
import com.example.smartmedicinebox.utils.Constants

class MyProfileActivity : BaseActivity() {

    private lateinit var mUserDetails: User

    private lateinit var binding: ActivityMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        setUpActionBar()

        FirestoreClass().loadUserData(this)

        binding.update.setOnClickListener{

            showProgressDialog(resources.getString(R.string.please_wait))
            updateUserProfileData()
        }
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

        mUserDetails = user

        binding.etName.setText(user.name)
        binding.etEmail.setText(user.email)
    }


    fun profileUpdateSuccess(){
        hideProgressDialog()

        setResult(Activity.RESULT_OK)
    }

    fun updateUserProfileData(){

        val userHashMap = HashMap<String, Any>()

        var anyChangesMade = false

        if(binding.etName.text.toString() !=  mUserDetails.name){
            userHashMap[Constants.NAME] = binding.etName.text.toString()
            anyChangesMade = true

        }

        if(anyChangesMade){
            FirestoreClass().updateUserProfileData(this, userHashMap)
        }
    }
}