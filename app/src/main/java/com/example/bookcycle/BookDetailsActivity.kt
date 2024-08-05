package com.example.bookcycle

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView

class BookDetailsActivity : AppCompatActivity() {

    private lateinit var messageEdt: EditText
    private lateinit var sendMsgBtn: Button
    private lateinit var sendEmailBtn: Button
    private lateinit var contactNumber: String
    private lateinit var sellerEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        // Menu
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_homepage -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.item_addbook -> {
                    val intent = Intent(this, BookActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.item_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Book details views
        val titleTextView: TextView = findViewById(R.id.bookTitleTextView)
        val categoryTextView: TextView = findViewById(R.id.bookCategoryTextView)
        val descriptionTextView: TextView = findViewById(R.id.bookDescriptionTextView)
        val priceTextView: TextView = findViewById(R.id.bookPriceTextView)
        val contactTextView: TextView = findViewById(R.id.bookContactTextView)
        val emailTextView: TextView = findViewById(R.id.bookEmailTextView)
        val bookImageView: ImageView = findViewById(R.id.bookImageView)
        val authorTextView: TextView = findViewById(R.id.bookAuthor)

        // Message intent views
        messageEdt = findViewById(R.id.idEdtMessage)
        sendMsgBtn = findViewById(R.id.idBtnSendMessage)
        sendEmailBtn = findViewById(R.id.idBtnSendEmail)

        // Retrieve and set book details from the intent
        val title = intent.getStringExtra("title") ?: "No Title"
        val category = intent.getStringExtra("category") ?: "No Category"
        val description = intent.getStringExtra("description") ?: "No Description"
        val price = intent.getStringExtra("price") ?: "No Price"
        val contact = intent.getStringExtra("contact") ?: "No Contact"
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""
        val author = intent.getStringExtra("author") ?: "No author"
        val email = intent.getStringExtra("email") ?: "No email"

        titleTextView.text = title
        categoryTextView.text = category
        descriptionTextView.text = description
        contactTextView.text = contact
        emailTextView.text = email
        priceTextView.text = "${price} CAD"
        authorTextView.text = author
        Glide.with(this).load(imageUrl).into(bookImageView)

        contactNumber = contact
        sellerEmail = email

        // Set up the send message button click listener
        sendMsgBtn.setOnClickListener {
            val message = messageEdt.text.toString()

            if (message.isNotEmpty()) {
                sendSMS(contactNumber, message)
            } else {
                Toast.makeText(applicationContext, "Please enter a message", Toast.LENGTH_LONG).show()
            }
        }

        // Set up the send email button click listener
        sendEmailBtn.setOnClickListener {
            val message = messageEdt.text.toString()

            if (message.isNotEmpty()) {
                sendEmail(sellerEmail, title, message)
            } else {
                Toast.makeText(applicationContext, "Please enter a message", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        try {
            val smsIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("sms:$phoneNumber")
                putExtra("sms_body", message)
            }
            startActivity(smsIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to open SMS app", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun sendEmail(email: String, subject: String, message: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

        try {
            startActivity(Intent.createChooser(intent, "Choose an email client"))
        } catch (e: Exception) {
            Toast.makeText(this, "No email client installed", Toast.LENGTH_SHORT).show()
        }
    }
}
