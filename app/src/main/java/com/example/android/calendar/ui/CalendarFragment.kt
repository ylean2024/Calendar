package com.example.android.calendar.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.calendar.R
import com.example.android.calendar.databinding.FragmentCalendarBinding
import com.example.android.calendar.model.Event
import com.example.android.calendar.ui.adapter.EventAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by activityViewModels()

    private lateinit var eventAdapter: EventAdapter
    private var email: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString("email")
        val emailFromArguments = email
        if(emailFromArguments != null){
            viewModel.getUser(emailFromArguments)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapter = EventAdapter(listOf()){}
        binding.eventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.eventsRecyclerView.adapter = eventAdapter


        binding.calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            val selectedDate: Calendar = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val date: Date = selectedDate.time
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            viewModel.loadEventsForDate(dateFormat.format(date), userId1 = viewModel.userLiveData.value!!.id)
        }

        viewModel.eventsLiveData.observe(viewLifecycleOwner) { events ->
            setAdapter(events)
        }

        viewModel.userLiveData.observe(viewLifecycleOwner){ user ->
            if(user == null && viewModel.wasNavigateToLogin != true){
                findNavController().navigate(R.id.loginFragment)
            }
        }

        binding.addEventButton.setOnClickListener {
            val addEventFragment = AddEventFragment()
            addEventFragment.show(parentFragmentManager, "AddEventFragment")
        }


    }

    private fun setAdapter(events: List<Event>){
        binding.eventsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.eventsRecyclerView.adapter = EventAdapter(events){
            viewModel.deleteEvent(it)
        }
    }

}
