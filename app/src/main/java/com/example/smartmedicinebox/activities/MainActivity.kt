package com.example.smartmedicinebox.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartmedicinebox.R
import com.example.smartmedicinebox.adapters.BoardItemsAdapter
import com.example.smartmedicinebox.databinding.ActivityMainBinding
import com.example.smartmedicinebox.firebase.FirestoreClass
import com.example.smartmedicinebox.models.Board
import com.example.smartmedicinebox.models.User
import com.example.smartmedicinebox.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mUserName: String

    companion object{
        const val MY_PROFILE_REQUEST_CODE : Int = 11
        const val CREATE_BOARD_REQUEST_CODE: Int = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        setUpActionBar()

        binding.navView.setNavigationItemSelectedListener(this)

        FirestoreClass().loadUserData(this, true)

        binding.appBarMainLayout.fabCreateBoard.setOnClickListener {
            val intent = Intent(this, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, mUserName)
            startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)
        }

    }

    fun populateBoardsListToUI(boardsList: ArrayList<Board>){

        Log.e("entered the function?", "We have entered populateBoardsListToUI function")

        hideProgressDialog()

        val rvBoardsList = binding.appBarMainLayout.mainContent.rvBoardsList
        val tvNoBoardsAvailable = binding.appBarMainLayout.mainContent.tvNoBoardsAvailable

        if (boardsList.size > 0){

            Log.e("tag", "Board list size is greater than zero")

            rvBoardsList.visibility = View.VISIBLE
            tvNoBoardsAvailable.visibility =  View.GONE

            rvBoardsList.layoutManager = LinearLayoutManager(this)
            rvBoardsList.setHasFixedSize(true)

            val adapter = BoardItemsAdapter(this, boardsList)
            rvBoardsList.adapter = adapter

            adapter.setOnClickListener(object :
                BoardItemsAdapter.OnClickListener {
                override fun onClick(position: Int, model: Board) {
                    startActivity(Intent(this@MainActivity, TaskListActivity::class.java))
                }
            })

        }
        else{                       //if the boards list is empty

            Log.e("if or else", "we have entered the else block")
            rvBoardsList.visibility = View.GONE
            tvNoBoardsAvailable.visibility = View.VISIBLE
        }

    }

    private fun setUpActionBar(){

        val toolbarMainActivity = binding.appBarMainLayout.toolbarMainActivity   //accessing toolbar_main_activity contained in app_bar_main

        setSupportActionBar(toolbarMainActivity)
        toolbarMainActivity.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        toolbarMainActivity.setNavigationOnClickListener{
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

       override fun onBackPressed() {
           if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
               binding.drawerLayout.closeDrawer(GravityCompat.START)
           }
           else {
               doubleBackToExit()
           }
       }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE){
            FirestoreClass().loadUserData(this)           //loading the whole page so that the changes in my profile is visible everywhere
        }
        else if(resultCode == Activity.RESULT_OK && requestCode == CREATE_BOARD_REQUEST_CODE){
            FirestoreClass().getBoardsList(this)
        }
        else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_my_profile  -> {              //accessing my profile present in activity_main_drawer

                startActivityForResult(Intent(this, MyProfileActivity::class.java), MY_PROFILE_REQUEST_CODE)     //moving from drawer layout to MyProfileActivity
            }
            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)

        return true       //just because the function expects a boolean return type value
    }
    //this function is to update the user details
    fun updateNavigationUserDetails(user: User, readBoardsList: Boolean){

        //use glide to update the user's image
        //go through the documentation of glide

        mUserName = user.name

        val tvUsername : TextView = binding.navView.findViewById(R.id.tv_username)
        tvUsername.text = user.name

        if(readBoardsList){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().getBoardsList(this)
        }

    }
}