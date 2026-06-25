package com.example.medical.presentation.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.medical.R

enum class UserRole {
    PATIENT, DOCTOR
}

data class MedicalBottomNavItem(
    val route: String,
    val icon: ImageVector,
    val titleResId: Int,
    val allowedRole: UserRole? = null
)

@Composable
fun MedicalBottomNavigation(
    currentRoute: String,
    role: UserRole,
    onTabSelected: (String) -> Unit
) {
    val patientItems = listOf(
        MedicalBottomNavItem("home", Icons.Default.Search, R.string.bottom_nav_search, UserRole.PATIENT),
        MedicalBottomNavItem("appointments", Icons.Default.DateRange, R.string.bottom_nav_appointments, UserRole.PATIENT),
        MedicalBottomNavItem("notifications", Icons.Default.Notifications, R.string.bottom_nav_notifications, UserRole.PATIENT),
        MedicalBottomNavItem("profile", Icons.Default.Person, R.string.bottom_nav_profile, UserRole.PATIENT)
    )

    val doctorItems = listOf(
        MedicalBottomNavItem("doctor_home", Icons.Default.Home, R.string.nav_home, UserRole.DOCTOR),
        MedicalBottomNavItem("doctor_appointments", Icons.Default.CalendarToday, R.string.nav_appointments, UserRole.DOCTOR),
        MedicalBottomNavItem("doctor_notifications", Icons.Default.NotificationsNone, R.string.nav_notifications, UserRole.DOCTOR),
        MedicalBottomNavItem("doctor_profile", Icons.Default.Person, R.string.nav_profile, UserRole.DOCTOR)
    )

    val items = if (role == UserRole.PATIENT) patientItems else doctorItems

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = stringResource(item.titleResId)) },
                label = { Text(stringResource(item.titleResId)) },
                selected = currentRoute == item.route,
                onClick = { onTabSelected(item.route) },
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
