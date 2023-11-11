package com.example.smartmedicinebox.activities

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import com.example.smartmedicinebox.R
import com.example.smartmedicinebox.databinding.ActivityCreateBoardBinding
import com.example.smartmedicinebox.firebase.FirestoreClass
import com.example.smartmedicinebox.models.Board
import com.example.smartmedicinebox.utils.Constants
import java.time.LocalTime

@Suppress("IMPLICIT_CAST_TO_ANY")
class CreateBoardActivity : BaseActivity() {

    private lateinit var binding: ActivityCreateBoardBinding

    private lateinit var mUserName: String

    lateinit var timePicker: TimePicker
    lateinit var pendingIntent: PendingIntent
    var time: Long? = null
    var alarmManager: AlarmManager? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setUpActionBar()

        if(intent.hasExtra(Constants.NAME)){
            mUserName = intent.getStringExtra(Constants.NAME)!!
        }

        timePicker = binding.timePicker
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        binding.btnCreate.setOnClickListener{
            showProgressDialog(resources.getString(R.string.please_wait))
            createBoard()
        }


    }



    private fun setUpActionBar(){

        val toolbarCreateBoardActivity = binding.toolbarCreateBoardActivity

        setSupportActionBar(toolbarCreateBoardActivity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_colour_back_24dp)
            actionBar.title = resources.getString(R.string.create_board_title)
        }

        toolbarCreateBoardActivity.setNavigationOnClickListener{onBackPressed()}

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createBoard(){

        val assignedUsersArrayList: ArrayList<String> = ArrayList()
        assignedUsersArrayList.add(getCurrentUserID())

        val alarmHour = timePicker.hour.toString()
        val alarmMin = timePicker.minute.toString()

        val alarmTime = "$alarmHour : $alarmMin"



        //preparing the board
        var board = Board(
            binding.etBoardName.text.toString(),
            mUserName,
            assignedUsersArrayList,
            "",
            binding.etPrescribedFor.text.toString(),
            binding.etPrescribedBy.text.toString(),
            alarmTime)
        FirestoreClass().createBoard(this, board)               //the board will now be visible in the Firebase
    }

    fun boardCreatedSuccessfully(){
        hideProgressDialog()

        setResult(Activity.RESULT_OK)

        finish()
    }

    //Alarm shit starts from here
    @RequiresApi(Build.VERSION_CODES.N)
    fun onToggleClicked(view: View){

        var calendar = java.util.Calendar.getInstance()
        val currentTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalTime.now()
        }
        else{
            TODO()
        }
        if((view as ToggleButton).isChecked){
            Toast.makeText(this, "Alarm is set", Toast.LENGTH_LONG).show()

            calendar[Calendar.HOUR_OF_DAY] = timePicker!!.currentHour
            calendar[Calendar.MINUTE] = timePicker!!.currentHour

            val intent = Intent(this, AlarmReceiver::class.java)
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            time = calendar.timeInMillis - calendar.timeInMillis % 60000

            if(System.currentTimeMillis() > time!!){
                time = if(Calendar.AM_PM == 0){
                    time!! + 1000*60*60*12
                }
                else{
                    time!! + 1000*60*60*24
                }
            }


            Log.e("system hour",currentTime.hour.toString())
            Log.e("system minute", currentTime.minute.toString())

            Log.e("alarm hour", timePicker.hour.toString())
            Log.e("alarm minute", timePicker.minute.toString())

            val sysHour = currentTime.hour.toString()
            val sysMin = currentTime.minute.toString()

            //alarm bs not working
            if (!(sysHour == timePicker.hour.toString() && sysMin == timePicker.minute.toString())) {

                AlarmReceiver().onReceive(this, intent)
                        alarmManager !!. setRepeating (AlarmManager.RTC_WAKEUP, time!!, 1000, pendingIntent)
            }


        }
        else{
            alarmManager!!.cancel(pendingIntent)
            Toast.makeText(this, "Alarm Off", Toast.LENGTH_LONG).show()
        }
    }


}