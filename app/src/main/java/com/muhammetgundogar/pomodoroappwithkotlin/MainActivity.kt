package com.muhammetgundogar.pomodoroappwithkotlin

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.google.android.material.timepicker.TimeFormat
import com.muhammetgundogar.pomodoroappwithkotlin.Constants.MINUTES_25
import com.muhammetgundogar.pomodoroappwithkotlin.databinding.ActivityMainBinding
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit



class MainActivity : AppCompatActivity() {
    private var isStopped = false
    private lateinit var binding: ActivityMainBinding
   private lateinit var countDownTimer: CountDownTimer

    var number = 0L




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)






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
        countDownTimer = object: CountDownTimer(time,1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.textViewTime.text = String.format("%d : %d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))


                number = millisUntilFinished

            }

            override fun onFinish() {
                binding.textViewTime.text = getString(R.string.TimeFinish)
            }

        }.start()
    }

    private fun stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel()
        }
    }
}





