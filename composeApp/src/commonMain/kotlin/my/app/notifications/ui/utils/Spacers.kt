package my.app.notifications.ui.utils

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HSpacer(width: Int) = Spacer(modifier = Modifier.width(width.dp))

@Composable
fun VSpacer(height: Int) = Spacer(modifier = Modifier.fillMaxWidth().height(height.dp))