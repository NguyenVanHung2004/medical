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

@Composable
fun DoctorMainScreen(
    onLogout: () -> Unit,
    onNavigateToAppointmentDetail: (String) -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                val items = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Appointments,
                    BottomNavItem.Notifications,
                    BottomNavItem.Profile
                )

                items.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = stringResource(screen.titleResId)) },
                        label = { Text(stringResource(screen.titleResId)) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                    )
                }
            }
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
                androidx.compose.animation.slideInHorizontally(
                    initialOffsetX = { if (isForward) it else -it },
                    animationSpec = androidx.compose.animation.core.tween(300)
                ) + androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(300))
            },
            exitTransition = {
                val initialIndex = items.indexOfFirst { it.route == initialState.destination.route }
                val targetIndex = items.indexOfFirst { it.route == targetState.destination.route }
                val isForward = targetIndex > initialIndex
                androidx.compose.animation.slideOutHorizontally(
                    targetOffsetX = { if (isForward) -it else it },
                    animationSpec = androidx.compose.animation.core.tween(300)
                ) + androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(300))
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
