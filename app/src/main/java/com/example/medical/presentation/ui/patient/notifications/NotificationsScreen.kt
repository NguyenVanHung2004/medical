package com.example.medical.presentation.ui.patient.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medical.R
import com.example.medical.domain.model.Notification
import com.example.medical.domain.model.NotificationType
import org.koin.androidx.compose.koinViewModel

@Composable
fun NotificationsRoute(
    viewModel: NotificationsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    NotificationsScreen(uiState = uiState)
}

@Composable
fun NotificationsScreen(uiState: NotificationsUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgLight))
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = colorResource(id = R.color.primaryBlue),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.notifications_screen_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.primaryBlue)
            )
        }

        if (uiState.notifications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.no_notifications),
                    color = colorResource(id = R.color.textSecondary),
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.notifications) { notification ->
                    NotificationCard(notification)
                }
            }
        }
    }
}

@Composable
fun NotificationCard(notification: Notification) {
    val icon: ImageVector
    val iconTint: Color
    val iconBgColor: Color

    when (notification.type) {
        NotificationType.REMINDER -> {
            icon = Icons.Default.Schedule
            iconTint = colorResource(id = R.color.primaryBlue)
            iconBgColor = colorResource(id = R.color.primaryBlueLight)
        }
        NotificationType.UPDATE -> {
            icon = Icons.Default.Sync
            iconTint = colorResource(id = R.color.warningOrange) // Assuming we need an orange/yellow
            iconBgColor = Color(0xFFFFF3E0)
        }
        NotificationType.SYSTEM -> {
            icon = Icons.Default.Info
            iconTint = colorResource(id = R.color.successGreen) // Assuming green
            iconBgColor = Color(0xFFE8F5E9)
        }

        NotificationType.NEW_APPOINTMENT_REQUEST -> TODO()
        NotificationType.APPOINTMENT_CANCELLED -> TODO()
        NotificationType.UPCOMING_APPOINTMENT -> TODO()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isUnread) Color.White else colorResource(id = R.color.bgLight)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (notification.isUnread) 2.dp else 0.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    fontSize = 16.sp,
                    fontWeight = if (notification.isUnread) FontWeight.Bold else FontWeight.Medium,
                    color = colorResource(id = R.color.textPrimary)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.textSecondary),
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = notification.timeAgo,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.textSecondary)
                )
            }
            
            if (notification.isUnread) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(colorResource(id = R.color.primaryBlue))
                )
            }
        }
    }
}
