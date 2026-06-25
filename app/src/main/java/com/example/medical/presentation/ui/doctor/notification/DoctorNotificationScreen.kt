package com.example.medical.presentation.ui.doctor.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medical.R
import com.example.medical.domain.model.AppointmentType
import com.example.medical.domain.model.DoctorNotification
import com.example.medical.domain.model.NotificationType
import androidx.compose.foundation.BorderStroke
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorNotificationRoute(
    viewModel: DoctorNotificationViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    DoctorNotificationScreen(
        uiState = uiState,
        onTabSelected = viewModel::selectTab,
        onMarkAllAsRead = viewModel::markAllAsRead,
        onConfirmAppointment = viewModel::confirmAppointment,
        onRejectAppointment = viewModel::rejectAppointment,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorNotificationScreen(
    uiState: DoctorNotificationUiState,
    onTabSelected: (Int) -> Unit,
    onMarkAllAsRead: () -> Unit,
    onConfirmAppointment: (String) -> Unit,
    onRejectAppointment: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.notifications_title),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onMarkAllAsRead) {
                        Text(
                            text = stringResource(id = R.string.mark_all_read),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TabRow(
                selectedTabIndex = uiState.selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = uiState.selectedTab == 0,
                    onClick = { onTabSelected(0) },
                    text = { 
                        Text(
                            text = stringResource(id = R.string.tab_all),
                            fontWeight = if (uiState.selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (uiState.selectedTab == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ) 
                    }
                )
                Tab(
                    selected = uiState.selectedTab == 1,
                    onClick = { onTabSelected(1) },
                    text = { 
                        Text(
                            text = stringResource(id = R.string.tab_unread),
                            fontWeight = if (uiState.selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (uiState.selectedTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ) 
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val displayList = if (uiState.selectedTab == 0) {
                    uiState.notifications
                } else {
                    uiState.notifications.filter { !it.isRead }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(displayList) { notification ->
                        NotificationItem(
                            notification = notification,
                            onConfirm = { onConfirmAppointment(notification.id) },
                            onReject = { onRejectAppointment(notification.id) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: DoctorNotification,
    onConfirm: () -> Unit,
    onReject: () -> Unit
) {
    val backgroundColor = if (!notification.isRead) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    val borderColor = if (!notification.isRead) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = if (notification.isRead) BorderStroke(1.dp, borderColor) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .run {
                    if (!notification.isRead) {
                        this.border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                        ).padding(start = 3.dp)
                    } else {
                        this
                    }
                }
                .padding(16.dp)
        ) {
            // Icon Background Circle
            val iconBgColor = when (notification.type) {
                NotificationType.NEW_APPOINTMENT_REQUEST -> MaterialTheme.colorScheme.background
                NotificationType.APPOINTMENT_CANCELLED -> Color(0xFFFFEBEE)
                NotificationType.UPCOMING_APPOINTMENT -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                NotificationType.REMINDER -> MaterialTheme.colorScheme.primaryContainer
                NotificationType.UPDATE -> Color(0xFFFFF3E0)
                NotificationType.SYSTEM -> Color(0xFFE8F5E9)
            }
            
            val iconTint = when (notification.type) {
                NotificationType.NEW_APPOINTMENT_REQUEST -> MaterialTheme.colorScheme.primary
                NotificationType.APPOINTMENT_CANCELLED -> Color(0xFFD32F2F)
                NotificationType.UPCOMING_APPOINTMENT -> MaterialTheme.colorScheme.primary
                NotificationType.REMINDER -> MaterialTheme.colorScheme.primary
                NotificationType.UPDATE -> Color(0xFFF57C00)
                NotificationType.SYSTEM -> Color(0xFF388E3C)
            }

            val icon = when (notification.type) {
                NotificationType.NEW_APPOINTMENT_REQUEST -> Icons.Default.EventNote
                NotificationType.APPOINTMENT_CANCELLED -> Icons.Default.Cancel
                NotificationType.UPCOMING_APPOINTMENT -> Icons.Default.Videocam
                NotificationType.REMINDER -> Icons.Default.Schedule
                NotificationType.UPDATE -> Icons.Default.Sync
                NotificationType.SYSTEM -> Icons.Default.Info
            }

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
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    val titleRes = when (notification.type) {
                        NotificationType.NEW_APPOINTMENT_REQUEST -> R.string.noti_title_new_request
                        NotificationType.APPOINTMENT_CANCELLED -> R.string.noti_title_cancelled
                        NotificationType.UPCOMING_APPOINTMENT -> R.string.noti_title_upcoming
                        NotificationType.REMINDER -> R.string.noti_title_reminder
                        NotificationType.UPDATE -> R.string.noti_title_update
                        NotificationType.SYSTEM -> R.string.noti_title_system
                    }
                    Text(
                        text = stringResource(id = titleRes),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = notification.timestamp,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                val bodyStr = when (notification.type) {
                    NotificationType.NEW_APPOINTMENT_REQUEST -> {
                        stringResource(
                            id = R.string.noti_body_new_request,
                            notification.patientName,
                            notification.timeInfo
                        )
                    }
                    NotificationType.APPOINTMENT_CANCELLED -> {
                        stringResource(
                            id = R.string.noti_body_cancelled,
                            notification.patientName,
                            notification.timeInfo
                        )
                    }
                    NotificationType.UPCOMING_APPOINTMENT -> {
                        val typeStr = if (notification.appointmentType == AppointmentType.ONLINE) {
                            stringResource(id = R.string.noti_online)
                        } else {
                            stringResource(id = R.string.noti_offline)
                        }
                        stringResource(
                            id = R.string.noti_body_upcoming,
                            typeStr,
                            notification.patientName,
                            notification.timeInfo
                        )
                    }

                    NotificationType.REMINDER -> notification.timeInfo
                    NotificationType.UPDATE -> notification.timeInfo
                    NotificationType.SYSTEM -> notification.timeInfo
                }

                Text(
                    text = bodyStr,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 20.sp
                )

                if (notification.type == NotificationType.NEW_APPOINTMENT_REQUEST) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onConfirm,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 10.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.confirm),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        OutlinedButton(
                            onClick = onReject,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                        ) {
                            Text(
                                text = stringResource(id = R.string.reject),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

