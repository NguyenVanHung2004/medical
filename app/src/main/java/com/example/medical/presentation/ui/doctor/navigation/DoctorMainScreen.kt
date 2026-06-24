package com.example.medical.presentation.ui.doctor.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.medical.presentation.ui.doctor.appointment.DoctorAppointmentRoute
import com.example.medical.presentation.ui.doctor.home.DoctorHomeRoute
import com.example.medical.presentation.ui.doctor.notification.DoctorNotificationRoute
import com.example.medical.presentation.ui.doctor.profile.DoctorProfileRoute
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween

@Composable
fun DoctorMainScreen(
    onLogout: () -> Unit,
    onNavigateToAppointmentDetail: (String) -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: "doctor_home"

            com.example.medical.presentation.ui.common.MedicalBottomNavigation(
                currentRoute = currentRoute,
                role = com.example.medical.presentation.ui.common.UserRole.DOCTOR,
                onTabSelected = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        val items = listOf(
            BottomNavItem.Home,
            BottomNavItem.Appointments,
            BottomNavItem.Notifications,
            BottomNavItem.Profile
        )

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
            enterTransition = {
                val initialIndex = items.indexOfFirst { it.route == initialState.destination.route }
                val targetIndex = items.indexOfFirst { it.route == targetState.destination.route }
                val isForward = targetIndex > initialIndex
                slideInHorizontally(
                    initialOffsetX = { if (isForward) it else -it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                val initialIndex = items.indexOfFirst { it.route == initialState.destination.route }
                val targetIndex = items.indexOfFirst { it.route == targetState.destination.route }
                val isForward = targetIndex > initialIndex
                slideOutHorizontally(
                    targetOffsetX = { if (isForward) -it else it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            composable(BottomNavItem.Home.route) {
                DoctorHomeRoute(
                    onNavigateToAppointments = {
                        navController.navigate(BottomNavItem.Appointments.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToNotifications = {
                        navController.navigate(BottomNavItem.Notifications.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(BottomNavItem.Appointments.route) {
                DoctorAppointmentRoute(
                    onNavigateToAppointmentDetail = onNavigateToAppointmentDetail
                )
            }
            composable(BottomNavItem.Notifications.route) {
                DoctorNotificationRoute(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(BottomNavItem.Profile.route) {
                DoctorProfileRoute(
                    onLogout = onLogout
                )
            }
        }
    }
}

