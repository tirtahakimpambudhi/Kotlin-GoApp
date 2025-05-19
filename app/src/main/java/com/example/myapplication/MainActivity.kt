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
import com.google.firebase.Firebase
import com.google.firebase.database.database


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")

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

        val buttonProduct :ImageView = findViewById(R.id.product)
        buttonProduct.setOnClickListener {
            val intent = Intent(this , ProductActivity::class.java)
            startActivity(intent)
        }

        val buttonUser :ImageView = findViewById(R.id.user)
        buttonUser.setOnClickListener {
            val intent = Intent(this , UserActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}