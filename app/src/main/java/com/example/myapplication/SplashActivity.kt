package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 5000 // 3 seconds
    private var mDelayHandler: Handler? = null
    private lateinit var progressBar: ProgressBar
    private var progressBarStatus = 0
    private var dummy: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        progressBar = findViewById(R.id.splash_screen_progress_bar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mDelayHandler = Handler()
        mDelayHandler?.postDelayed(mRunnable, SPLASH_DELAY)
    }

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
        mDelayHandler?.removeCallbacks(mRunnable)
    }

    private val mRunnable: Runnable = Runnable {
        // Progress bar update logic
        Thread(Runnable {
            while (progressBarStatus < 100) {
                // Simulating work being done
                try {
                    Thread.sleep(100) // Simulate work being done
                    dummy += 25
                    progressBarStatus = dummy

                    // Update the progress bar on the main thread
                    runOnUiThread {
                        progressBar.setProgress(progressBarStatus)
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            // After finishing the dummy work, launch the main activity
            launchMainActivity()
        }).start()
    }

    override fun onDestroy() {
        mDelayHandler?.removeCallbacks(mRunnable)
        super.onDestroy()
    }
}
