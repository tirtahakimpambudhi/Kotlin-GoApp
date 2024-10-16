package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val buttonCalculator :ImageView = findViewById(R.id.calculator)
        buttonCalculator.setOnClickListener {
            val intent = Intent(this , CalculatorActivity::class.java)
            startActivity(intent)
        }

        val buttonQRCodeGen :ImageView = findViewById(R.id.qrgen)
        buttonQRCodeGen.setOnClickListener {
            val intent = Intent(this , QRCodeGenActivity::class.java)
            startActivity(intent)
        }

        val buttonQRCodeScan :ImageView = findViewById(R.id.qrscan)
        buttonQRCodeScan.setOnClickListener {
            val intent = Intent(this , QRCodeScanActivity::class.java)
            startActivity(intent)
        }

        val buttonMaps :ImageView = findViewById(R.id.map)
        buttonMaps.setOnClickListener {
            val intent = Intent(this , MapsActivity::class.java)
            startActivity(intent)
        }

        val buttonTodo :ImageView = findViewById(R.id.todolist)
        buttonTodo.setOnClickListener {
            val intent = Intent(this , TodoListActivity::class.java)
            startActivity(intent)
        }

        val buttonAdiwiyata :ImageView = findViewById(R.id.adwiyata)
        buttonAdiwiyata.setOnClickListener {
//            val intent = Intent(this , AdiwiyataActivity::class.java)

            val url = "https://www.smk2-yk.sch.id/"

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }

            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}