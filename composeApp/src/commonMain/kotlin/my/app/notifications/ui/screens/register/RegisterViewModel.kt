package my.app.notifications.ui.screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.app.notifications.network.UserApiClient

class RegisterViewModel : ViewModel() {
    private val userApiClient = UserApiClient()

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var registrationSuccessful by mutableStateOf(false)
        private set

    fun registerUser(username: String, email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            registrationSuccessful = false

            val result = userApiClient.registerUser(username, email, password)

            isLoading = false

            if (result != null) {
                println("User registered successfully: ${result.username}")
                registrationSuccessful = true
            } else {
                println("Registration failed")
                errorMessage = "Registration failed. Please try again."
            }
        }
    }

    fun resetRegistrationState() {
        registrationSuccessful = false
        errorMessage = null
    }
}