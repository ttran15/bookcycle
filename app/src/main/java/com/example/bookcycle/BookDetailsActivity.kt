package com.example.bookcycle

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

class BookDetailsActivity : AppCompatActivity() {

    private lateinit var phoneEdt: EditText
    private lateinit var messageEdt: EditText
    private lateinit var sendMsgBtn: Button

    private val SMS_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        // Book details views
        val titleTextView: TextView = findViewById(R.id.bookTitleTextView)
        val descriptionTextView: TextView = findViewById(R.id.bookDescriptionTextView)
        val priceTextView: TextView = findViewById(R.id.bookPriceTextView)
        val bookImageView: ImageView = findViewById(R.id.bookImageView)

        // Message intent views
        phoneEdt = findViewById(R.id.idEdtphone)
        messageEdt = findViewById(R.id.idEdtMessage)
        sendMsgBtn = findViewById(R.id.idBtnSendMessage)

        // Retrieve and set book details from the intent
        val title = intent.getStringExtra("title") ?: "No Title"
        val description = intent.getStringExtra("description") ?: "No Description"
        val price = intent.getStringExtra("price") ?: "No Price"
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""

        titleTextView.text = title
        descriptionTextView.text = description
        priceTextView.text = "${price} CAD"
        Glide.with(this).load(imageUrl).into(bookImageView)

        // Check for SMS permissions
        checkPermissions()

        // Set up the send message button click listener
        sendMsgBtn.setOnClickListener {
            val phoneNumber = phoneEdt.text.toString()
            val message = messageEdt.text.toString()

            if (phoneNumber.isNotEmpty() && message.isNotEmpty()) {
                val smsIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("sms:$phoneNumber")
                    putExtra("sms_body", message)
                }
                startActivity(smsIntent)
            } else {
                Toast.makeText(applicationContext, "Please enter both phone number and message", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "SMS Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}