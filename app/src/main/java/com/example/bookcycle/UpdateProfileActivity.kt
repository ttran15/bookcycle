package com.example.bookcycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookcycle.databinding.SignInLayoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class AllUser(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val postal_code: String = "",
    val password: String = ""
)

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtAddress: EditText
    private lateinit var edtPostalCode: EditText
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_profile)

        // get item by id
        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPhone = findViewById(R.id.edtPhone)
        edtAddress = findViewById(R.id.edtAddress)
        edtPostalCode = findViewById(R.id.edtPostalCode)
        val btnUpdate: Button = findViewById(R.id.btnUpdate)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid.toString()

        // load data
        if (uid.isNotEmpty()) {
            fetchUserData(uid)
        }

        // change intent
        //// update -> profile
        btnUpdate.setOnClickListener {
            val update_edtName = edtName.text.toString()
            val update_edtEmail = edtEmail.text.toString()
            val update_edtPhone = edtPhone.text.toString()
            val update_edtAddress = edtAddress.text.toString()
            val update_edtPostalCode = edtPostalCode.text.toString()

            saveFullUserFireStore(update_edtName, update_edtEmail, update_edtPhone, update_edtAddress, update_edtPostalCode, uid)
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        //// cancel -> profile
        btnCancel.setOnClickListener {
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
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
    private fun fetchUserData(uid: String) {
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("user_db").document(uid)

        usersRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Convert the document to a User object
                    val user = document.toObject(AllUser::class.java)
                    if (user != null) {
                        edtName.setText(user.name)
                        edtEmail.setText(user.email)
                        edtPhone.setText(user.phone)
                        edtAddress.setText(user.address)
                        edtPostalCode.setText(user.postal_code)
                    } else {
                        Log.d("MainActivity", "User data is null")
                    }
                } else {
                    Log.d("MainActivity", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents.", exception)
            }
    }

    fun saveFullUserFireStore(name: String, email: String, phone:String, address: String, postalCode: String, uid: String) {
        // init firebase firestore
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["name"] = name
        user["email"] = email
        user["phone"] = phone
        user["address"] = address
        user["postal_code"] = postalCode

        if (name.isBlank() || email.isBlank()) {
            Toast.makeText(this, "Name and Email cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("user_db").document(uid)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "record added successfully ", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "record Failed to add ", Toast.LENGTH_SHORT).show()
            }
        }
    }


