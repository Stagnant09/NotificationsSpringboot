package my.app.notifications.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute {
    @Serializable data object MainRoute : AppRoute
    @Serializable data class DetailsRoute(val notificationId: String) : AppRoute
    @Serializable data object LoginRoute : AppRoute
    @Serializable data object RegisterRoute : AppRoute
}