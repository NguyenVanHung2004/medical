package com.example.medical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.consumeWindowInsets
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
import com.example.medical.presentation.ui.auth.forgot_password.ForgotPasswordStep1Route
import com.example.medical.presentation.ui.auth.forgot_password.ForgotPasswordStep2Route
import com.example.medical.presentation.ui.auth.forgot_password.ForgotPasswordStep3Route
import com.example.medical.presentation.ui.patient.complete_profile.CompleteProfileRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.medical.presentation.ui.doctor.navigation.DoctorMainScreen
import com.example.medical.presentation.ui.patient.booking.BookingRoute
import com.example.medical.presentation.ui.patient.booking_success.BookingSuccessRoute
import com.example.medical.presentation.ui.patient.doctor_list.DoctorListRoute
import com.example.medical.presentation.ui.patient.patient_home.PatientHomeRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedicalAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding).consumeWindowInsets(innerPadding)) {
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
                                arguments = listOf(navArgument("isDoctor") {
                                    type = NavType.BoolType
                                })
                            ) { backStackEntry ->
                                val isDoctor =
                                    backStackEntry.arguments?.getBoolean("isDoctor") ?: false
                                LoginRoute(
                                    isDoctor = isDoctor,
                                    onBackClick = { navController.popBackStack() },
                                    onLoginSuccess = {
                                        if (isDoctor) {
                                            navController.navigate("doctor_home") {
                                                popUpTo("welcome") { inclusive = true }
                                            }
                                        } else {
                                            navController.navigate("patient_home") {
                                                popUpTo("welcome") { inclusive = true }
                                            }
                                        }
                                    },
                                    onRegisterClick = {
                                        navController.navigate("register/$isDoctor")
                                    },
                                    onForgotPasswordClick = {
                                        navController.navigate("forgot_password_step1")
                                    }
                                )
                            }
                            composable(
                                "register/{isDoctor}",
                                arguments = listOf(navArgument("isDoctor") {
                                    type = NavType.BoolType
                                })
                            ) { backStackEntry ->
                                val isDoctor =
                                    backStackEntry.arguments?.getBoolean("isDoctor") ?: false
                                RegisterRoute(
                                    isDoctor = isDoctor,
                                    onBackClick = { navController.popBackStack() },
                                    onLoginClick = { navController.popBackStack() },
                                    onRegisterSuccess = { 
                                        if (isDoctor) {
                                            navController.popBackStack() 
                                        } else {
                                            navController.navigate("complete_profile") {
                                                popUpTo("register/$isDoctor") { inclusive = true }
                                            }
                                        }
                                    }
                                )
                            }
                            composable("forgot_password_step1") {
                                ForgotPasswordStep1Route(
                                    onBackClick = { navController.popBackStack() },
                                    onNavigateNext = { navController.navigate("forgot_password_step2") }
                                )
                            }
                            composable("forgot_password_step2") {
                                ForgotPasswordStep2Route(
                                    onBackClick = { navController.popBackStack() },
                                    onNavigateNext = { navController.navigate("forgot_password_step3") }
                                )
                            }
                            composable("forgot_password_step3") {
                                ForgotPasswordStep3Route(
                                    onBackClick = { navController.popBackStack() },
                                    onNavigateSuccess = {
                                        navController.navigate("welcome") {
                                            popUpTo(0)
                                        }
                                    }
                                )
                            }
                            composable("complete_profile") {
                                CompleteProfileRoute(
                                    onNavigateBack = { navController.popBackStack() },
                                    onNavigateNext = {
                                        navController.navigate("patient_home") {
                                            popUpTo("welcome") { inclusive = true }
                                        }
                                    }
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
                                PatientHomeRoute(
                                    onNavigateToDoctorList = { type, specialty ->
                                        val route =
                                            if (specialty != null) "doctor_list/$type?specialty=$specialty" else "doctor_list/$type"
                                        navController.navigate(route)
                                    },
                                    onNavigateToAppointmentDetail = { appointmentId ->
                                        navController.navigate("appointment_detail/$appointmentId")
                                    }
                                )
                            }
                            composable("doctor_home") {
                              DoctorMainScreen(
                                    onLogout = {
                                        navController.navigate("welcome") {
                                            popUpTo(0) // Xóa toàn bộ stack để về màn welcome an toàn
                                        }
                                    }
                                )
                            }
                            composable(
                                "doctor_list/{type}?specialty={specialty}",
                                arguments = listOf(
                                    navArgument("type") { type = NavType.StringType },
                                    navArgument("specialty") {
                                        type = NavType.StringType; nullable = true
                                    }
                                )
                            ) { backStackEntry ->
                                DoctorListRoute(
                                    onNavigateBack = { navController.popBackStack() },
                                    onNavigateToBooking = { doctorId ->
                                        navController.navigate("booking/$doctorId")
                                    }
                                )
                            }
                            composable(
                                "booking/{doctorId}",
                                arguments = listOf(navArgument("doctorId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                BookingRoute(
                                    onNavigateBack = { navController.popBackStack() },
                                    onNavigateToNext = { doctorId, date, time ->
                                        val encodedDate = android.net.Uri.encode(date)
                                        val encodedTime = android.net.Uri.encode(time)
                                        navController.navigate("booking_success/$doctorId/$encodedDate/$encodedTime") {
                                            popUpTo("patient_home")
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
                               BookingSuccessRoute(
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
                                arguments = listOf(navArgument("appointmentId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                com.example.medical.presentation.ui.patient.appointment_detail.AppointmentDetailRoute(
                                    appointmentId = backStackEntry.arguments?.getString("appointmentId")
                                        ?: "",
                                    onNavigateBack = { navController.popBackStack() },
                                    onNavigateToChangeDoctor = {
                                        navController.navigate("doctor_list/all")
                                    },
                                    onNavigateToReschedule = { doctorId ->
                                        navController.navigate("booking/$doctorId")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

