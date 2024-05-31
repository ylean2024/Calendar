package com.example.android.calendar.ui

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.android.calendar.databinding.FragmentAddEventBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.android.calendar.model.Event
import com.example.android.calendar.model.Friend
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_CODE_POST_NOTIFICATIONS = 1

class AddEventFragment : BottomSheetDialogFragment() {
    private val viewModel: CalendarViewModel by activityViewModels()
    private var _binding: FragmentAddEventBinding? = null
    private val binding get() = _binding!!
    private var userId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEventBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = viewModel.userLiveData.value!!.id

        binding.dateTextView.setOnClickListener {
            openDatePicker()
        }

        binding.startTimeEditText.addTextChangedListener(object : TextWatcher {
            private var isFormatting: Boolean = false

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (isFormatting) return

                isFormatting = true

                val text = s.toString().replace(":", "")
                val formattedText = StringBuilder()

                if (text.length > 4) {
                    formattedText.append(text.substring(0, 4))
                } else {
                    formattedText.append(text)
                }

                if (formattedText.length >= 2) {
                    val firstPart = formattedText.substring(0, 2).toIntOrNull()
                    if (firstPart != null && firstPart > 23) {
                        formattedText.replace(0, 2, "23")
                    }
                }

                if (formattedText.length == 4) {
                    val secondPart = formattedText.substring(2, 4).toIntOrNull()
                    if (secondPart != null && secondPart > 59) {
                        formattedText.replace(2, 4, "59")
                    }
                }

                if (formattedText.length > 2) {
                    formattedText.insert(2, ":")
                }

                binding.startTimeEditText.setText(formattedText)
                binding.startTimeEditText.setSelection(formattedText.length)

                isFormatting = false
            }
        })

        binding.endTimeEditText.addTextChangedListener(object : TextWatcher {
            private var isFormatting: Boolean = false

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (isFormatting) return

                isFormatting = true

                val text = s.toString().replace(":", "")
                val formattedText = StringBuilder()

                if (text.length > 4) {
                    formattedText.append(text.substring(0, 4))
                } else {
                    formattedText.append(text)
                }

                if (formattedText.length >= 2) {
                    val firstPart = formattedText.substring(0, 2).toIntOrNull()
                    if (firstPart != null && firstPart > 23) {
                        formattedText.replace(0, 2, "23")
                    }
                }

                if (formattedText.length == 4) {
                    val secondPart = formattedText.substring(2, 4).toIntOrNull()
                    if (secondPart != null && secondPart > 59) {
                        formattedText.replace(2, 4, "59")
                    }
                }

                if (formattedText.length > 2) {
                    formattedText.insert(2, ":")
                }

                binding.endTimeEditText.setText(formattedText)
                binding.endTimeEditText.setSelection(formattedText.length)

                isFormatting = false
            }
        })

        binding.createEventButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPostNotificationsPermission()
            } else {
                createEventAndScheduleNotification()
            }
        }


    }

    private fun requestPostNotificationsPermission() {
        Toast.makeText(
            requireContext(),
            "Не выдано разрешение на уведомление, выдайте его в настройках.",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun createEventAndScheduleNotification() {
        val eventName = binding.eventNameEditText.text.toString()
        val eventDescription = binding.eventDescriptionEditText.text.toString()
        val date = binding.dateTextView.text.toString()
        val startTime = binding.startTimeEditText.text.toString()
        val endTime = binding.endTimeEditText.text.toString()
        val friends = emptyList<Friend>()

        if (eventName.isEmpty() || eventDescription.isEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_LONG).show()
            return
        }

        val dateTimeFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val startDateTime = dateTimeFormat.parse("$date $startTime")
        val endDateTime = dateTimeFormat.parse("$date $endTime")

        if (startDateTime == null || endDateTime == null || endDateTime.before(startDateTime)) {
            Toast.makeText(
                requireContext(),
                "Время окончания должно быть позже времени начала",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val event =
            Event(0, userId, eventName, eventDescription, date, startTime, endTime, friends, false)
        viewModel.addEvent(event)

        viewModel.scheduleEventNotification(event, requireContext())

        dismiss()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                createEventAndScheduleNotification()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Не дано разрешение на уведомления",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val formattedDay = String.format("%02d", selectedDay)
                binding.dateTextView.text = "$formattedDay.$formattedMonth.$selectedYear"
            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }
}