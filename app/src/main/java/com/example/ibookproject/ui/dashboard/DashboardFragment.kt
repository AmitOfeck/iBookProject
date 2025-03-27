package com.example.ibookproject.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ibookproject.R
import com.example.ibookproject.databinding.FragmentDashboardBinding
import com.example.ibookproject.Utils

class DashboardFragment : Fragment() {

    private lateinit var quoteViewModel: QuoteViewModel
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var quoteAdapter: QuoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        quoteViewModel = ViewModelProvider(this).get(QuoteViewModel::class.java)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        Utils.manageLoadingSpinner(binding.progressBar, true)

        quoteViewModel.quotes.observe(viewLifecycleOwner, Observer { quotes ->
            quoteAdapter = QuoteAdapter(quotes)
            binding.recyclerView.adapter = quoteAdapter

            Utils.manageLoadingSpinner(binding.progressBar, false)
        })

        quoteViewModel.loadQuotes()

        binding.refreshButton.setOnClickListener {
            Utils.manageLoadingSpinner(binding.progressBar, true)
            quoteViewModel.loadQuotes()
        }

        return binding.root
    }
}
