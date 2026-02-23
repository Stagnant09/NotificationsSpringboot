package my.app.notifications.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.app.notifications.models.Notification
import my.app.notifications.models.NotificationPriority
import my.app.notifications.ui.utils.VSpacer

// GitHub-inspired dark theme colors for notifications
private val GitHubCardBackground = Color(0xFF161B22)
private val GitHubBorder = Color(0xFF30363D)
private val GitHubText = Color(0xFFC9D1D9)
private val GitHubTextSecondary = Color(0xFF8B949E)

// Priority-specific colors
private val NormalBorder = Color(0xFF30363D)
private val UrgentBorder = Color(0xFFD29922)
private val UrgentBackground = Color(0xFF1C1810)
private val CriticalBorder = Color(0xFFDA3633)
private val CriticalBackground = Color(0xFF1C1011)

@Composable
fun NotificationCard(
    notification: Notification,
    onClick: () -> Unit = {}
) {
    val (backgroundColor, borderColor, accentColor) = when (notification.priority) {
        NotificationPriority.NORMAL -> Triple(
            GitHubCardBackground,
            NormalBorder,
            GitHubTextSecondary
        )
        NotificationPriority.URGENT -> Triple(
            UrgentBackground,
            UrgentBorder,
            Color(0xFFD29922)
        )
        NotificationPriority.CRITICAL -> Triple(
            CriticalBackground,
            CriticalBorder,
            Color(0xFFFF7B72)
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "blink")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Card(
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Priority indicator icon
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp, end = 12.dp)
                ) {
                    when (notification.priority) {
                        NotificationPriority.NORMAL -> {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "Normal",
                                tint = GitHubTextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        NotificationPriority.URGENT -> {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = "Urgent",
                                tint = Color(0xFFD29922),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        NotificationPriority.CRITICAL -> {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                contentDescription = "Critical",
                                tint = Color(0xFFFF7B72),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Title
                    Text(
                        text = notification.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GitHubText,
                        lineHeight = 24.sp
                    )

                    VSpacer(4)

                    // Message
                    Text(
                        text = notification.message,
                        fontSize = 14.sp,
                        color = GitHubTextSecondary,
                        lineHeight = 20.sp
                    )
                }

                // Blinking dot for CRITICAL notifications
                if (notification.priority == NotificationPriority.CRITICAL) {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp, top = 4.dp)
                            .size(8.dp)
                            .graphicsLayer(alpha = alpha)
                            .background(Color(0xFFFF7B72), shape = CircleShape)
                    )
                }
            }
        }
    }
}