package com.taufiq.tastywasty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufiq.tastywasty.data.model.User
import com.taufiq.tastywasty.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun register(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.register(User(email = email, password = password))
            if (result == -1L) {
                errorMessage = "Email already exists"
            } else {
                val user = repository.login(email, password)
                _currentUser.value = user
            }
        }
    }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = repository.login(email, password)
            if (user != null) {
                _currentUser.value = user
                errorMessage = null
            } else {
                errorMessage = "Invalid credentials"
            }
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String): String {
        val user = _currentUser.value ?: return "No user logged in"

        return when {
            currentPassword != user.password -> "Incorrect current password"
            newPassword.length < 4 -> "Password must be at least 4 characters"
            newPassword != confirmPassword -> "New passwords do not match"
            else -> {
                val updatedUser = user.copy(password = newPassword)
                viewModelScope.launch {
                    repository.updateUser(updatedUser)
                    _currentUser.value = updatedUser
                }
                "Password changed successfully"
            }
        }
    }


}
