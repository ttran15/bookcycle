package com.example.bookcycle

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Book(
    val title: String = "",
    val author: String = "",
    val category: String = "",
    val description: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val userId: String = ""
)
class UserBooksActivity : AppCompatActivity() {

    private lateinit var booksRecyclerView: RecyclerView
    private lateinit var booksAdapter: BooksAdapter
    private val books = mutableListOf<Book>()
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_books)

        booksRecyclerView = findViewById(R.id.booksRecyclerView)
        booksRecyclerView.layoutManager = LinearLayoutManager(this)
        booksAdapter = BooksAdapter(this, books, ::deleteBook)  // Pass the delete callback
        booksRecyclerView.adapter = booksAdapter

        firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            fetchUserBooks(userId)
        } else {
            Log.d("UserBooksActivity", "User ID is null. Redirecting to sign-in.")
            Toast.makeText(this, "Please sign in to view your books.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUserBooks(userId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("files")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                books.clear()
                for (document in documents) {
                    val book = document.toObject(Book::class.java)
                    books.add(book)
                }
                booksAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("UserBooksActivity", "Error getting documents: ", exception)
                Toast.makeText(this, "Error fetching books. Please try again.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteBook(book: Book) {
        val db = FirebaseFirestore.getInstance()
        db.collection("files")
            .whereEqualTo("userId", firebaseAuth.currentUser?.uid)
            .whereEqualTo("title", book.title)  // Assuming title is unique for each book
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("files").document(document.id).delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Book deleted.", Toast.LENGTH_SHORT).show()
                            books.remove(book)
                            booksAdapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener { exception ->
                            Log.w("UserBooksActivity", "Error deleting document: ", exception)
                            Toast.makeText(this, "Error deleting book. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("UserBooksActivity", "Error finding book: ", exception)
                Toast.makeText(this, "Error finding book. Please try again.", Toast.LENGTH_SHORT).show()
            }
    }
}
