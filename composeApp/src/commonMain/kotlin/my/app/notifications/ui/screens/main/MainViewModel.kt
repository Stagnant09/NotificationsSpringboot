package my.app.notifications.ui.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import my.app.notifications.models.Notification
import my.app.notifications.models.NotificationPriority
import my.app.notifications.network.NotificationApiClient
import my.app.notifications.network.NotificationWebSocketClient
import my.app.notifications.statics.currentUser

class MainViewModel {
    private val apiClient = NotificationApiClient()
    private val webSocketClient = NotificationWebSocketClient()
    private val scope = CoroutineScope(Dispatchers.Main)
    private var currentSearchQuery: String = ""


    var notifications = mutableStateOf<List<Notification>>(emptyList())
        private set

    var displayedNotifications = mutableStateOf<List<Notification>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isWebSocketConnected by mutableStateOf(false)
        private set

    init {
        // Start WebSocket connection
        connectWebSocket()

        // Load initial notifications
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

            // Observe deleted notifications
            launch {
                webSocketClient.deletedNotificationId.collect { id ->
                    if (id != null) {
                        handleDeletedNotification(id)
                    }
                }
            }
        }
    }

    private fun handleNewNotification(notification: Notification) {
        println("Received new notification via WebSocket: ${notification.title}")

        // Update or add notification to list
        val existingIndex = notifications.value.indexOfFirst { it.id == notification.id }
        notifications.value = when (existingIndex) {
            0 -> notifications.value.toMutableList().apply {
                set(existingIndex, notification)
            }
            else -> listOf(notification) + notifications.value.toMutableList()
        }

        // Update displayed list
        applyFiltersAndSort()
    }

    private fun handleDeletedNotification(id: String) {
        println("Notification deleted via WebSocket: $id")

        notifications.value = notifications.value.filter { it.id != id }
        applyFiltersAndSort()
    }

    private var currentSortOption: String? = null

    private fun applyFiltersAndSort() {
        var result = notifications.value

        if (currentSearchQuery.isNotBlank()) {
            val query = currentSearchQuery.lowercase()
            result = result.filter {
                it.title.lowercase().contains(query) ||
                        it.message.lowercase().contains(query)
            }
        }

        result = when (currentSortOption) {
            "Priority" -> result.sortedByDescending { it.priority }
            "Date" -> result.sortedByDescending { it.time }
            "Status" -> result.sortedBy { it.deliveryStatus }
            else -> result
        }

        displayedNotifications.value = result
    }

    fun loadNotifications() {
        scope.launch {
            isLoading = true
            errorMessage = null

            try {
                notifications.value = apiClient.getNotificationsByUser(currentUser) ?: throw Exception()
                displayedNotifications.value = notifications.value
            } catch (e: Exception) {
                errorMessage = "Failed to load notifications: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun createNotification(title: String, message: String, priority: NotificationPriority) {
        scope.launch {
            isLoading = true

            try {
                val newNotification = apiClient.createNotification(title, message, priority)
                // No need to manually refresh - WebSocket will push the update
                if (newNotification == null) {
                    errorMessage = "Failed to create notification"
                }
            } catch (e: Exception) {
                errorMessage = "Failed to create notification: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun sortNotifications(option: String) {
        currentSortOption = option

        when (option) {
            "Priority" -> {
                displayedNotifications.value = notifications.value.sortedByDescending { it.priority }
            }
            "Date" -> {
                displayedNotifications.value = notifications.value.sortedByDescending { it.time }
            }
            "Status" -> {
                displayedNotifications.value = notifications.value.sortedBy { it.deliveryStatus }
            }
        }
    }

    fun sortAndSearch(value: String, option: String = "") {
        currentSearchQuery = value
        currentSortOption = option.ifBlank { currentSortOption }
        applyFiltersAndSort()
    }

    suspend fun reconnectWebSocket() {
        webSocketClient.disconnect()
        connectWebSocket()
    }

    suspend fun onCleared() {
        webSocketClient.disconnect()
    }
}