package com.dsafun.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.dsafun.app.R
import com.dsafun.app.navigation.Screen

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val labelResId: Int
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, Icons.Filled.Home, R.string.nav_home),
    BottomNavItem(Screen.Problems, Icons.Filled.List, R.string.nav_problems),
    BottomNavItem(Screen.Timer, Icons.Filled.Timer, R.string.nav_timer),
    BottomNavItem(Screen.Analytics, Icons.Filled.Info, R.string.nav_analytics),
    BottomNavItem(Screen.Settings, Icons.Filled.Settings, R.string.nav_settings)
)

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.labelResId)
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.labelResId),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = currentRoute == item.screen.route,
                onClick = { onNavigate(item.screen.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

// Made with Bob
