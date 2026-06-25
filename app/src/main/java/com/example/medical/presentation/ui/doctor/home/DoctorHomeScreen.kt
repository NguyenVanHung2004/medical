package com.example.medical.presentation.ui.doctor.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medical.R
import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import com.example.medical.domain.model.AppointmentStatus
import com.example.medical.domain.model.AppointmentType
import com.example.medical.presentation.ui.doctor.home.DoctorHomeUIState
import com.example.medical.presentation.ui.doctor.home.DoctorHomeViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.medical.presentation.ui.common.ScheduledAppointmentCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorHomeRoute(
    viewModel: DoctorHomeViewModel = koinViewModel(),
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToAppointments: () -> Unit = {},
    onNavigateToAppointmentDetail: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }


    DoctorHomeScreen(
        uiState = uiState,
        onNavigateToNotifications = onNavigateToNotifications,
        onNavigateToAppointments = onNavigateToAppointments,
        onRequestAction = viewModel::handleRequestAction,
        onNavigateToAppointmentDetail = onNavigateToAppointmentDetail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorHomeScreen(
    uiState: DoctorHomeUIState,
    onNavigateToNotifications: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onRequestAction: (String, Boolean) -> Unit,
    onNavigateToAppointmentDetail: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        androidx.compose.foundation.Image(
                            painter = androidx.compose.ui.res.painterResource(id = R.drawable.medical_app_logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.app_name),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToNotifications) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { /* Add event */ },
//                containerColor = MaterialTheme.colorScheme.primary,
//                contentColor = Color.White,
//                shape = CircleShape
//            ) {
//                Icon(Icons.Default.EditCalendar, contentDescription = "Add")
//            }
//        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                BoxWithConstraints {
                    if (maxWidth > 600.dp) {
                        // Tablet Layout
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ) {
                                LazyColumn {
                                    item {
                                        GreetingSection(doctorName = uiState.doctor?.name ?: "")
                                        Spacer(modifier = Modifier.height(24.dp))
                                        StatsSection()
                                        Spacer(modifier = Modifier.height(24.dp))
                                        RequestsSection(requests = uiState.pendingRequests, onViewAllClick = onNavigateToAppointments, onRequestAction = onRequestAction)
                                        Spacer(modifier = Modifier.height(24.dp))
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ) {
                                LazyColumn {
                                    item {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        AppointmentsSection(appointments = uiState.todayAppointments, onNavigateToAppointmentDetail = onNavigateToAppointmentDetail)
                                        Spacer(modifier = Modifier.height(80.dp)) // For FAB
                                    }
                                }
                            }
                        }
                    } else {
                        // Mobile Layout
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            item {
                                GreetingSection(doctorName = uiState.doctor?.name ?: "")
                                Spacer(modifier = Modifier.height(24.dp))
                                StatsSection()
                                Spacer(modifier = Modifier.height(24.dp))
                                RequestsSection(requests = uiState.pendingRequests, onViewAllClick = onNavigateToAppointments, onRequestAction = onRequestAction)
                                Spacer(modifier = Modifier.height(24.dp))
                                AppointmentsSection(appointments = uiState.todayAppointments, onNavigateToAppointmentDetail = onNavigateToAppointmentDetail)
                                Spacer(modifier = Modifier.height(80.dp)) // For FAB
                            }
                        }
                    }
                }
            }
            
            com.example.medical.presentation.ui.common.MedicalToast(
                toastData = uiState.toastData,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun GreetingSection(doctorName: String) {
    val currentHour = remember { java.time.LocalTime.now().hour }
    val greetingResId = when (currentHour) {
        in 0..11 -> R.string.good_morning
        in 12..17 -> R.string.good_afternoon
        else -> R.string.good_evening
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(greetingResId),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = doctorName,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StatsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.appointments_today),
            value = "12",
            icon = Icons.Default.Group
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.pending_requests),
            value = "3",
            icon = Icons.Default.AssignmentLate,
            hasBadge = true
        )
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    hasBadge: Boolean = false
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (hasBadge) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.Red, CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
fun RequestsSection(
    requests: List<AppointmentRequest>,
    onViewAllClick: () -> Unit,
    onRequestAction: (String, Boolean) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.requests_to_process),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.view_all),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onViewAllClick() }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(requests) { request ->
                RequestCard(request = request, onRequestAction = onRequestAction)
            }
        }
    }
}

@Composable
fun RequestCard(
    request: AppointmentRequest,
    onRequestAction: (String, Boolean) -> Unit
) {
    Card(
        modifier = Modifier.width(280.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = request.patientInitial,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = request.patientName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = request.timeRange,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.reason_label),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = request.reason,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onRequestAction(request.id, false) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(stringResource(R.string.reject), color = MaterialTheme.colorScheme.onSurface)
                }
                Button(
                    onClick = { onRequestAction(request.id, true) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(stringResource(R.string.confirm))
                }
            }
        }
    }
}

@Composable
fun AppointmentsSection(appointments: List<Appointment>, onNavigateToAppointmentDetail: (String) -> Unit) {
    Column {
        Text(
            text = stringResource(R.string.schedule_today),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        appointments.forEachIndexed { index, appointment ->
            ScheduledAppointmentCard(
                appointment = appointment,
                isLast = index == appointments.size - 1,
                onNavigateToAppointmentDetail = onNavigateToAppointmentDetail
            )
        }
    }
}