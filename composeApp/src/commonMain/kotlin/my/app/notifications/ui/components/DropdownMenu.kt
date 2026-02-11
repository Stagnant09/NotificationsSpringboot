package my.app.notifications.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DropdownMenu(
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        options.forEach { option ->
            Row(
                modifier = modifier.clickable(
                    onClick = { onOptionSelected(option) }
                )
            ){
                Text(text = option)
            }
        }
    }
}