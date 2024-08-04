package com.example.bookcycle

import FileItem
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var itemsRecyclerView: RecyclerView
    private lateinit var fileAdapter: FileAdapter
    private val files = mutableListOf<FileItem>()
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)
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

        // Set up search functionality
        val searchEditText: EditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }
        })

        val categories = listOf("All","Fiction", "Non-Fiction", "Science", "History", "Biography", "Fantasy", "Mystery", "Romance")

        // Spinner setup
        val filterSpinner: Spinner = findViewById(R.id.filterSpinner)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = spinnerAdapter

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedCategory = categories[position]
                applyFilter(selectedCategory)
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
            it.title.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
        fileAdapter.updateList(filteredList)
    }

    private fun applyFilter(category: String) {
        val filteredList = if (category == "All") {
            files // Show all files if "All" or a non-category is selected
        } else {
            files.filter { it.category == category } // Filter based on category
        }
        fileAdapter.updateList(filteredList)
    }


}







