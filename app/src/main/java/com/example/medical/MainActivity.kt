package com.example.medical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.medical.presentation.theme.MedicalAppTheme
import com.example.medical.presentation.ui.auth.LoginRoute
import com.example.medical.presentation.ui.doctor.home.DoctorHomeRoute
import com.example.medical.presentation.ui.doctor.appointment.DoctorAppointmentRoute
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedicalAppTheme {
                var currentScreen by remember { mutableStateOf("login") }
                
                when (currentScreen) {
                    "login" -> {
                        LoginRoute(
                            onLoginSuccess = { currentScreen = "doctor_home" }
                        )
                    }
                    "doctor_home" -> {
                        DoctorHomeRoute(
                            onNavigateToAppointments = { currentScreen = "doctor_appointments" }
                        )
                    }
                    "doctor_appointments" -> {
                        DoctorAppointmentRoute(
                            onNavigateToHome = { currentScreen = "doctor_home" }
                        )
                    }
                }
            }
        }
    }
}

