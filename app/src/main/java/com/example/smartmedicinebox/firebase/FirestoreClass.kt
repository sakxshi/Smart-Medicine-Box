package com.example.smartmedicinebox.firebase

import android.app.Activity
import android.widget.Toast
import com.example.smartmedicinebox.activities.CreateBoardActivity
import com.example.smartmedicinebox.activities.MainActivity
import com.example.smartmedicinebox.activities.MyProfileActivity
import com.example.smartmedicinebox.activities.SignInActivity
import com.example.smartmedicinebox.activities.SignUpActivity
import com.example.smartmedicinebox.models.Board
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

    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>){          //In hava what we call object, Kotlin we call it Any

        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                Toast.makeText(activity, "Profile Updated Successfully", Toast.LENGTH_LONG).show()
                activity.profileUpdateSuccess()
            }.addOnFailureListener{
                Toast.makeText(activity, "Profile updation faield", Toast.LENGTH_LONG).show()
            }

    }

    fun loadUserData(activity: Activity){

        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User :: class.java)
                if(loggedInUser != null) {

                    when (activity) {
                        is SignInActivity -> {
                            activity.signInSuccess(loggedInUser)                //if the activity passed is SignInActivity, sign in the user
                        }
                        is MainActivity ->{
                            activity.updateNavigationUserDetails(loggedInUser)  //if the activity passed is the MainActivity, update the user's details
                        }
                        is MyProfileActivity ->{
                            activity.setUserDataInUI(loggedInUser)              //if the activity passed is MyProileActivity, load the user's details

                        }
                    }
                }
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

    fun createBoard(activity: CreateBoardActivity, board: Board){
        mFirestore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(activity, "Board created successfully", Toast.LENGTH_LONG).show()
                activity.boardCreatedSuccessfully()
            }
            .addOnFailureListener{
                Toast.makeText(activity, "Board could not be created", Toast.LENGTH_LONG).show()
            }
    }

}