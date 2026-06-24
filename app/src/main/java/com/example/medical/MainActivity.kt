package com.example.medical

import android.annotation.SuppressLint
import android.os.Bundle
import android.content.Context
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
import android.content.res.Configuration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
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
import com.example.medical.presentation.ui.settings.SettingsScreen

class MainActivity : ComponentActivity() {
    @SuppressLint("LocalContextConfigurationRead")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            var selectedTheme by remember {
                mutableStateOf(sharedPreferences.getString("theme", "standard") ?: "standard")
            }
            var selectedLanguage by remember {
                mutableStateOf(sharedPreferences.getString("language", "vi") ?: "vi")
            }

            val context = LocalContext.current
            val locale = Locale(selectedLanguage)
            val configuration = remember(selectedLanguage) {
                Configuration(context.resources.configuration).apply {
                    setLocale(locale)
                    setLayoutDirection(locale)
                }
            }
            val localeContext = remember(selectedLanguage) {
                context.createConfigurationContext(configuration)
            }

            CompositionLocalProvider(
                LocalContext provides localeContext,
                LocalConfiguration provides configuration
            ) {
                MedicalAppTheme(themeName = selectedTheme) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Box(
                            modifier = Modifier.padding(innerPadding)
                                .consumeWindowInsets(innerPadding)
                        ) {
                            val navController = rememberNavController()

                            NavHost(navController = navController, startDestination = "welcome") {
                                composable("welcome") {
                                    WelcomeScreen(
                                        currentLanguage = selectedLanguage,
                                        onLanguageChange = { newLang ->
                                            selectedLanguage = newLang
                                            sharedPreferences.edit().putString("language", newLang).apply()
                                        },
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
                                                navController.navigate("complete_doctor_profile") {
                                                    popUpTo("register/$isDoctor") {
                                                        inclusive = true
                                                    }
                                                }
                                            } else {
                                                navController.navigate("complete_profile") {
                                                    popUpTo("register/$isDoctor") {
                                                        inclusive = true
                                                    }
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
                                composable("complete_doctor_profile") {
                                    com.example.medical.presentation.ui.doctor.complete_profile.CompleteDoctorProfileRoute(
                                        onNavigateBack = { navController.popBackStack() },
                                        onNavigateNext = {
                                            navController.navigate("doctor_home") {
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
                                        },
                                        onLogout = {
                                            navController.navigate("welcome") {
                                                popUpTo(0)
                                            }
                                        },
                                        onNavigateToSettings = {
                                            navController.navigate("settings")
                                        }
                                    )
                                }
                                composable("doctor_home") {
                                    DoctorMainScreen(
                                        onLogout = {
                                            navController.navigate("welcome") {
                                                popUpTo(0) // Xóa toàn bộ stack để về màn welcome an toàn
                                            }
                                        },
                                        onNavigateToAppointmentDetail = { appointmentId ->
                                            navController.navigate("doctor_appointment_detail/$appointmentId")
                                        },
                                        onNavigateToSettings = {
                                            navController.navigate("settings")
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
                                    com.example.medical.presentation.ui.patient.doctor_list.DoctorListRoute(
                                        onNavigateBack = { navController.popBackStack() },
                                        onNavigateToBooking = { doctorId ->
                                            val type = backStackEntry.arguments?.getString("type")
                                                ?: "offline"
                                            navController.navigate("booking/$doctorId/$type")
                                        }
                                    )
                                }
                                composable(
                                    "booking/{doctorId}/{type}",
                                    arguments = listOf(
                                        navArgument("doctorId") { type = NavType.StringType },
                                        navArgument("type") { type = NavType.StringType }
                                    )
                                ) { backStackEntry ->
                                    com.example.medical.presentation.ui.patient.booking.BookingRoute(
                                        onNavigateBack = { navController.popBackStack() },
                                        onNavigateToNext = { doctorId, date, time ->
                                            val type = backStackEntry.arguments?.getString("type")
                                                ?: "offline"
                                            navController.navigate("booking_success/$doctorId/$date/$time/$type") {
                                                popUpTo("patient_home") // Trở về patient_home sau khi success
                                            }
                                        }
                                    )
                                }
                                composable(
                                    "booking_success/{doctorId}/{date}/{time}/{type}",
                                    arguments = listOf(
                                        navArgument("doctorId") { type = NavType.StringType },
                                        navArgument("date") { type = NavType.StringType },
                                        navArgument("time") { type = NavType.StringType },
                                        navArgument("type") { type = NavType.StringType }
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
                                            navController.navigate("booking/$doctorId/offline")
                                        }
                                    )
                                }
                                composable("doctor_appointment_detail/{appointmentId}") { backStackEntry ->
                                    com.example.medical.presentation.ui.doctor.appointment_detail.DoctorAppointmentDetailRoute(
                                        appointmentId = backStackEntry.arguments?.getString("appointmentId")
                                            ?: "",
                                        onNavigateBack = { navController.popBackStack() }
                                    )
                                }
                                composable("settings") {
                                    SettingsScreen(
                                        currentTheme = selectedTheme,
                                        onThemeChange = { newTheme ->
                                            selectedTheme = newTheme
                                            sharedPreferences.edit().putString("theme", newTheme)
                                                .apply()
                                        },
                                        currentLanguage = selectedLanguage,
                                        onLanguageChange = { newLang ->
                                            selectedLanguage = newLang
                                            sharedPreferences.edit().putString("language", newLang)
                                                .apply()
                                        },
                                        onBackClick = { navController.popBackStack() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

