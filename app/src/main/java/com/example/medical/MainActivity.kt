package com.example.medical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.medical.presentation.theme.MedicalAppTheme
import com.example.medical.presentation.ui.auth.LoginRoute
import com.example.medical.presentation.ui.auth.RegisterRoute
import com.example.medical.presentation.ui.intro.IntroScreen
import com.example.medical.presentation.ui.welcome.WelcomeScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedicalAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        val navController = rememberNavController()

                        NavHost(navController = navController, startDestination = "welcome") {
                            composable("welcome") {
                                WelcomeScreen(
                                    onRoleSelected = { isDoctor ->
                                        navController.navigate("login/$isDoctor")
                                    }
                                )
                            }
                            composable(
                                "login/{isDoctor}",
                                arguments = listOf(navArgument("isDoctor") { type = NavType.BoolType })
                            ) { backStackEntry ->
                                val isDoctor = backStackEntry.arguments?.getBoolean("isDoctor") ?: false
                                LoginRoute(
                                    isDoctor = isDoctor,
                                    onLoginSuccess = {
                                        if (isDoctor) {
                                            navController.navigate("doctor_home") {
                                                popUpTo("welcome") { inclusive = true }
                                            }
                                        } else {
                                            navController.navigate("intro") {
                                                popUpTo("welcome") { inclusive = true }
                                            }
                                        }
                                    },
                                    onRegisterClick = {
                                        navController.navigate("register/$isDoctor")
                                    }
                                )
                            }
                            composable(
                                "register/{isDoctor}",
                                arguments = listOf(navArgument("isDoctor") { type = NavType.BoolType })
                            ) { backStackEntry ->
                                val isDoctor = backStackEntry.arguments?.getBoolean("isDoctor") ?: false
                                RegisterRoute(
                                    isDoctor = isDoctor,
                                    onBackClick = { navController.popBackStack() },
                                    onLoginClick = { navController.popBackStack() },
                                    onRegisterSuccess = { navController.popBackStack() }
                                )
                            }
                            composable("intro") {
                                IntroScreen(
                                    onFinishIntro = {
                                        navController.navigate("patient_home") {
                                            popUpTo("intro") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("patient_home") {
                                com.example.medical.presentation.ui.patient.patient_home.PatientHomeRoute(
                                    onNavigateToDoctorList = { type, specialty ->
                                        val route = if (specialty != null) "doctor_list/$type?specialty=$specialty" else "doctor_list/$type"
                                        navController.navigate(route)
                                    },
                                    onNavigateToAppointmentDetail = { appointmentId ->
                                        navController.navigate("appointment_detail/$appointmentId")
                                    }
                                )
                            }
                            composable("doctor_home") {
                                com.example.medical.presentation.ui.doctor.navigation.DoctorMainScreen(
                                    onLogout = {
                                        navController.navigate("welcome") {
                                            popUpTo(0) // Xóa toàn bộ stack để về màn welcome an toàn
                                        }
                                    },
                                    onNavigateToAppointmentDetail = { appointmentId ->
                                        navController.navigate("doctor_appointment_detail/$appointmentId")
                                    }
                                )
                            }
                            composable(
                                "doctor_list/{type}?specialty={specialty}",
                                arguments = listOf(
                                    navArgument("type") { type = NavType.StringType },
                                    navArgument("specialty") { type = NavType.StringType; nullable = true }
                                )
                            ) { backStackEntry ->
                                com.example.medical.presentation.ui.patient.doctor_list.DoctorListRoute(
                                    onNavigateBack = { navController.popBackStack() },
                                    onNavigateToBooking = { doctorId ->
                                        navController.navigate("booking/$doctorId")
                                    }
                                )
                            }
                            composable(
                                "booking/{doctorId}",
                                arguments = listOf(navArgument("doctorId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                com.example.medical.presentation.ui.patient.booking.BookingRoute(
                                    onNavigateBack = { navController.popBackStack() },
                                    onNavigateToNext = { doctorId, date, time ->
                                        navController.navigate("booking_success/$doctorId/$date/$time") {
                                            popUpTo("patient_home") // Trở về patient_home sau khi success
                                        }
                                    }
                                )
                            }
                            composable(
                                "booking_success/{doctorId}/{date}/{time}",
                                arguments = listOf(
                                    navArgument("doctorId") { type = NavType.StringType },
                                    navArgument("date") { type = NavType.StringType },
                                    navArgument("time") { type = NavType.StringType }
                                )
                            ) {
                                com.example.medical.presentation.ui.patient.booking_success.BookingSuccessRoute(
                                    onNavigateBack = { navController.popBackStack() },
                                    onNavigateHome = {
                                        navController.navigate("patient_home") {
                                            popUpTo("patient_home") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable(
                                "appointment_detail/{appointmentId}",
                                arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                com.example.medical.presentation.ui.patient.appointment_detail.AppointmentDetailRoute(
                                    appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: "",
                                    onNavigateBack = { navController.popBackStack() },
                                    onNavigateToChangeDoctor = { /* TODO */ },
                                    onNavigateToReschedule = { /* TODO */ }
                                )
                            }
                            composable(
                                "doctor_appointment_detail/{appointmentId}",
                                arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                com.example.medical.presentation.ui.doctor.appointment_detail.DoctorAppointmentDetailRoute(
                                    appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: "",
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

