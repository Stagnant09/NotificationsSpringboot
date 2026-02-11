package my.app.notifications.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.app.notifications.ui.utils.VSpacer
import org.jetbrains.compose.ui.tooling.preview.Preview

// GitHub-inspired color palette
private val GitHubBackground = Color(0xFF0D1117)
private val GitHubCardBackground = Color(0xFF161B22)
private val GitHubBorder = Color(0xFF30363D)
private val GitHubInputBackground = Color(0xFF0D1117)
private val GitHubPrimary = Color(0xFF238636)
private val GitHubText = Color(0xFFC9D1D9)
private val GitHubTextSecondary = Color(0xFF8B949E)
private val GitHubWhite = Color(0xFFFFFFFF)
private val GitHubError = Color(0xFFFF7B72)

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterClick: (String, String, String) -> Unit = { _, _, _ -> },
    onNavigateToLoginScreen: () -> Unit = {}
) {
    var valueUsername by remember { mutableStateOf("") }
    var valueEmail by remember { mutableStateOf("") }
    var valuePassword by remember { mutableStateOf("") }
    var valueConfirmPassword by remember { mutableStateOf("") }
    var passwordsMatch by remember { mutableStateOf(true) }

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
                imageVector = Icons.Default.PersonAdd,
                contentDescription = "Register",
                modifier = Modifier.size(48.dp),
                tint = GitHubText
            )

            VSpacer(24)

            // Register card
            Box(
                modifier = Modifier
                    .width(380.dp)
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
                        text = "Create your account",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light,
                        color = GitHubText,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Join us to get started",
                        fontSize = 14.sp,
                        color = GitHubTextSecondary,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Username field
                    InputField(
                        label = "Username",
                        value = valueUsername,
                        onValueChange = { valueUsername = it },
                        placeholder = "Choose a username"
                    )

                    VSpacer(16)

                    // Email field
                    InputField(
                        label = "Email address",
                        value = valueEmail,
                        onValueChange = { valueEmail = it },
                        placeholder = "you@example.com"
                    )

                    VSpacer(16)

                    // Password field
                    InputField(
                        label = "Password",
                        value = valuePassword,
                        onValueChange = {
                            valuePassword = it
                            passwordsMatch = valueConfirmPassword.isEmpty() ||
                                    valuePassword == valueConfirmPassword
                        },
                        placeholder = "Create a password",
                        isPassword = true
                    )

                    VSpacer(16)

                    // Confirm password field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        InputField(
                            label = "Confirm password",
                            value = valueConfirmPassword,
                            onValueChange = {
                                valueConfirmPassword = it
                                passwordsMatch = valuePassword == valueConfirmPassword
                            },
                            placeholder = "Confirm your password",
                            isPassword = true,
                            isError = !passwordsMatch && valueConfirmPassword.isNotEmpty()
                        )

                        if (!passwordsMatch && valueConfirmPassword.isNotEmpty()) {
                            VSpacer(4)
                            Text(
                                text = "Passwords do not match",
                                fontSize = 12.sp,
                                color = GitHubError,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }

                    VSpacer(8)

                    // Terms text
                    Text(
                        text = "By creating an account, you agree to our Terms of Service and Privacy Policy",
                        fontSize = 12.sp,
                        color = GitHubTextSecondary,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    VSpacer(12)

                    // Register button
                    Button(
                        onClick = {
                            if (passwordsMatch && valuePassword.isNotEmpty()) {
                                onRegisterClick(valueUsername, valueEmail, valuePassword)
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
                        enabled = valueUsername.isNotEmpty() &&
                                valueEmail.isNotEmpty() &&
                                valuePassword.isNotEmpty() &&
                                passwordsMatch
                    ) {
                        Text(
                            text = "Create account",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            VSpacer(16)

            // Sign in section
            Box(
                modifier = Modifier
                    .width(380.dp)
                    .background(
                        color = GitHubCardBackground,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = GitHubBorder,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        fontSize = 14.sp,
                        color = GitHubText
                    )
                    Text(
                        text = "Sign in",
                        fontSize = 14.sp,
                        color = Color(0xFF58A6FF),
                        modifier = Modifier.padding(start = 4.dp).clickable { onNavigateToLoginScreen() }
                    )
                }
            }
        }
    }
}

@Composable
private fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    isError: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = GitHubText,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        CustomTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            isPassword = isPassword,
            isError = isError
        )
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    isError: Boolean = false
) {
    val borderColor = when {
        isError -> GitHubError
        value.isNotEmpty() -> Color(0xFF58A6FF)
        else -> GitHubBorder
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(
                color = GitHubInputBackground,
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 12.dp, vertical = 5.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = GitHubText,
                lineHeight = 20.sp
            ),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontSize = 14.sp,
                        color = GitHubTextSecondary,
                        lineHeight = 20.sp
                    )
                }
                innerTextField()
            },
            cursorBrush = SolidColor(GitHubText)
        )
    }
}

@Preview(heightDp = 700, widthDp = 800, showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val viewModel = RegisterViewModel()
    RegisterScreen(viewModel)
}