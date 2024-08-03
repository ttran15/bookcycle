package com.example.bookcycle

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FileAdapter(
    private val context: Context,
<<<<<<< Updated upstream
    private var files: List<FileItem>,
=======
    private var book_db: List<FileItem>, // Updated collection name
>>>>>>> Stashed changes
    private val itemClickListener: (FileItem) -> Unit
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)

        fun bind(fileItem: FileItem) {
            nameTextView.text = fileItem.title // Updated property name
            descriptionTextView.text = fileItem.description
            priceTextView.text = fileItem.price

            // Load image using Glide
            Glide.with(context)
<<<<<<< Updated upstream
                .load(fileItem.imageUrl)
=======
                .load(fileItem.image) // Updated property name
>>>>>>> Stashed changes
                .placeholder(R.drawable.placeholder_image) // Placeholder image
                .error(R.drawable.error) // Error image
                .into(imageView)

            itemView.setOnClickListener {
                // Pass the clicked item to the click listener
                itemClickListener(fileItem)

                // Intent to navigate to BookDetailsActivity
                val intent = Intent(context, BookDetailsActivity::class.java).apply {
                    putExtra("title", fileItem.title) // Updated property name
                    putExtra("description", fileItem.description)
                    putExtra("price", fileItem.price)
                    putExtra("image", fileItem.image) // Updated property name
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file, parent, false)
        return FileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val fileItem = book_db[position] // Updated collection name
        holder.bind(fileItem)
    }

    override fun getItemCount(): Int {
        return book_db.size // Updated collection name
    }

    fun updateList(newList: List<FileItem>) {
<<<<<<< Updated upstream
        files = newList
=======
        book_db = newList // Updated collection name
>>>>>>> Stashed changes
        notifyDataSetChanged()
    }
}
