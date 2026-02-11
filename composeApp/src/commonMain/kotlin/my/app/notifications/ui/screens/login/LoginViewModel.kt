package my.app.notifications.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.app.notifications.network.UserApiClient

class LoginViewModel : ViewModel() {
    private val userApiClient = UserApiClient()

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var loginSuccessful by mutableStateOf(false)
        private set

    var loggedInUser by mutableStateOf<String?>(null)
        private set

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            loginSuccessful = false

            val result = userApiClient.loginUser(username, password)

            isLoading = false

            if (result != null) {
                println("User logged in successfully: ${result.username}")
                loggedInUser = result.username
                loginSuccessful = true
            } else {
                println("Login failed")
                errorMessage = "Invalid username or password."
            }
        }
    }

    fun resetLoginState() {
        loginSuccessful = false
        errorMessage = null
    }
}