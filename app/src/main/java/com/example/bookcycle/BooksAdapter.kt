package com.example.bookcycle

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
class BooksAdapter(
    private val context: Context,
    private val books: MutableList<Book>,
    private val onDeleteClick: (Book) -> Unit  // Callback for delete action
) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.book_item_layout, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textTitle)
        private val authorTextView: TextView = itemView.findViewById(R.id.textAuthor)
        private val categoryTextView: TextView = itemView.findViewById(R.id.textCategory)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textDescription)
        private val priceTextView: TextView = itemView.findViewById(R.id.textPrice)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)

        fun bind(book: Book) {
            titleTextView.text = book.title
            authorTextView.text = book.author
            categoryTextView.text = book.category
            descriptionTextView.text = book.description
            priceTextView.text = "${book.price} CAD"
            Glide.with(context)
                .load(book.image)
                .placeholder(R.drawable.placeholder_image) // Placeholder image
                .error(R.drawable.error) // Error image
                .into(imageView)

            deleteButton.setOnClickListener {
                onDeleteClick(book)
            }
        }
    }
}
