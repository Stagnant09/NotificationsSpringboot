package my.app.notifications.ui.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.EditAttributes
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import my.app.notifications.models.Notification
import my.app.notifications.ui.components.NotificationCard
import my.app.notifications.ui.components.SearchBar
import my.app.notifications.ui.utils.VSpacer

// GitHub-inspired dark theme colors
private val GitHubBackground = Color(0xFF0D1117)
private val GitHubSurface = Color(0xFF161B22)
private val GitHubBorder = Color(0xFF30363D)
private val GitHubText = Color(0xFFC9D1D9)
private val GitHubTextSecondary = Color(0xFF8B949E)
private val GitHubAccent = Color(0xFF58A6FF)
private val GitHubError = Color(0xFFFF7B72)
private val GitHubSuccess = Color(0xFF3FB950)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNotificationClick: (Notification) -> Unit,
    onLogoutClick: () -> Unit
) {
    val viewModel = remember { MainViewModel() }
    val showSearchBar = remember { mutableStateOf(false) }
    val searchQuery = remember { mutableStateOf("") }
    val showDropdownMenuSort = remember { mutableStateOf(false) }
    val showDropdownMenuSettings = remember { mutableStateOf(false) }

    // Load notifications on first composition
    LaunchedEffect(Unit) {
        viewModel.loadNotifications()
    }

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
                            actionIconContentColor = GitHubText
                        ),
                        title = {
                            Text(
                                "Notifications",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )
                        },
                        actions = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                AnimatedContent(
                                    targetState = showSearchBar.value,
                                    transitionSpec = {
                                        (fadeIn(tween(200)) + expandHorizontally(
                                            expandFrom = Alignment.Start
                                        )).togetherWith(
                                            fadeOut(tween(150)) + shrinkHorizontally(
                                                shrinkTowards = Alignment.Start
                                            )
                                        )
                                    }
                                ) { visible ->
                                    if (visible) {
                                        SearchBar(
                                            value = searchQuery.value,
                                            onValueChange = {
                                                searchQuery.value = it
                                                if (searchQuery.value.length > 1) viewModel.sortAndSearch(searchQuery.value)
                                            },
                                            onTrailingButtonClick = {
                                                showSearchBar.value = false
                                                searchQuery.value = ""
                                                viewModel.sortAndSearch(searchQuery.value)
                                            }
                                        )
                                    } else {
                                        IconButton(
                                            onClick = { showSearchBar.value = true }
                                        ) {
                                            Icon(
                                                Icons.Filled.Search,
                                                contentDescription = "Search",
                                                tint = GitHubTextSecondary
                                            )
                                        }
                                    }
                                }

                                IconButton(onClick = { viewModel.loadNotifications() }) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = "Reload",
                                        tint = GitHubTextSecondary
                                    )
                                }

                                // Sort button with dropdown
                                Box {
                                    IconButton(onClick = { showDropdownMenuSort.value = !showDropdownMenuSort.value }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Sort,
                                            contentDescription = "Sort",
                                            tint = GitHubTextSecondary
                                        )
                                    }

                                    MaterialTheme(
                                        colorScheme = darkColorScheme(
                                            surface = GitHubSurface,
                                            onSurface = GitHubText,
                                            surfaceVariant = GitHubBorder
                                        )
                                    ) {
                                        DropdownMenu(
                                            expanded = showDropdownMenuSort.value,
                                            onDismissRequest = { showDropdownMenuSort.value = false },
                                            offset = DpOffset(0.dp, 4.dp),
                                            modifier = Modifier.background(GitHubSurface)
                                        ) {
                                            DropdownMenuItem(
                                                text = { Text("Date", color = GitHubText) },
                                                onClick = {
                                                    viewModel.sortAndSearch(searchQuery.value, "Date")
                                                    showDropdownMenuSort.value = false
                                                }
                                            )
                                            DropdownMenuItem(
                                                text = { Text("Priority", color = GitHubText) },
                                                onClick = {
                                                    viewModel.sortAndSearch(searchQuery.value, "Priority")
                                                    showDropdownMenuSort.value = false
                                                }
                                            )
                                            DropdownMenuItem(
                                                text = { Text("Status", color = GitHubText) },
                                                onClick = {
                                                    viewModel.sortAndSearch(searchQuery.value, "Status")
                                                    showDropdownMenuSort.value = false
                                                }
                                            )
                                        }
                                    }
                                }

                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = Icons.Filled.FilterAlt,
                                        contentDescription = "Filter",
                                        tint = GitHubTextSecondary
                                    )
                                }

                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = Icons.Filled.Create,
                                        contentDescription = "Create",
                                        tint = GitHubTextSecondary
                                    )
                                }

                                Box {
                                    IconButton(onClick = { showDropdownMenuSettings.value = !showDropdownMenuSettings.value }) {
                                        Icon(
                                            imageVector = Icons.Filled.Settings,
                                            contentDescription = "Settings",
                                            tint = GitHubTextSecondary
                                        )
                                    }

                                    MaterialTheme(
                                        colorScheme = darkColorScheme(
                                            surface = GitHubSurface,
                                            onSurface = GitHubText,
                                            surfaceVariant = GitHubBorder
                                        )
                                    ) {
                                        DropdownMenu(
                                            expanded = showDropdownMenuSettings.value,
                                            onDismissRequest = { showDropdownMenuSettings.value = false },
                                            offset = DpOffset(0.dp, 4.dp),
                                            modifier = Modifier.background(GitHubSurface)
                                        ) {
                                            DropdownMenuItem(
                                                leadingIcon = {
                                                    Icon(
                                                        imageVector = Icons.Filled.Person,
                                                        contentDescription = "Profile",
                                                        tint = GitHubTextSecondary
                                                    )
                                                },
                                                text = { Text("Profile", color = GitHubText) },
                                                onClick = {
                                                    showDropdownMenuSettings.value = false
                                                }
                                            )
                                            DropdownMenuItem(
                                                leadingIcon = {
                                                    Icon(
                                                        imageVector = Icons.Filled.EditAttributes,
                                                        contentDescription = "Options",
                                                        tint = GitHubTextSecondary
                                                    )
                                                },
                                                text = { Text("Options", color = GitHubText) },
                                                onClick = {
                                                    showDropdownMenuSettings.value = false
                                                }
                                            )
                                            DropdownMenuItem(
                                                leadingIcon = {
                                                    Icon(
                                                        imageVector = Icons.AutoMirrored.Filled.Logout,
                                                        contentDescription = "Logout",
                                                        tint = GitHubTextSecondary
                                                    )
                                                },
                                                text = { Text("Logout", color = GitHubText) },
                                                onClick = {
                                                    showDropdownMenuSettings.value = false
                                                    onLogoutClick()
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                when {
                    viewModel.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = GitHubAccent
                            )
                        }
                    }

                    viewModel.errorMessage != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = viewModel.errorMessage ?: "Unknown error",
                                    color = GitHubError,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.loadNotifications() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = GitHubAccent,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Retry")
                                }
                            }
                        }
                    }

                    viewModel.displayedNotifications.value.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null,
                                    tint = GitHubTextSecondary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    "No notifications yet",
                                    color = GitHubTextSecondary,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(viewModel.displayedNotifications.value) { notification ->
                                NotificationCard(notification, onClick = {
                                    println("Clicked notification card")
                                    onNotificationClick(notification)
                                })
                            }
                            // Add bottom padding
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}