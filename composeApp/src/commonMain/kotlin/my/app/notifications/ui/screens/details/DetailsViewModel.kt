package my.app.notifications.ui.screens.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import my.app.notifications.models.DeliveryStatus
import my.app.notifications.models.Notification
import my.app.notifications.models.getPlaceholderNotification
import my.app.notifications.network.NotificationApiClient
import my.app.notifications.network.NotificationWebSocketClient

class DetailsViewModel : ViewModel() {

    private val apiClient = NotificationApiClient()
    private val webSocketClient = NotificationWebSocketClient()
    private val scope = CoroutineScope(Dispatchers.Main)
    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isWebSocketConnected by mutableStateOf(false)
        private set

    var notifications = mutableStateOf<List<Notification>>(emptyList())
        private set

    private val _notification = MutableStateFlow<Notification?>(null)
    val notification: StateFlow<Notification?> = _notification.asStateFlow()

    fun updateNotification(newNotification: Notification) {
        // To update, simply assign to the .value property
        _notification.value = newNotification
    }

    var isLoading by mutableStateOf(false)
        private set

    init {
        println("Viewmodel init")
        connectWebSocket()
        loadNotifications()
    }

    private fun connectWebSocket() {
        scope.launch {
            // Connect to WebSocket
            webSocketClient.connect(scope)

            // Observe connection state
            launch {
                webSocketClient.connectionState.collect { state ->
                    isWebSocketConnected = state == NotificationWebSocketClient.ConnectionState.CONNECTED
                }
            }

            // Observe new/updated notifications
            launch {
                webSocketClient.notifications.collect { notification ->
                    if (notification != null) {
                        handleNewNotification(notification)
                    }
                }
            }

        }
    }

    private fun handleNewNotification(notification: Notification) {
        println("Received new notification via WebSocket: ${notification.title}")
        val existingIndex = notifications.value.indexOfFirst { it.id == notification.id }
        notifications.value = when (existingIndex) {
            0 -> notifications.value.toMutableList().apply {
                set(existingIndex, notification)
            }
            else -> listOf(notification) + notifications.value.toMutableList()
        }
    }

    fun loadNotifications() {
        scope.launch {
            isLoading = true
            errorMessage = null

            try {
                notifications.value = apiClient.getAllNotifications()
            } catch (e: Exception) {
                errorMessage = "Failed to load notifications: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    fun getNotificationById(id: String) {
        viewModelScope.launch {
            isLoading = true

            // If notifications list is empty, wait for loadNotifications() to finish
            // or trigger it if it hasn't started.
            if (notifications.value.isEmpty()) {
                loadNotifications() // Wait for the network call
            }
            delay(200)
            val found = notifications.value.find { it.id == id }
            if (found != null) {
                _notification.value = found
            } else {
                println("Id not found: $id")
            }
            isLoading = false
        }
    }

    fun markAsRead() {
        viewModelScope.launch {
            if (_notification.value != null) {
                apiClient.updateNotificationStatus(_notification.value!!.id, DeliveryStatus.READ).let {
                    if (it != null) {
                        updateNotification(it)
                    }
                }
            }
        }
    }
}