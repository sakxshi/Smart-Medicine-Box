package com.example.smartmedicinebox.activities

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.smartmedicinebox.R
import com.example.smartmedicinebox.databinding.ActivityDetailsToDisplayBinding
import com.example.smartmedicinebox.firebase.FirestoreClass
import com.example.smartmedicinebox.models.Board
import com.example.smartmedicinebox.utils.Constants

class DetailsToDisplayActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailsToDisplayBinding

    private lateinit var mBoardDetails: Board

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsToDisplayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        var boardDocumentID = ""
        if(intent.hasExtra(Constants.DOCUMENT_ID)){
            boardDocumentID = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getBoardDetails(this, boardDocumentID)

        FirestoreClass().loadBoardDetails(this, false, boardDocumentID)

        binding.btnDtdOkay.setOnClickListener{
            showProgressDialog(resources.getString(R.string.please_wait))
            updateBoard()
        }

    }

    private fun setUpActionBar(title: String){

        val toolbarDetailsToDisplayActivity = binding.toolbarDetailsToDisplayActivity

        setSupportActionBar(toolbarDetailsToDisplayActivity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_colour_back_24dp)
            actionBar.title = title
        }

        toolbarDetailsToDisplayActivity.setNavigationOnClickListener{onBackPressed()}
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun setDetailsInUI(board: Board) {


        mBoardDetails = board
        binding.dtdName.setText(board.medsName)
        binding.dtdPrescribedFor.setText(board.prescribedFor)
        binding.dtdPrescribedBy.setText(board.prescribedBy)

        /*binding.dtdName.text = "Medicine Name: ${board.medsName}"
        binding.dtdPrescribedFor.text = "Prescribed For: ${board.prescribedFor}"
        binding.dtdPrescribedBy.text = "Prescribed By: ${board.prescribedBy}"
        binding.dtdTime.text = "Time: ${board.time}" */
    }

    fun boardDetails(board: Board){
        hideProgressDialog()
        setUpActionBar(board.medsName)
    }

    fun updateBoard(){


        val userHashMap = HashMap<String, Any>()

        var anyChangesMade = false

        var boardDocumentID = ""
        if(intent.hasExtra(Constants.DOCUMENT_ID)){
            boardDocumentID = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }

        if(binding.dtdName.text.toString() != mBoardDetails.medsName) {

            userHashMap[Constants.MEDSNAME] = binding.dtdName.text.toString()
            anyChangesMade = true

        }
        if(binding.dtdPrescribedFor.text.toString() != mBoardDetails.prescribedFor) {

            userHashMap[Constants.PRESCRIBEDFOR] = binding.dtdPrescribedFor.text.toString()
            anyChangesMade = true

        }
        if(binding.dtdPrescribedBy.text.toString() != mBoardDetails.prescribedBy) {

            userHashMap[Constants.PRESCRIBEDBY] = binding.dtdPrescribedBy.text.toString()
            anyChangesMade = true

        }

        if(anyChangesMade){
            FirestoreClass().updateBoardDetails(this, userHashMap, boardDocumentID)
        }
        else
            hideProgressDialog()

    }

    fun boardUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
    }

    fun updateNavigationBoardDetails(board: Board, readBoardsList: Boolean){

        //use glide to update the user's image
        //go through the documentation of glide

        mBoardDetails = board

        mBoardDetails = board
        binding.dtdName.setText(board.medsName)
        binding.dtdPrescribedFor.setText(board.prescribedFor)
        binding.dtdPrescribedBy.setText(board.prescribedBy)


        if(readBoardsList){
            showProgressDialog(resources.getString(R.string.please_wait))

        }

    }






}