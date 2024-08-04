package com.example.bookcycle

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
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

    private lateinit var messageEdt: EditText
    private lateinit var sendMsgBtn: Button
    private lateinit var contactNumber: String

    private val SMS_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        // Book details views
        val titleTextView: TextView = findViewById(R.id.bookTitleTextView)
        val categoryTextView: TextView = findViewById(R.id.bookCategoryTextView)
        val descriptionTextView: TextView = findViewById(R.id.bookDescriptionTextView)
        val priceTextView: TextView = findViewById(R.id.bookPriceTextView)
        val contactTextView: TextView = findViewById(R.id.bookContactTextView)
        val bookImageView: ImageView = findViewById(R.id.bookImageView)

        // Message intent views
        messageEdt = findViewById(R.id.idEdtMessage)
        sendMsgBtn = findViewById(R.id.idBtnSendMessage)

        // Retrieve and set book details from the intent
        val title = intent.getStringExtra("title") ?: "No Title"
        val category = intent.getStringExtra("category") ?: "No Category"
        val description = intent.getStringExtra("description") ?: "No Description"
        val price = intent.getStringExtra("price") ?: "No Price"
        val contact = intent.getStringExtra("contact") ?: "No Contact"
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""

        titleTextView.text = title
        categoryTextView.text = category
        descriptionTextView.text = description
        contactTextView.text = contact
        priceTextView.text = "${price} CAD"
        Glide.with(this).load(imageUrl).into(bookImageView)

        contactNumber = contact

        // Check for SMS permissions
        checkPermissions()

        // Set up the send message button click listener
        sendMsgBtn.setOnClickListener {
            val message = messageEdt.text.toString()

            if (message.isNotEmpty()) {
                sendSMS(contactNumber, message)
            } else {
                Toast.makeText(applicationContext, "Please enter a message", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION_CODE)
        }
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
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
