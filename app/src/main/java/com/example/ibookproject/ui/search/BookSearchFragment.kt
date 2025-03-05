package com.example.ibookproject.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ibookproject.R
import com.example.ibookproject.ui.book.Book
import com.example.ibookproject.ui.book.BooksAdapter

class BookSearchFragment : Fragment() {

    private lateinit var booksAdapter: BooksAdapter
    private lateinit var etSearch: EditText
    private lateinit var spinnerSort: Spinner
    private lateinit var genreButtons: List<Button>
    private lateinit var rvBooks: RecyclerView

    private var allBooks = listOf(
        Book("The Great", "John Smith", "Adventure", 4, R.drawable.img),
        Book("Mystery at the Mansion", "Emily Clark", "Mystery", 4, R.drawable.img),
        Book("Science and Discovery", "Dr. Alice Wong", "Non-Fiction", 4, R.drawable.img)
    )

    private var displayedBooks = allBooks.toMutableList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        etSearch = view.findViewById(R.id.etSearch)
        spinnerSort = view.findViewById(R.id.spinnerSort)
        rvBooks = view.findViewById(R.id.rvBooks)

        genreButtons = listOf(
            view.findViewById(R.id.btnFiction),
            view.findViewById(R.id.btnRomance),
            view.findViewById(R.id.btnMystery),
            view.findViewById(R.id.btnHistory)
        )

        booksAdapter = BooksAdapter(displayedBooks)
        rvBooks.layoutManager = LinearLayoutManager(context)
        rvBooks.adapter = booksAdapter

        etSearch.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                filterBooks()
                return@setOnKeyListener true
            }
            false
        }

        genreButtons.forEach { button ->
            button.setOnClickListener {
                filterBooksByGenre(button.text.toString())
            }
        }

        val sortOptions = arrayOf("Ratings", "Book Title", "Author Name")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSort.adapter = adapter
        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sortBooks(sortOptions[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        return view
    }

    private fun filterBooks() {
        val query = etSearch.text.toString().lowercase()
        displayedBooks = allBooks.filter {
                    it.title.lowercase().contains(query) ||
                    it.author.lowercase().contains(query) ||
                    it.genre.lowercase().contains(query)
        }.toMutableList()
        booksAdapter.updateBooks(displayedBooks)
    }

    private fun filterBooksByGenre(genre: String) {
        displayedBooks = allBooks.filter { it.genre.equals(genre, ignoreCase = true) }.toMutableList()
        booksAdapter.updateBooks(displayedBooks)
    }

    private fun sortBooks(option: String) {
        displayedBooks = when (option) {
            "Ratings" -> displayedBooks.sortedByDescending { it.rating }.toMutableList()
            "Book Title" -> displayedBooks.sortedBy { it.title }.toMutableList()
            "Author Name" -> displayedBooks.sortedBy { it.author }.toMutableList()
            else -> displayedBooks
        }
        booksAdapter.updateBooks(displayedBooks)
    }
}
