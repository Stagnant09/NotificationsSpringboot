package my.app.notifications.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.app.notifications.ui.screens.login.GitHubText

@Composable
fun SearchBar(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    onTrailingButtonClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(320.dp)
            .height(46.dp)
            .clip(CircleShape)
            .background(Color(42,42,42,255))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = Color(238,238,238,255)
            )

            Spacer(Modifier.width(8.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.9f),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = "Search",
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            color = Color(238,238,238,255)
                        )
                    }
                    innerTextField()
                },
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    color = Color(238,238,238,255)
                ),
                cursorBrush = SolidColor(GitHubText)
            )

            IconButton(onClick = onTrailingButtonClick){
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = Color(238,238,238,255)
                )
            }
        }
    }
}