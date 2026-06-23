package com.example.medical.presentation.ui.doctor.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.medical.R

sealed class BottomNavItem(val route: String, val icon: ImageVector, val titleResId: Int) {
    object Home : BottomNavItem("doctor_home", Icons.Default.Home, R.string.nav_home)
    object Appointments : BottomNavItem("doctor_appointments", Icons.Default.CalendarToday, R.string.nav_appointments)
    object Notifications : BottomNavItem("doctor_notifications", Icons.Default.NotificationsNone, R.string.nav_notifications)
    object Profile : BottomNavItem("doctor_profile", Icons.Default.Person, R.string.nav_profile)
}
