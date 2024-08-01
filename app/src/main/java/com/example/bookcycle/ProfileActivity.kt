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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.auth
import com.example.bookcycle.databinding.ProfileLayoutBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

data class User(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val postal_code: String = "",
    val password: String = ""
)

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ProfileLayoutBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ProfileLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid.toString()

        // load data
        if (uid.isNotEmpty()) {
            fetchUserData(uid)
        }

        // change intent

        //// logout -> sign in
        binding.imgLogout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        //// edit profile -> update profile
        binding.imgEditProfile.setOnClickListener {
            val intent = Intent(this,UpdateProfileActivity::class.java)
            startActivity(intent)
        }

        //// edit password -> update password
        binding.imgEditPassword.setOnClickListener {
            val intent = Intent(this,UpdatePasswordActivity::class.java)
            startActivity(intent)
        }

        // menu
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_homepage -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.item_booklist -> {
                    val intent = Intent(this, HomeActivity::class.java)
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
    }

    private fun fetchUserData(uid: String) {
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("user_db").document(uid)

        usersRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Convert the document to a User object
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        binding.txtName.setText(user.name)
                        binding.edtEmail.setText(user.email)
                        binding.edtPhone.setText(user.phone)
                        binding.edtAddress.setText(user.address)
                        binding.edtPostalCode.setText(user.postal_code)
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
}