package com.muhammetgundogar.pomodoroappwithkotlin

import android.os.Build

object Constants {
     const val MINUTES_25: Long  = 25 * 1000 * 60
     const val CHANNEL_ID = "channelID"
     const val NOTIFICATION_ID = 0
    const val CHANNEL_NAME = "channelName"
    const val IS_VERSION_OREO = Build.VERSION_CODES.O
    const val POMODORO_LINK = "https://en.wikipedia.org/wiki/Pomodoro_Technique"
    const val MINUTES_5: Long  = 5 * 1000 * 60
}