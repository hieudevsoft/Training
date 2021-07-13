package com.devapp.trainning

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.skydoves.landscapist.glide.GlideImage
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity" 
    private lateinit var button: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioMale: RadioButton
    private lateinit var radioFemale: RadioButton
    private lateinit var checkBox: CheckBox
    private lateinit var toogleButton:ToggleButton
    private lateinit var buttonShowDialog:Button
    private lateinit var buttonShowDatePicker: Button
    private lateinit var buttonTimePicker: Button
    private lateinit var btnShowNotification:Button
    private lateinit var btnCancelNotificaion:Button
    private lateinit var btnMutilScreen:Button
    private lateinit var btnReadata:Button
    private var indexRemoveItem = 0
    private var listNotificationId = mutableListOf<Long>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapping()
        button.setOnClickListener {
            Toast.makeText(this, "Click Button", Toast.LENGTH_SHORT).show()
        }
        button.setOnLongClickListener {
            Toast.makeText(this, "Click on Long", Toast.LENGTH_SHORT).show()
            return@setOnLongClickListener false
        }
        radioGroup.setOnCheckedChangeListener { group, checkedId -> 
            if (checkedId==radioFemale.id) Toast.makeText(this, "Female is checked", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this, "Male is checked", Toast.LENGTH_SHORT).show()
        }
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) Toast.makeText(this, "CheckBox is checked", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this, "CheckBox is unchecked", Toast.LENGTH_SHORT).show()
        }
        toogleButton.setOnCheckedChangeListener { _, isChecked ->  
            if(isChecked) {
                Toast.makeText(this, "Toggle is On", Toast.LENGTH_SHORT).show()
                toogleButton.setBackgroundResource(R.drawable.custom_login_button_enabled)
            }
            else {
                toogleButton.setBackgroundResource(R.drawable.custom_login_button_notenabled)
                Toast.makeText(this, "Toggle is Off", Toast.LENGTH_SHORT).show()
            }
        }
        buttonShowDialog.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage("This is Alert Dialog")
            alertDialog.setTitle("Notification")
            alertDialog.setPositiveButton("OK"
            ) { _, which -> Log.d(TAG, "onCreate: $which") ;}
            alertDialog.setNegativeButton("CANCEL") { _, which ->
                Log.d(TAG, "onCreate: $which")
            }
            val dialog = alertDialog.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }
        buttonShowDatePicker.setOnClickListener {
            val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
            datePickerBuilder.setTitleText("Date Picker")
            datePickerBuilder.setSelection(Calendar.getInstance().timeInMillis)
            val datePicker = datePickerBuilder.build()
            datePicker.showNow(supportFragmentManager,"MATERIAL_DATE_PICKER")
            datePicker.addOnPositiveButtonClickListener {
                val dateString = datePicker.headerText
                val df = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(dateString)
                val dateSelected = SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH).format(df)
                Toast.makeText(this, "Selected Date: $dateSelected", Toast.LENGTH_SHORT).show()
            }

        }
        buttonTimePicker.setOnClickListener {
            val isHourSystem24h = is24HourFormat(this)
            val clockFormat = if(isHourSystem24h) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
            val timePicker = MaterialTimePicker.Builder().
                    setTimeFormat(clockFormat)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Time")
                .build()
            timePicker.showNow(supportFragmentManager,"MATERIAL_TIME_PICKER")
            timePicker.addOnPositiveButtonClickListener {
                if(timePicker.inputMode==0) Log.d(TAG, "onCreate: clock")
                else Log.d(TAG, "onCreate: keyboard")
                Log.d(TAG, "onCreate: Selected time: ${timePicker.inputMode} ${timePicker.hour}:${timePicker.minute}")
            }
        }
        btnShowNotification.setOnClickListener {
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
            sendNotification()
        }
        btnCancelNotificaion.setOnClickListener {
            removeNotification()
        }
        btnReadata.setOnClickListener {
            ReadDataTask(this).execute("https://jsonplaceholder.typicode.com/posts/1/comments")
        }
        btnMutilScreen.setOnClickListener { Intent(this,LoginMultilForm::class.java).also { startActivity(it) } }

    }
    private fun mapping(){
        button = findViewById(R.id.btnCheck)
        radioGroup = findViewById(R.id.rdGroup)
        radioMale = findViewById(R.id.rbbMale)
        radioFemale = findViewById(R.id.rdFemale)
        checkBox = findViewById(R.id.cbCheckBox)
        toogleButton = findViewById(R.id.toggleButton)
        buttonShowDialog = findViewById(R.id.btnShowAlertDialog)
        buttonShowDatePicker = findViewById(R.id.btnShowDatePicker)
        buttonTimePicker = findViewById(R.id.btnShowTimePicker)
        btnCancelNotificaion = findViewById(R.id.btnRemoveNotification)
        btnShowNotification = findViewById(R.id.btnShowNotification)
        btnReadata = findViewById(R.id.btnReadData)
        btnMutilScreen = findViewById(R.id.btnMutilScreen)
    }
    private fun sendNotification(){
        val uriSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification = NotificationCompat.Builder(this,MyApp.CHANNEL_ID.get.toString())
            .setContentTitle("Send Notification")
            .setContentText("Message Push Notification")
            .setSmallIcon(R.drawable.soda)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.soda))
            .setAutoCancel(true)
            .setColor(resources.getColor(R.color.design_default_color_error,null))
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(resources,R.drawable.soda)).bigLargeIcon(null))
            .setSound(uriSound)
            .build()
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        val intent = Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT)
        notification.contentIntent = pendingIntent
        notificationManagerCompat.notify(getIdNotificaiton(),notification)
    }
    private fun removeNotification(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(listNotificationId.removeAt(listNotificationId.size-1).toInt())
    }
    private fun getIdNotificaiton(): Int {
        indexRemoveItem = Date().time.toInt()
        listNotificationId.add(indexRemoveItem.toLong())
        return indexRemoveItem
    }

    override fun onStart() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
        ){
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
        }
        super.onStart()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==1){
            if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "onRequestPermissionsResult: Ready read data")
                btnReadata.isEnabled = true
            }else{
                btnReadata.isEnabled = false
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
