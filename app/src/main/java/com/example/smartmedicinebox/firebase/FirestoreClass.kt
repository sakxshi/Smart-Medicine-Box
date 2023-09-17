package com.example.smartmedicinebox.firebase

import android.app.Activity
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.smartmedicinebox.activities.CreateBoardActivity
import com.example.smartmedicinebox.activities.DetailsToDisplayActivity
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

    //to register the user in the database
    fun registerUser(activity: SignUpActivity, userInfo: User){

        //we're storing the details of every user in a document
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

            activity.userRegisteredSuccess()
        }

    }

    //function to create a boardsList ArrayList
    //as we create new boards, they get added in the ArrayList
    fun getBoardsList(activity: MainActivity){
        mFirestore.collection(Constants.BOARDS)
            //show the documents only if they are assigned to the signed in user
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                // Here we get the list of boards in the form of documents.
                Log.e(activity.javaClass.simpleName, document.documents.toString())
               //boardsList will contain all the boards that we have created
                val boardsList: ArrayList<Board> = ArrayList()

                // A for loop to convert into Boards ArrayList.
                for (i in document.documents) {

                    val board = i.toObject(Board::class.java)!!
                    board.documentID = i.id

                    boardsList.add(board)
                }

                activity.populateBoardsListToUI(boardsList)
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
            }
    }

    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>){          //In java what we call object, Kotlin we call it Any

        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                Toast.makeText(activity, "Profile Updated Successfully", Toast.LENGTH_LONG).show()
                activity.profileUpdateSuccess()
            }.addOnFailureListener{
                Toast.makeText(activity, "Profile update failed", Toast.LENGTH_LONG).show()
            }

    }

    fun loadUserData(activity: Activity, readBoardsList: Boolean = false){

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
                            activity.updateNavigationUserDetails(loggedInUser, readBoardsList)  //if the activity passed is the MainActivity, update the user's details (in our case only name)
                        }
                        is MyProfileActivity ->{
                            activity.setUserDataInUI(loggedInUser)              //if the activity passed is MyProfileActivity, load the user's details
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

    @RequiresApi(Build.VERSION_CODES.M)
    fun getBoardDetails(activity: DetailsToDisplayActivity, documentID: String){

        mFirestore.collection(Constants.BOARDS)
            .document(documentID)
            .get()
            .addOnSuccessListener { document ->
                // Here we get the list of boards in the form of documents.
                Log.e(activity.javaClass.simpleName, document.toString())
                //boardsList will contain all the boards that we have created

                activity.boardDetails(document.toObject(Board::class.java)!!)
                activity.setDetailsInUI(document.toObject(Board::class.java)!!)
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
            }


    }

}