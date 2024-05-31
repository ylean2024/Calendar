package com.example.android.calendar.ui

import android.R
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzadelivery.ui.utils.SearchHistoryManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.android.calendar.databinding.FragmentNewsBinding
import com.example.android.calendar.model.Article
import com.example.android.calendar.ui.adapter.NewsAdapter
import kotlinx.coroutines.delay

class NewsFragment : BottomSheetDialogFragment() {
    private val viewModel: CalendarViewModel by activityViewModels()

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            actionId == EditorInfo.IME_ACTION_SEARCH
        }

        binding.buttonClear.setOnClickListener {
            binding.editTextSearch.text.clear()
            hideKeyboard()
        }

        val history = SearchHistoryManager.getHistory(requireContext()).toMutableList()
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line,
            SearchHistoryManager.getHistory(requireContext()).reversed()
        )


        binding.editTextSearch.setAdapter(adapter)

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (binding.searchResults.adapter != null) {
                    filterRecyclerViewItems(query)
                } else {
                    binding.editTextSearch.text.clear()
                }
                if (query.length > 5 && !history.contains(query)) SearchHistoryManager.addToHistory(
                    requireContext(),
                    query
                )
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.buttonClear.visibility = View.GONE
                } else {
                    binding.buttonClear.visibility = View.VISIBLE
                }
            }
        })

        viewModel.newsItemsLiveData.observe(viewLifecycleOwner) { response ->
            showNewsItems(response)
            binding.customProgressBar.root.visibility = View.GONE
        }

        binding.refreshButton.setOnClickListener {
            binding.placeholderText.setText("Данные загружаются...")
            viewModel.fetchNews()
            binding.customProgressBar.root.visibility = View.VISIBLE
            it.isEnabled = false
        }
    }

    private fun showNewsItems(newsItems: List<Article>) {
        if (newsItems.isNotEmpty()) {
            binding.placeholderText.visibility = View.GONE
            binding.refreshButton.visibility = View.GONE
            binding.searchResults.layoutManager = LinearLayoutManager(context)
            binding.searchResults.adapter = NewsAdapter(newsItems)
        } else {
            binding.placeholderText.visibility = View.VISIBLE
            binding.refreshButton.visibility = View.VISIBLE
            binding.placeholderText.setText("Произошла ошибка загрузки")
            binding.refreshButton.isEnabled = true
        }
    }

    private fun filterRecyclerViewItems(query: String) {
        val adapter = binding.searchResults.adapter as NewsAdapter
        adapter.filter.filter(query)
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}