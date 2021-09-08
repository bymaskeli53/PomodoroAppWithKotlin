package com.muhammetgundogar.pomodoroappwithkotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.TypedArrayUtils.getString


class LowBatteryReceiver: BroadcastReceiver() {


    var scale = -1
    var level = 100
    var voltage = -1
    var temp = -1

    override fun onReceive(context: Context?, intent: Intent) {
        level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 100)
        scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
        voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
        if (level == 100) {
          //  Toast.makeText(context,"low battery",Toast.LENGTH_LONG).show()

         val notification =  NotificationCompat.Builder(context!!, Constants.CHANNEL_ID)
                .setContentTitle("Plug in")
                .setContentText("Your battery level is low you should plug in your phone")
                .setSmallIcon(R.drawable.low_battery)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

                .build()

            val notificationManager = NotificationManagerCompat.from(context)

            notificationManager.notify(Constants.NOTIFICATION_ID, notification)
        }
    }


}