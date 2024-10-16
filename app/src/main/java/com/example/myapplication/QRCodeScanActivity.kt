package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONException
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


class QRCodeScanActivity : AppCompatActivity() {
    private lateinit var tvResult: TextView

    private var WRITE_EXTERNAL_STORAGE_PERMISSION_CODE: Int = 1
    private var READ_EXTERNAL_STORAGE_PERMISSION_CODE: Int = 2
    private var CAMERA_PERMISSION_CODE: Int = 3
    private var READ_MEDIA_VIDEO_PERM_CODE :Int = 4
    private var READ_MEDIA_AUDIO_PERM_CODE :Int = 5
    private var READ_MEDIA_IMAGES_PERM_CODE :Int = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_qrcode_scan)
        init()
        checkPermission()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun init(){
        tvResult = findViewById(R.id.tv_result)
    }
    private fun checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            when (PackageManager.PERMISSION_DENIED) {
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        WRITE_EXTERNAL_STORAGE_PERMISSION_CODE
                    )
                }
                checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) -> {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                        READ_MEDIA_IMAGES_PERM_CODE
                    )
                }
                checkSelfPermission(Manifest.permission.READ_MEDIA_VIDEO) -> {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_MEDIA_VIDEO),
                        READ_MEDIA_VIDEO_PERM_CODE
                    )
                }
                checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) -> {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
                        READ_MEDIA_AUDIO_PERM_CODE
                    )
                }
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        READ_EXTERNAL_STORAGE_PERMISSION_CODE
                    )
                }
                checkSelfPermission(Manifest.permission.CAMERA) -> {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA),
                        CAMERA_PERMISSION_CODE
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= 33 ) {
            when (requestCode) {
                CAMERA_PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                        Toast.makeText(this, "Anda perlu memberikan semua izin untuk menggunakan aplikasi ini.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                READ_MEDIA_AUDIO_PERM_CODE -> if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                        Toast.makeText(this, "Anda perlu memberikan semua izin untuk menggunakan aplikasi ini.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                READ_MEDIA_VIDEO_PERM_CODE -> if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                        Toast.makeText(this, "Anda perlu memberikan semua izin untuk menggunakan aplikasi ini.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                READ_MEDIA_IMAGES_PERM_CODE -> if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                        Toast.makeText(this, "Anda perlu memberikan semua izin untuk menggunakan aplikasi ini.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        } else {
            when (requestCode) {
                WRITE_EXTERNAL_STORAGE_PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "Anda perlu memberikan semua izin untuk menggunakan aplikasi ini.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                READ_EXTERNAL_STORAGE_PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "Anda perlu memberikan semua izin untuk menggunakan aplikasi ini.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                CAMERA_PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                        Toast.makeText(this, "Anda perlu memberikan semua izin untuk menggunakan aplikasi ini.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }

    fun clickScan(view: View) {
        val scanOptions = arrayOf<String>("Camera", "Gallery")
        AlertDialog.Builder(this)
            .setTitle("Scan QR Code melalui")
            .setItems(scanOptions) { _, which-> when (which) {
                0 -> openCamera()
                1 -> openGallery()
            } }
            .create()
            .show()
    }

    private fun openCamera() {
        val qrScan = IntentIntegrator(this)
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        qrScan.setPrompt("Scan a QR Code")
        qrScan.setOrientationLocked(false)
        qrScan.setBeepEnabled(true)
        qrScan.setBarcodeImageEnabled(true)
        //initiating the qr code scan
        qrScan.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null)
        {
            //if qrcode has nothing in it
            if (result.contents == null){
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show()
            } else {
                //if qr contains data
                try {
                    val contents = result.contents

                    tvResult.text = contents
                }
                catch (e: JSONException) {
                    e.printStackTrace()
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.contents, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncherGallery.launch(galleryIntent)
    }

    var resultLauncherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            val imageUri = data!!.data!!
            val imagePath = convertMediaUriToPath(imageUri)
            val imgFile = File(imagePath)
            scanImageQRCode(imgFile)
        } else {
            Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show()
        }
    }

    private fun convertMediaUriToPath(uri: Uri):String {
        val proj = arrayOf<String>(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, proj, null, null, null)
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val path = cursor.getString(columnIndex)
        cursor.close()
        return path
    }

    private fun scanImageQRCode(file: File){
        val inputStream: InputStream = BufferedInputStream(FileInputStream(file))
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val decoded = scanQRImage(bitmap)
        Log.i("QrTest", "Decoded string=$decoded")
    }

    private fun scanQRImage(bMap: Bitmap): String? {
        var contents: String? = null
        val intArray = IntArray(bMap.width * bMap.height)
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.width, 0, 0, bMap.width, bMap.height)
        val source: LuminanceSource = RGBLuminanceSource(bMap.width, bMap.height, intArray)
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        val reader: Reader = MultiFormatReader()
        try {
            val result: Result = reader.decode(bitmap)
            contents = result.text

            tvResult.text = contents
        } catch (e: Exception) {
            Log.e("QrTest", "Error decoding qr code", e)
            Toast.makeText(this, "Error decoding QR Code, Mohon pilih gambar QR Code yang benar!", Toast.LENGTH_SHORT).show()
        }
        return contents
    }
}