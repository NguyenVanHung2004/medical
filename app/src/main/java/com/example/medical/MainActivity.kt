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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedicalAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        var currentScreen by remember { mutableStateOf("welcome") }
                        var isDoctorRole by remember { mutableStateOf(false) }

                        when (currentScreen) {
                            "welcome" -> {
                                WelcomeScreen(
                                    onRoleSelected = { isDoctor ->
                                        isDoctorRole = isDoctor
                                        currentScreen = "login"
                                    }
                                )
                            }
                            "login" -> {
                                LoginRoute(
                                    isDoctor = isDoctorRole,
                                    onLoginSuccess = {
                                        if (isDoctorRole) {
                                            currentScreen = "doctor_home"
                                        } else {
                                            currentScreen = "intro"
                                        }
                                    },
                                    onRegisterClick = {
                                        currentScreen = "register"
                                    }
                                )
                            }
                            "register" -> {
                                RegisterRoute(
                                    isDoctor = isDoctorRole,
                                    onBackClick = { currentScreen = "login" },
                                    onLoginClick = { currentScreen = "login" },
                                    onRegisterSuccess = {
                                        currentScreen = "login"
                                    }
                                )
                            }
                            "intro" -> {
                                IntroScreen(
                                    onFinishIntro = {
                                        currentScreen = "patient_home"
                                    }
                                )
                            }
                            "patient_home" -> {
                                AlertDialog(
                                    onDismissRequest = { },
                                    title = { Text("Home Screen") },
                                    text = { Text("Bắt đầu vào homeScreen Bệnh nhân") },
                                    confirmButton = {
                                        TextButton(onClick = { /* TODO */ }) { Text("OK") }
                                    }
                                )
                            }
                            "doctor_home" -> {
                                AlertDialog(
                                    onDismissRequest = { },
                                    title = { Text("Home Screen") },
                                    text = { Text("Bắt đầu vào homeScreen Bác sĩ") },
                                    confirmButton = {
                                        TextButton(onClick = { /* TODO */ }) { Text("OK") }
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

