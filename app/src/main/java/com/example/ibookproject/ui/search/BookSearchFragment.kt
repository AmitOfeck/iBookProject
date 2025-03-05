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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ibookproject.R
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.ui.book.BooksAdapter

class BookSearchFragment : Fragment() {

    private lateinit var booksAdapter: BooksAdapter
    private lateinit var etSearch: EditText
    private lateinit var spinnerSort: Spinner
    private lateinit var genreButtons: List<Button>
    private lateinit var rvBooks: RecyclerView

    private val bookViewModel: BookSearchViewModel by activityViewModels()

    private var displayedBooks = mutableListOf<BookEntity>()
    private val selectedGenres = mutableSetOf<String>()

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

        booksAdapter = BooksAdapter(displayedBooks, { bookId ->
            val bundle = Bundle().apply {
                putInt("bookId", bookId)
            }
            findNavController().navigate(R.id.action_searchBookFragment_to_bookDetailsFragment, bundle)
        })
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
                toggleGenreFilter(button)
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

        // **טעינת הנתונים מה-ViewModel**
        bookViewModel.getAllBooks().observe(viewLifecycleOwner) { books ->
            displayedBooks = books.toMutableList()
            booksAdapter.updateBooks(displayedBooks)
        }

        return view
    }

    private fun filterBooks() {
        val query = etSearch.text.toString().lowercase()
        val filteredBooks = displayedBooks.filter {
            it.title.lowercase().contains(query) ||
                    it.author.lowercase().contains(query) ||
                    it.genre.lowercase().contains(query)
        }.toMutableList()

        if (selectedGenres.isNotEmpty()) {
            filteredBooks.retainAll { it.genre in selectedGenres }
        }

        booksAdapter.updateBooks(filteredBooks)
    }

    private fun toggleGenreFilter(button: Button) {
        val genre = button.text.toString()
        if (selectedGenres.contains(genre)) {
            selectedGenres.remove(genre)
            button.setBackgroundColor(resources.getColor(R.color.purple_700))
        } else {
            selectedGenres.add(genre)
            button.setBackgroundColor(resources.getColor(R.color.black))
        }
        filterBooks()
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
