package org.hyperskill.stopwatch

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import org.hyperskill.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: TimerViewModel by viewModels()
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.alertDialog_title)
            .setNegativeButton(R.string.cancel, null)
        binding.apply {
            startButton.setOnClickListener{
                if(!isTimerRunning)
                    viewModel.startTimer()
            }

            resetButton.setOnClickListener{
                viewModel.endTimer()
                progressBar.isVisible = false
                textView.setTextColor(Color.BLACK)
                viewModel.defaultSetPoint()
            }

            settingsButton.apply {
                setOnClickListener{
                    val contentView = LayoutInflater.from(this@MainActivity).inflate(R.layout.custom_alert_dialog, null, false)
                    dialog
                        .setView(contentView)
                        .setPositiveButton(R.string.ok) {_, _ ->
                            val editTextValue = contentView.
                            findViewById<EditText>(R.id.upperLimitEditText).
                            text.toString()
                            if(editTextValue.isDigitsOnly())
                                viewModel.updateSetPoint(editTextValue.toLong() * 1000)
                        }
                        .show()
                }
            }
        }
        viewModel._elapsedTime.observe(this) { time -> binding.textView.text = time.toString() }
        viewModel._isStartBtnEnabled.observe(this) { status ->
            isTimerRunning = status.also { binding.apply {
                settingsButton.isEnabled = !status
                progressBar.isVisible = status
            } }}
        viewModel._progressBarColor.observe(this) { color ->
            binding.progressBar.indeterminateTintList = ColorStateList.valueOf(color)
        }
        viewModel._isTimeLimitExceeded.observe(this) { exceeded ->
            if(exceeded){
                binding.textView.setTextColor(Color.RED)
                NotificationService(this).showNotification()
            }

        }
    }
}

