package com.muhammetgundogar.pomodoroappwithkotlin

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.snackbar.Snackbar
import com.muhammetgundogar.pomodoroappwithkotlin.Constants.CHANNEL_ID
import com.muhammetgundogar.pomodoroappwithkotlin.Constants.CHANNEL_NAME
import com.muhammetgundogar.pomodoroappwithkotlin.Constants.IS_VERSION_OREO
import com.muhammetgundogar.pomodoroappwithkotlin.Constants.MINUTES_25
import com.muhammetgundogar.pomodoroappwithkotlin.Constants.NOTIFICATION_ID
import com.muhammetgundogar.pomodoroappwithkotlin.Constants.POMODORO_LINK
import com.muhammetgundogar.pomodoroappwithkotlin.databinding.ActivityMainBinding
import com.muhammetgundogar.pomodoroappwithkotlin.databinding.CustomSnackbarViewBinding
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {


    private var isStopped = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingSnackbar: CustomSnackbarViewBinding
    private lateinit var countDownTimer: CountDownTimer

    private lateinit var lowBatteryReceiver: LowBatteryReceiver


    var number = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingSnackbar = CustomSnackbarViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()




        lowBatteryReceiver = LowBatteryReceiver()
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.drawable.action_bar_icon)
        supportActionBar?.setDisplayUseLogoEnabled(true)


        val snackbar: Snackbar = Snackbar.make(binding.buttonReset, "", Snackbar.LENGTH_LONG)

        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        val snackbarLayout: Snackbar.SnackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)

        snackbarLayout.addView(bindingSnackbar.root, 0)
        snackbar.show()
        bindingSnackbar.gotoWebsiteButton.setOnClickListener {
            val intentToWikipedia = Intent(Intent.ACTION_VIEW)
            intentToWikipedia.apply {
                data = Uri.parse(POMODORO_LINK)
                startActivity(this)
            }
//            intentToWikipedia.data = Uri.parse(POMODORO_LINK)
//            startActivity(intentToWikipedia)
        }


//        IntentFilter(Intent.ACTION_BATTERY_LOW).also {
//            registerReceiver(lowBatteryReceiver, it)
//
//        }
        registerReceiver(lowBatteryReceiver, IntentFilter())




        binding.buttonStart.setOnClickListener {
            createTimer(MINUTES_25)
            binding.buttonStart.isEnabled = false


        }


        binding.buttonStopAndContinue.setOnClickListener {
            binding.buttonStart.isEnabled = false
            if (!isStopped) {
                stopTimer()
                isStopped = true
            } else {

                createTimer(number)

                isStopped = false
            }
        }

        binding.buttonReset.setOnClickListener {
            stopTimer()
            createTimer(MINUTES_25)
            isStopped = false

        }


    }

    private fun createTimer(time: Long) {
        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.textViewTime.text = String.format(
                    "%d : %d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    millisUntilFinished
                                )
                            )
                )


                number = millisUntilFinished

            }

            override fun onFinish() {
                binding.textViewTime.text = getString(R.string.TimeFinish)
                createNotification()
                binding.buttonStart.isEnabled = true
            }

        }.start()
    }

    private fun stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel()
        }
    }

    private fun createNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }


        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.TimeFinish))
            .setContentText("Your time is over you should rest")
            .setSmallIcon(R.drawable.icon_time_off)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

            .build()

        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= IS_VERSION_OREO) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lightColor = Color.GREEN
                enableLights(true)
                enableVibration(true)



            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(lowBatteryReceiver)
    }
}





