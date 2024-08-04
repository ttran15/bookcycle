package com.example.bookcycle

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class BookActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_layout)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if user is signed in
        if (auth.currentUser == null) {
            navigateToSignIn()
            return
        }

        // Set up Spinner
        val spinnerCategory: Spinner = findViewById(R.id.spinnerCategory)
        val categories = listOf("Fiction", "Non-Fiction", "Science", "History", "Biography", "Fantasy", "Mystery", "Romance")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        // Set up Button Listeners
        findViewById<Button>(R.id.buttonSelectImage).setOnClickListener {
            selectImage()
        }

        findViewById<Button>(R.id.buttonUpload).setOnClickListener {
            uploadBookDetails()
        }

        // Bottom Navigation Setup
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_homepage -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }

                R.id.item_addbook -> {
                    startActivity(Intent(this, BookActivity::class.java))
                    true
                }

                R.id.item_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    private fun navigateToSignIn() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun uploadBookDetails() {
        val editTextAuthor: EditText = findViewById(R.id.editTextAuthor)
        val editTextTitle: EditText = findViewById(R.id.editTextTitle)
        val spinnerCategory: Spinner = findViewById(R.id.spinnerCategory)
        val editTextDescription: EditText = findViewById(R.id.editTextDescription)
        val editTextPrice: EditText = findViewById(R.id.editTextPrice)
        val contactNumber: EditText = findViewById(R.id.contactNumber)


        val author = editTextAuthor.text.toString().trim()
        val title = editTextTitle.text.toString().trim()
        val category = spinnerCategory.selectedItem.toString()
        val description = editTextDescription.text.toString().trim()
        val price = editTextPrice.text.toString().trim()
        val contact = contactNumber.text.toString().trim()


        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        uploadBookDetailsToFirebase(author, title, category, description, price, contact)
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
        }
    }

    private fun uploadBookDetailsToFirebase(author: String, title: String, category: String, description: String, price: String, contact: String) {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${title}_${System.currentTimeMillis()}")
        selectedImageUri?.let {
            storageRef.putFile(it)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        saveBookDetailsToFirestore(author, title, category, description, price, contact, uri.toString())
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveBookDetailsToFirestore(author: String, title: String, category: String, description: String, price: String, contact: String, imageUrl: String) {
        val firestoreDb = FirebaseFirestore.getInstance()
        val bookData = hashMapOf(
            "author" to author,
            "title" to title,
            "category" to category,
            "description" to description,
            "price" to price,
            "contact" to contact,
            "image" to imageUrl
        )

        firestoreDb.collection("files").add(bookData)
            .addOnSuccessListener {
                Toast.makeText(this, "Book details uploaded", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload book details", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
