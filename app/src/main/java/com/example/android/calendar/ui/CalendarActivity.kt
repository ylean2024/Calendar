package com.example.android.calendar.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.android.calendar.R
import com.example.android.calendar.databinding.ActivityCalendarBinding

class CalendarActivity : AppCompatActivity() {

    private var _binding: ActivityCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavView
        navController =
            (supportFragmentManager.findFragmentById(R.id.mainContainerView) as NavHostFragment).navController
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.calendarFragment -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }

                else -> {
                    bottomNavigationView.visibility = View.GONE
                }
            }
        }
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.calendarFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}