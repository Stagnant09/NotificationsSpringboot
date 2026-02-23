package my.app.notifications.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.app.notifications.models.Notification
import my.app.notifications.models.NotificationPriority
import my.app.notifications.ui.utils.VSpacer
import my.app.notifications.utils.convertToDate

// GitHub-inspired dark theme colors
private val GitHubBackground = Color(0xFF0D1117)
private val GitHubSurface = Color(0xFF161B22)
private val GitHubBorder = Color(0xFF30363D)
private val GitHubText = Color(0xFFC9D1D9)
private val GitHubTextSecondary = Color(0xFF8B949E)
private val GitHubSuccess = Color(0xFF3FB950)
private val GitHubDanger = Color(0xFFDA3633)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    notification: Notification,
    onReturnClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GitHubBackground)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Surface(
                    color = GitHubSurface,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = GitHubText,
                            navigationIconContentColor = GitHubText,
                            actionIconContentColor = GitHubTextSecondary
                        ),
                        title = {
                            Text(
                                text = notification.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { onReturnClick() }
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Return")
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    viewModel.markAsRead()
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Check,
                                    "Mark as read",
                                    tint = GitHubSuccess
                                )
                            }
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    "Delete",
                                    tint = GitHubDanger
                                )
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // Main content card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = GitHubSurface,
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
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DetailRow(
                            label = "ID",
                            value = notification.id
                        )

                        VSpacer(16)

                        DetailRow(
                            label = "Time",
                            value = notification.time.toString().convertToDate()
                        )

                        VSpacer(16)

                        DetailRow(
                            label = "Priority",
                            value = notification.priority.name,
                            valueColor = when (notification.priority) {
                                NotificationPriority.NORMAL -> GitHubText
                                NotificationPriority.URGENT -> Color(0xFFD29922)
                                NotificationPriority.CRITICAL -> Color(0xFFFF7B72)
                            }
                        )

                        VSpacer(16)

                        DetailRow(
                            label = "Delivery Status",
                            value = notification.deliveryStatus.name
                        )

                        VSpacer(16)

                        // Message section with different layout
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Message",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = GitHubText,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = GitHubBackground,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = GitHubBorder,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = notification.message,
                                    fontSize = 14.sp,
                                    color = GitHubText,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = GitHubText
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = GitHubTextSecondary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = valueColor,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(2f)
        )
    }
}