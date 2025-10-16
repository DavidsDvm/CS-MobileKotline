package com.test.tadia

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.test.tadia.R

enum class BottomTab { Home, Calendar, Inbox, Favorites, Profile }

@Composable
fun AppBottomBar(
    selected: BottomTab,
    onSelected: (BottomTab) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selected == BottomTab.Home,
            onClick = { onSelected(BottomTab.Home) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = selected == BottomTab.Calendar,
            onClick = { onSelected(BottomTab.Calendar) },
            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Calendario") },
            label = { Text("Calendario") }
        )
        NavigationBarItem(
            selected = selected == BottomTab.Inbox,
            onClick = { onSelected(BottomTab.Inbox) },
            icon = { Icon(Icons.Default.Email, contentDescription = "Mensajes") },
            label = { Text("Mensajes") }
        )
        NavigationBarItem(
            selected = selected == BottomTab.Favorites,
            onClick = { onSelected(BottomTab.Favorites) },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
            label = { Text("Favoritos") }
        )
        NavigationBarItem(
            selected = selected == BottomTab.Profile,
            onClick = { onSelected(BottomTab.Profile) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}
