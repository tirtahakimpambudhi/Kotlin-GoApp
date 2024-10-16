package com.example.myapplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.EnumMap
import java.util.Locale


class QRCodeGenActivity :  AppCompatActivity() {

    private var WRITE_EXTERNAL_STORAGE_PERMISSION_CODE: Int = 1
    private var READ_EXTERNAL_STORAGE_PERMISSION_CODE: Int = 2
    private var CAMERA_PERMISSION_CODE: Int = 3
    private var READ_MEDIA_VIDEO_PERM_CODE :Int = 4
    private var READ_MEDIA_AUDIO_PERM_CODE :Int = 5
    private var READ_MEDIA_IMAGES_PERM_CODE :Int = 6
    // on below line we are creating a variable
    // for our image view, edit text and a button.
    private lateinit var qrIV: ImageView
    private lateinit var msgEdt: EditText
    private lateinit var generateQRBtn: Button
    private lateinit var resetBtn: Button
    private lateinit var downloadBtn: Button
    private val qrCodeWidthPixels = 500
    // on below line we are creating
    // a variable for qr encoder.

    private fun generateQRCode(data: String): Bitmap? {
        val bitMatrix: BitMatrix = try {
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
            MultiFormatWriter().encode(
                data,
                BarcodeFormat.QR_CODE,
                qrCodeWidthPixels,
                qrCodeWidthPixels,
                hints
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        val qrCodeWidth = bitMatrix.width
        val qrCodeHeight = bitMatrix.height
        val pixels = IntArray(qrCodeWidth * qrCodeHeight)

        for (y in 0 until qrCodeHeight) {
            val offset = y * qrCodeWidth
            for (x in 0 until qrCodeWidth) {
                pixels[offset + x] = if (bitMatrix[x, y]) {
                    resources.getColor(R.color.io_background, theme) // QR code color
                } else {
                    resources.getColor(R.color.white, theme) // Background color
                }
            }
        }

        val bitmap = Bitmap.createBitmap(qrCodeWidth, qrCodeHeight, Bitmap.Config.RGB_565)
        bitmap.setPixels(pixels, 0, qrCodeWidth, 0, 0, qrCodeWidth, qrCodeHeight)

        return bitmap
    }

    private fun combineBitmaps(backgroundBitmap: Bitmap, overlayBitmap: Bitmap): Bitmap {
        val combinedBitmap = Bitmap.createBitmap(backgroundBitmap.width, backgroundBitmap.height, backgroundBitmap.config)
        val canvas = Canvas(combinedBitmap)
        canvas.drawBitmap(backgroundBitmap, 0f, 0f, null)
        val left = (backgroundBitmap.width - overlayBitmap.width) / 2
        val top = (backgroundBitmap.height - overlayBitmap.height) / 2
        canvas.drawBitmap(overlayBitmap, left.toFloat(), top.toFloat(), null)
        return combinedBitmap
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
            }
        }
    }


    private fun generateUniqueName(imageName: String?, returnFullPath: Boolean): String {
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyyMMddsshhmmss", Locale.getDefault())
        val date: String = sdf.format(Date())

        val filename = String.format("%s_%s.jpg", imageName, date)

        if (returnFullPath) {
            return filename
        } else {
            return filename
        }
    }


    private fun getBitmapFromImageView(imageView: ImageView): Bitmap? {
        val drawable = imageView.drawable
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            null
        }
    }


    fun isWriteStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            true
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }
    fun requestWriteStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_PERMISSION_CODE
            )
        }
    }


    fun saveBitmapToGallery(name: String?, bitmapImage: Bitmap) {
        CoroutineScope(Dispatchers.IO).launch {
            val filename = generateUniqueName(name, false)
            var fos: java.io.OutputStream? = null
            val resolver = contentResolver

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/QRCodeApp")
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
            }

            val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            if (imageUri != null) {
                try {
                    // Use let to handle the nullable OutputStream
                    fos = resolver.openOutputStream(imageUri)
                    fos?.let {
                        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, it)
                        it.flush()
                        it.close()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@QRCodeGenActivity, "Failed to save QR Code", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(imageUri, contentValues, null, null)
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@QRCodeGenActivity, "QR Code saved to gallery", Toast.LENGTH_SHORT).show()
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@QRCodeGenActivity, "Failed to create MediaStore entry", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun setupDownloadButton() {
        downloadBtn.setOnClickListener {
            val bitmap = getBitmapFromImageView(qrIV)
            if (bitmap == null) {
                Toast.makeText(this, "Unable to retrieve QR Code bitmap", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isWriteStoragePermissionGranted()) {
                saveBitmapToGallery("QR_CODE", bitmap)
            } else {
                requestWriteStoragePermission()
            }
        }
    }
    fun initViews() {
        qrIV = findViewById(R.id.idIVQrcode)
        msgEdt = findViewById(R.id.idEdt)
        generateQRBtn = findViewById(R.id.idBtnGenerateQR)
        resetBtn = findViewById(R.id.idBtnReset)
        downloadBtn = findViewById(R.id.idBtnDownload)

        // Listener untuk Generate QR Code
        generateQRBtn.setOnClickListener {
            val message = msgEdt.text.toString()
            if (TextUtils.isEmpty(message)) {
                Toast.makeText(applicationContext, "Enter your message", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val bitmap = generateQRCode(message)
                    if (bitmap != null) {
                        qrIV.setImageBitmap(bitmap)
                        Toast.makeText(applicationContext, "QR Code generated", Toast.LENGTH_SHORT).show()
                        Log.d("QRCodeGenActivity", "Bitmap: $bitmap")
                    } else {
                        Toast.makeText(applicationContext, "Failed to generate QR Code", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Error generating QR Code", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Listener untuk Reset QR Code
        resetBtn.setOnClickListener {
            qrIV.setImageDrawable(null)
            Toast.makeText(applicationContext, "QR Code has been reset", Toast.LENGTH_SHORT).show()
        }

        setupDownloadButton()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode_gen)
        initViews()
    }
}
