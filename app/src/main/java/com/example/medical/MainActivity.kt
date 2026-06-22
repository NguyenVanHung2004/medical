package com.example.medical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.medical.presentation.theme.MedicalAppTheme
import com.example.medical.presentation.ui.auth.LoginRoute

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
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
                val navController = rememberNavController()
                
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginRoute(
                                onLoginSuccess = {
                                    navController.navigate("patient_home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("patient_home") {
                            PatientHomeRoute(
                                onNavigateToDoctorList = { type, specialty ->
                                    val route = if (specialty != null) "doctor_list/$type?specialty=$specialty" else "doctor_list/$type"
                                    navController.navigate(route)
                                }
                            )
                        }
                        composable(
                            "doctor_list/{type}?specialty={specialty}",
                            arguments = listOf(
                                navArgument("type") { type = NavType.StringType },
                                navArgument("specialty") { type = NavType.StringType; nullable = true; defaultValue = null }
                            )
                        ) {
                            DoctorListRoute(
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToBooking = { doctorId ->
                                    navController.navigate("booking/$doctorId")
                                }
                            )
                        }
                        composable(
                            "booking/{doctorId}",
                            arguments = listOf(navArgument("doctorId") { type = NavType.StringType })
                        ) {
                            BookingRoute(
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToNext = { doctorId, date, time ->
                                    navController.navigate("booking_success/$doctorId/$date/$time")
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
                    }
                }
            }
        }
    }
}

