package com.example.smartmedicinebox.activities

import android.os.Bundle
import androidx.core.view.GravityCompat
import com.example.smartmedicinebox.R
import com.example.smartmedicinebox.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        setUpActionBar()

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
}