package com.example.smartmedicinebox.firebase

import com.example.smartmedicinebox.activities.SignInActivity
import com.example.smartmedicinebox.activities.SignUpActivity
import com.example.smartmedicinebox.models.User
import com.example.smartmedicinebox.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


//here we will have all the firestore dependent code

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()


    fun registerUser(activity: SignUpActivity, userInfo: User){

        //we'll store the details of every user in a document
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

            activity.userRegisteredSuccess()
        }

    }

    fun signInUser(activity: SignInActivity){

        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                val loggedInUser = document.toObject(User :: class.java)
                if(loggedInUser != null)
                    activity.signInSuccess(loggedInUser)
            }


    }

    fun getCurrentUserID() : String {

        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID= ""
        if(currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

}