package com.example.android.calendar.ui.log_reg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.calendar.db.Repository
import com.example.android.calendar.db.user.UserDb
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {
    private val repository: Repository = Repository.get()

    fun registerUser(email: String, password: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val checkUser = repository.getUserByEmail(email)
            if(checkUser != null){
                callback(false)
                return@launch
            }

            val user = UserDb(email = email, password = password, showToAllFriends = false)
            repository.insertUser(user)
            callback(true)
        }
    }

    fun loginUser(email: String, password: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email)
            if (user != null && user.password == password) {
                callback(true)
            } else {
                callback(false)
            }
        }
    }
}