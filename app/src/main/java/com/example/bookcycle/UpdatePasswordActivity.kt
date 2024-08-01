package com.example.bookcycle

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class UpdatePasswordActivity : AppCompatActivity() {
    private lateinit var edtEmail: Email
    private lateinit var btnUpdate: Button
    private lateinit var btnCancel: Button

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_password)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        // get item by id
        val edtEmail: EditText = findViewById(R.id.edtEmail)
        val btnUpdate: Button = findViewById(R.id.btnUpdate)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        // change intent
        //// update -> sign in
        btnUpdate.setOnClickListener {
            val resetEmail = edtEmail.text.toString()
            firebaseAuth.sendPasswordResetEmail(resetEmail)
                .addOnSuccessListener {
                    Toast.makeText(this,"Please check your email",Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        //// cancel -> sign in
        btnCancel.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}