package com.example.medical.presentation.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class ToastType {
    SUCCESS, ERROR, INFO, WARNING
}

data class ToastData(
    val message: String,
    val type: ToastType = ToastType.INFO
)

@Composable
fun MedicalToast(
    toastData: ToastData?,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = toastData != null,
        enter = slideInVertically(initialOffsetY = { -it }), // Slide from top
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        toastData?.let { data ->
            val backgroundColor = when (data.type) {
                ToastType.SUCCESS -> Color(0xFF4CAF50) // Green
                ToastType.ERROR -> Color(0xFFF44336)   // Red
                ToastType.WARNING -> Color(0xFFFF9800) // Orange
                ToastType.INFO -> MaterialTheme.colorScheme.primary
            }

            val icon: ImageVector = when (data.type) {
                ToastType.SUCCESS -> Icons.Default.CheckCircle
                ToastType.ERROR -> Icons.Default.Error
                ToastType.WARNING -> Icons.Default.Warning
                ToastType.INFO -> Icons.Default.Info
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 32.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Toast Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = data.message,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
