package my.app.notifications

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import my.app.notifications.models.Notification
import my.app.notifications.models.NotificationPriority
import my.app.notifications.models.emptyUser
import my.app.notifications.statics.currentUser
import my.app.notifications.ui.navigation.AppRoute
import my.app.notifications.ui.screens.details.DetailsScreen
import my.app.notifications.ui.screens.details.DetailsViewModel
import my.app.notifications.ui.screens.login.LoginScreen
import my.app.notifications.ui.screens.login.LoginViewModel
import my.app.notifications.ui.screens.main.MainScreen
import my.app.notifications.ui.screens.register.RegisterScreen
import my.app.notifications.ui.screens.register.RegisterViewModel
import org.jetbrains.compose.resources.painterResource

import notificationsdisplay.composeapp.generated.resources.Res
import notificationsdisplay.composeapp.generated.resources.compose_multiplatform
import kotlin.time.Clock

@Composable
fun App() {
    println("Init")
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = AppRoute.LoginRoute
        ) {
            composable<AppRoute.MainRoute> {
                do {

                } while (currentUser == emptyUser)
                MainScreen(
                    onNotificationClick = { notification ->
                        navController.navigate(AppRoute.DetailsRoute(notification.id))
                    },
                    onLogoutClick = {
                        navController.navigate(AppRoute.LoginRoute)
                    }
                )
            }

            composable<AppRoute.DetailsRoute> { backStackEntry ->
                val route: AppRoute.DetailsRoute = backStackEntry.toRoute()
                val viewModel: DetailsViewModel = viewModel(factory = viewModelFactory {
                    initializer { DetailsViewModel() }
                })

                LaunchedEffect(route.notificationId) {
                    viewModel.getNotificationById(route.notificationId)
                }

                val notification = viewModel.notification.collectAsStateWithLifecycle().value

                if (notification != null) {
                    DetailsScreen(
                        viewModel = viewModel,
                        notification = notification!!,
                        onReturnClick = { navController.popBackStack() }
                    )
                } else {
                    // Simple loading indicator for Desktop
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }

            composable<AppRoute.LoginRoute> {
                val viewModel = remember { LoginViewModel() }

                LaunchedEffect(viewModel.loginSuccessful) {
                    if (viewModel.loginSuccessful) {
                        currentUser = viewModel.loggedInUser ?: return@LaunchedEffect
                        navController.navigate(AppRoute.MainRoute) {
                            popUpTo(AppRoute.LoginRoute) { inclusive = true }
                        }
                        viewModel.resetLoginState()
                    }
                }

                LoginScreen(
                    viewModel = viewModel,
                    onNavigateToRegisterScreen = {
                        navController.navigate(AppRoute.RegisterRoute)
                    }
                )
            }

            composable<AppRoute.RegisterRoute> {
                val viewModel = remember { RegisterViewModel() }

                // Observe registration success
                LaunchedEffect(viewModel.registrationSuccessful) {
                    if (viewModel.registrationSuccessful) {
                        navController.navigate(AppRoute.LoginRoute) {
                            popUpTo(AppRoute.RegisterRoute) { inclusive = true }
                        }
                        viewModel.resetRegistrationState()
                    }
                }

                RegisterScreen(
                    viewModel = viewModel,
                    onRegisterClick = { username, email, password ->
                        println("Registering user: $username, $email, $password")
                        viewModel.registerUser(username, email, password)
                    },
                    onNavigateToLoginScreen = {
                        navController.navigate(AppRoute.LoginRoute)
                    }
                )
            }
        }
    }
}