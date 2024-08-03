package com.example.bookcycle

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
<<<<<<< Updated upstream
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
=======
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
>>>>>>> Stashed changes
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
    private lateinit var itemsRecyclerView: RecyclerView
    private lateinit var fileAdapter: FileAdapter
    private val files = mutableListOf<FileItem>()
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)
<<<<<<< Updated upstream
=======

        itemsRecyclerView = findViewById(R.id.itemsRecyclerView)
        itemsRecyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns in the grid

        fileAdapter = FileAdapter(this, files) { fileItem ->
            Toast.makeText(this, "Clicked ${fileItem.title}", Toast.LENGTH_SHORT).show()
        }
        itemsRecyclerView.adapter = fileAdapter

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // Load files from Firestore
        loadFilesFromFirestore()

        val searchEditText: EditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }
        })

        // Menu
>>>>>>> Stashed changes
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val backButton: Button = findViewById(R.id.back1)
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
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView)
        itemsRecyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns in the grid

        fileAdapter = FileAdapter(this, files) { fileItem ->
            Toast.makeText(this, "Clicked ${fileItem.name}", Toast.LENGTH_SHORT).show()
        }
        itemsRecyclerView.adapter = fileAdapter

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // Load files from Firestore
        loadFilesFromFirestore()

        // Set up search functionality
        val searchEditText: EditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }
        })

        // Set up filter functionality
        val filterSpinner: Spinner = findViewById(R.id.filterSpinner)
        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val filterOption = parent.getItemAtPosition(position).toString()
                applyFilter(filterOption)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun loadFilesFromFirestore() {
        db.collection("files").addSnapshotListener { snapshots, e ->
            if (e != null) {
                Toast.makeText(this, "Error loading files.", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (snapshots != null) {
                files.clear()
                for (doc in snapshots) {
                    val fileItem = doc.toObject(FileItem::class.java)
                    files.add(fileItem)
                }
                fileAdapter.updateList(files)
            }
        }
    }

    private fun filterList(query: String) {
        val filteredList = files.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
        fileAdapter.updateList(filteredList)
    }

    private fun applyFilter(option: String) {
        val filteredList = when (option) {
            "Cost" -> files.sortedBy { it.price.toDoubleOrNull() ?: Double.MAX_VALUE }
            "Genre" -> files // Assuming a genre field exists
            "Author" -> files // Assuming an author field exists
            else -> files
        }
        fileAdapter.updateList(filteredList)
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Optional: close the current activity
    }

    private fun loadFilesFromFirestore() {
        db.collection("book_db").addSnapshotListener { snapshots, e ->
            if (e != null) {
                Toast.makeText(this, "Error loading books.", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (snapshots != null) {
                files.clear()
                for (doc in snapshots) {
                    val fileItem = doc.toObject(FileItem::class.java)
                    files.add(fileItem)
                }
                fileAdapter.updateList(files)
            }
        }
    }

    private fun filterList(query: String) {
        val filteredList = files.filter {
            it.title.contains(query, ignoreCase = true)
        }
        fileAdapter.updateList(filteredList)
    }
}







