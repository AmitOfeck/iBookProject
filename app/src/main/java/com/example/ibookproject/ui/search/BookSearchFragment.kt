package com.example.ibookproject.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ibookproject.R
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.ui.Genres
import com.example.ibookproject.ui.book.BooksAdapter

class BookSearchFragment : Fragment() {

    private lateinit var booksAdapter: BooksAdapter
    private lateinit var etSearch: EditText
    private lateinit var spinnerSort: Spinner
    private lateinit var spinnerGenre: Spinner
    private lateinit var rvBooks: RecyclerView

    private val bookViewModel: BookSearchViewModel by activityViewModels()

    private var displayedBooks = mutableListOf<BookEntity>()
    private var selectedGenre: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        etSearch = view.findViewById(R.id.etSearch)
        spinnerSort = view.findViewById(R.id.spinnerSort)
        spinnerGenre = view.findViewById(R.id.spinnerGenre)
        rvBooks = view.findViewById(R.id.rvBooks)

        booksAdapter = BooksAdapter(displayedBooks) { bookId ->
            val bundle = Bundle().apply {
                putString("bookId", bookId)
            }
            findNavController().navigate(R.id.action_searchBookFragment_to_bookDetailsFragment, bundle)
        }

        rvBooks.layoutManager = LinearLayoutManager(context)
        rvBooks.adapter = booksAdapter

        etSearch.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                filterBooks()
                return@setOnKeyListener true
            }
            false
        }

        val sortOptions = arrayOf("Ratings", "Book Title", "Author Name")
        val sortAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions)
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSort.adapter = sortAdapter

        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sortBooks(sortOptions[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // הוספת כל הז'אנרים ל-Spinner
        val genresList = mutableListOf("All") + Genres.getAll()
        val genreAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genresList)
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenre.adapter = genreAdapter

        spinnerGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedGenre = if (position == 0) null else genresList[position]
                filterBooks()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        bookViewModel.getAllBooks().observe(viewLifecycleOwner) { books ->
            displayedBooks = books.toMutableList()
            filterBooks()
        }

        return view
    }

    private fun filterBooks() {
        val query = etSearch.text.toString().lowercase()

        val filteredBooks = displayedBooks.filter { book ->
            (book.title.lowercase().contains(query) ||
                    book.author.lowercase().contains(query) ||
                    book.genre.lowercase().contains(query)) &&
                    (selectedGenre == null || book.genre == selectedGenre)
        }.toMutableList()

        booksAdapter.updateBooks(filteredBooks)
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
