package com.example.android.calendar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pizzadelivery.ui.utils.ThemeUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.android.calendar.databinding.FragmentSettingsBinding

class SettingsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.themeSwitcher.isChecked = ThemeUtil.checkTheme(requireContext())

        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                ThemeUtil.toggleTheme(requireActivity())
            } else {
                ThemeUtil.toggleTheme(requireActivity())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}