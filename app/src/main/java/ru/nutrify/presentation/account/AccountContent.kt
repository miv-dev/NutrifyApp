package ru.nutrify.presentation.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountContent() {
    val theme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Account")
                }
            )
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ListItem(
                leadingContent = {
                    Box(
                        Modifier
                            .size(70.dp)
                            .border(2.dp, theme.outline, CircleShape)
                    ) {
                        Box(
                            Modifier
                                .padding(8.dp)
                                .size(60.dp)
                                .background(theme.outlineVariant, CircleShape)
                                .align(Alignment.Center)
                        ) {
                            Icon(
                                Icons.Filled.CameraAlt,
                                contentDescription = "avatar",
                                Modifier.align(
                                    Alignment.Center
                                )
                            )
                        }
                    }
                },
                headlineContent = {
                    Text("John Doe")
                },
                supportingContent = {
                    Text("john@doe.ru")
                },
                trailingContent = {
                    FilledTonalButton({}) {
                        Text("Выйти")
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = "Выйти")
                    }
                }
            )

            SettingCard {
                SettingItem(
                    icon = Icons.Outlined.Person,
                    text = "Профиль",
                    onClick = {}
                )
                SettingItem(
                    icon = Icons.Outlined.TrackChanges,
                    text = "Цель",
                    onClick = {}
                )
            }

            SettingCard("Настройки", inDev = true) {
                SettingItem(
                    icon = Icons.Outlined.DarkMode,
                    text = "Темная Тема",
                    onClick = {},
                )
                SettingItem(
                    icon = Icons.Outlined.CalendarToday,
                    text = "Напоминания",
                    onClick = {},
                )
                SettingItem(
                    icon = Icons.Outlined.Notifications,
                    text = "Уведомления",
                    onClick = {},
                )

            }

        }
    }
}

@Composable
fun SettingItem(text: String, onClick: () -> Unit, icon: ImageVector, inDev: Boolean = false) {
    val theme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = text,
            tint = theme.outline,
        )
        Spacer(Modifier.width(8.dp))
        Text(text.uppercase(), style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp))
        Spacer(Modifier.weight(1f))
        if (inDev) {
            Text(
                "Скоро",
                modifier = Modifier
                    .background(theme.tertiaryContainer, CircleShape)
                    .padding(vertical = 2.dp, horizontal = 8.dp),
                color = theme.onTertiaryContainer
            )
        } else {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "avatar",
                tint = theme.outline,

                )
        }
    }
}

@Composable
fun SettingCard(
    title: String? = null,
    inDev: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val theme = MaterialTheme.colorScheme
    ElevatedCard(
        Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(1.dp)
    ) {
        Column(
            Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row {
                if (!title.isNullOrEmpty()) {
                    Text(
                        title.uppercase(),
                        Modifier.padding(start = 8.dp, top = 8.dp),
                        style = TextStyle(color = theme.outline, fontWeight = FontWeight.Medium)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                if (inDev) {
                    Text(
                        "В разработке",
                        modifier = Modifier
                            .background(theme.tertiaryContainer, CircleShape)
                            .padding(vertical = 2.dp, horizontal = 8.dp),
                        color = theme.onTertiaryContainer
                    )
                }
            }
            content()
        }
    }
}