package com.example.smartmedicinebox.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.example.smartmedicinebox.R
import com.example.smartmedicinebox.databinding.ActivitySignInBinding
import com.example.smartmedicinebox.firebase.FirestoreClass
import com.example.smartmedicinebox.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException


class SignInActivity : BaseActivity() {

    val mFirestore = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth     //now we can use auth like an object and access Firebase easily and with lesser lines of code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //for accessing Firebase
        auth = FirebaseAuth.getInstance()


        setUpActionBar()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.btnSignIn.setOnClickListener {
            signInRegisteredUser()
            //checkIfCollectionExists(Constants.USERS)

        }

    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarSignInActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_colour_back_24dp)
        }

        binding.toolbarSignInActivity.setNavigationOnClickListener { onBackPressed() }

    }

    private fun signInRegisteredUser() {
        val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
        val password: String = binding.etPassword.text.toString().trim { it <= ' ' }

        //first check if the email and password are entered or not
        if (validateForm(email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))

            //now we can run the code from Firebase docs
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        FirestoreClass().loadUserData(this)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Sign In", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter an email address")
                false
            }

            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password")
                false
            }

            else -> true

        }
    }

    fun signInSuccess(user: User) {

        hideProgressDialog()
        Toast.makeText(this, "User signed in successfully", Toast.LENGTH_LONG).show()
        //when signing in is successful, move to the MainActivity
        startActivity(Intent(this, MainActivity::class.java))
        finish()        //so that the user cannot get back to the SignInActivity
    }

    //firebase me docs nai dikh rhe
    fun checkIfCollectionExists(collectionName: String) {
        val collectionRef = mFirestore.collection(collectionName)

        // Perform a query on the collection with a limit of 1 document
        collectionRef.limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Collection does not exist
                    Log.e("Tag", "Collection $collectionName does not exist")
                } else {
                    // Collection exists
                    Log.e("Tag", "Collection $collectionName exists")
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occurred during the query
                if (exception is FirebaseFirestoreException) {
                    Log.e("Tag", "Error in checking collection")
                }
            }
    }
}