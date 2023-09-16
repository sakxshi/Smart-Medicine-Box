package com.example.smartmedicinebox.activities

import android.content.Intent
import android.os.Bundle
import com.example.smartmedicinebox.R
import com.example.smartmedicinebox.databinding.ActivityDetailsToDisplayBinding
import com.example.smartmedicinebox.firebase.FirestoreClass
import com.example.smartmedicinebox.models.Board
import com.example.smartmedicinebox.utils.Constants

class DetailsToDisplayActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailsToDisplayBinding

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

        FirestoreClass().loadUserData(this)

        FirestoreClass().getBoardDetails(this, boardDocumentID)

        binding.btnDtdOkay.setOnClickListener{
            showProgressDialog(resources.getString(R.string.please_wait))
            startActivity(Intent(this, MainActivity::class.java))
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

    fun setDetailsInUI(board: Board) {


        binding.dtdName.text = "Medicine Name: ${board.medsName}"
        binding.dtdPrescribedFor.text = "Prescribed For: ${board.prescribedFor}"
        binding.dtdPrescribedBy.text = "Prescribed By: ${board.prescribedBy}"
        binding.dtdTime.text = "Time: ${board.time}"
    }

    fun boardDetails(board: Board){
        hideProgressDialog()
        setUpActionBar(board.medsName)
    }




}