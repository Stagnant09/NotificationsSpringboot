package my.app.notifications.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import my.app.notifications.ui.components.CustomTextField
import my.app.notifications.ui.components.InputField
import my.app.notifications.ui.utils.VSpacer
import org.jetbrains.compose.ui.tooling.preview.Preview

// GitHub-inspired color palette
val GitHubBackground = Color(0xFF0D1117)
val GitHubCardBackground = Color(0xFF161B22)
val GitHubBorder = Color(0xFF30363D)
val GitHubInputBackground = Color(0xFF0D1117)
val GitHubPrimary = Color(0xFF238636)
val GitHubPrimaryHover = Color(0xFF2EA043)
val GitHubText = Color(0xFFC9D1D9)
val GitHubTextSecondary = Color(0xFF8B949E)
val GitHubWhite = Color(0xFFFFFFFF)
val GitHubError = Color(255,0,0,255)

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToRegisterScreen: () -> Unit = {}
) {
    var valueUsername by remember { mutableStateOf("") }
    var valuePassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GitHubBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo/Title section
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Logo",
                modifier = Modifier.size(48.dp),
                tint = GitHubText
            )

            VSpacer(24)

            // Login card
            Box(
                modifier = Modifier
                    .width(340.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(6.dp),
                        ambientColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(
                        color = GitHubCardBackground,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = GitHubBorder,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Title
                    Text(
                        text = "Sign in to your account",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light,
                        color = GitHubText,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Username field
                    InputField(
                        label = "Username or email address",
                        value = valueUsername,
                        onValueChange = { valueUsername = it },
                        placeholder = "",
                        icon = Icons.Default.Person
                    )

                    VSpacer(16)

                    // Password field with forgot password link
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Password",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = GitHubText,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Forgot password?",
                                fontSize = 12.sp,
                                color = Color(0xFF58A6FF),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        CustomTextField(
                            value = valuePassword,
                            onValueChange = { valuePassword = it },
                            placeholder = "",
                            isPassword = true
                        )
                    }

                    // Show error message if any
                    viewModel.errorMessage?.let { error ->
                        VSpacer(12)
                        Text(
                            text = error,
                            fontSize = 12.sp,
                            color = GitHubError,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }

                    VSpacer(20)

                    // Sign in button
                    Button(
                        onClick = {
                            if (!viewModel.isLoading &&
                                valueUsername.isNotEmpty() &&
                                valuePassword.isNotEmpty()) {
                                viewModel.loginUser(valueUsername, valuePassword)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GitHubPrimary,
                            contentColor = GitHubWhite
                        ),
                        shape = RoundedCornerShape(6.dp),
                        enabled = !viewModel.isLoading &&
                                valueUsername.isNotEmpty() &&
                                valuePassword.isNotEmpty()
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = GitHubWhite,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Sign in",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            VSpacer(16)

            // Sign up section
            Box(
                modifier = Modifier
                    .width(340.dp)
                    .background(
                        color = GitHubCardBackground,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = GitHubBorder,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(16.dp)
                    .clickable { onNavigateToRegisterScreen() },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "New to this app? ",
                        fontSize = 14.sp,
                        color = GitHubText
                    )
                    Text(
                        text = "Create an account",
                        fontSize = 14.sp,
                        color = Color(0xFF58A6FF)
                    )
                }
            }
        }
    }
}

@Suppress("ViewModelConstructorInComposable")
@Preview(heightDp = 600, widthDp = 800, showBackground = true)
@Composable
fun LoginScreenPreview() {
    val viewModel = LoginViewModel()
    LoginScreen(
        viewModel = viewModel,
        onNavigateToRegisterScreen = {}
    )
}