package com.dsafun.app.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dsafun.app.navigation.Screen

@Composable
fun DsaNavigationRail(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    showLabels: Boolean = false,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        NavigationRailItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = if (showLabels) {{ Text("Home") }} else null,
            selected = currentRoute == Screen.Home.route,
            onClick = { onNavigate(Screen.Home.route) },
            colors = NavigationRailItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        NavigationRailItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Problems") },
            label = if (showLabels) {{ Text("Problems") }} else null,
            selected = currentRoute == Screen.Problems.route,
            onClick = { onNavigate(Screen.Problems.route) },
            colors = NavigationRailItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        NavigationRailItem(
            icon = { Icon(Icons.Default.Timer, contentDescription = "Timer") },
            label = if (showLabels) {{ Text("Timer") }} else null,
            selected = currentRoute == Screen.Timer.route,
            onClick = { onNavigate(Screen.Timer.route) },
            colors = NavigationRailItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        NavigationRailItem(
            icon = { Icon(Icons.Default.LocalFireDepartment, contentDescription = "Streak") },
            label = if (showLabels) {{ Text("Streak") }} else null,
            selected = currentRoute == Screen.Streak.route,
            onClick = { onNavigate(Screen.Streak.route) },
            colors = NavigationRailItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        NavigationRailItem(
            icon = { Icon(Icons.Default.BarChart, contentDescription = "Analytics") },
            label = if (showLabels) {{ Text("Analytics") }} else null,
            selected = currentRoute == Screen.Analytics.route,
            onClick = { onNavigate(Screen.Analytics.route) },
            colors = NavigationRailItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        NavigationRailItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = if (showLabels) {{ Text("Settings") }} else null,
            selected = currentRoute == Screen.Settings.route,
            onClick = { onNavigate(Screen.Settings.route) },
            colors = NavigationRailItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

// Made with Bob