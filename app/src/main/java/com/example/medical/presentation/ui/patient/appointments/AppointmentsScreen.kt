package com.example.medical.presentation.ui.patient.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.medical.R

@Composable
fun AppointmentsRoute(
    onNavigateToHome: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    viewModel: AppointmentsViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    AppointmentsScreen(uiState = uiState, onNavigateToHome)
}

@Composable
fun AppointmentsScreen(uiState: AppointmentsUiState, onNavigateToHome : () -> Unit) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Sắp tới", "Lịch sử")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgLight))
    ) {
        // Custom Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = onNavigateToHome,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = colorResource(id = R.color.primaryBlue)
                )
            }

            Text(
                text = "Lịch hẹn của tôi",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.primaryBlue)
            )
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = colorResource(id = R.color.bgLight),
            contentColor = colorResource(id = R.color.primaryBlue),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = colorResource(id = R.color.primaryBlue),
                    height = 3.dp
                )
            },
            divider = {
                HorizontalDivider(
                    Modifier,
                    DividerDefaults.Thickness,
                    color = colorResource(id = R.color.dividerColor)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            color = if (selectedTabIndex == index) colorResource(id = R.color.primaryBlue) else colorResource(id = R.color.textSecondary)
                        )
                    }
                )
            }
        }

        // List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (selectedTabIndex == 0) {
                if (uiState.upcomingAppointments.isEmpty()) {
                    item {
                        Text(
                            text = "Chưa có lịch hẹn sắp tới.",
                            modifier = Modifier.padding(16.dp),
                            color = colorResource(id = R.color.textSecondary)
                        )
                    }
                } else {
                    items(uiState.upcomingAppointments) { appt ->
                        AppointmentCard(
                            doctorName = appt.doctor.name,
                            specialty = appt.doctor.specialty,
                            avatarUrl = appt.doctor.avatarUrl,
                            date = appt.date,
                            time = appt.time,
                            location = appt.location,
                            isOnline = appt.isOnline,
                            status = appt.status
                        )
                    }
                }
            } else {
                if (uiState.historyAppointments.isEmpty()) {
                    item {
                        Text(
                            text = "Chưa có lịch sử khám bệnh.",
                            modifier = Modifier.padding(16.dp),
                            color = colorResource(id = R.color.textSecondary)
                        )
                    }
                } else {
                    items(uiState.historyAppointments) { appt ->
                        AppointmentCard(
                            doctorName = appt.doctor.name,
                            specialty = appt.doctor.specialty,
                            avatarUrl = appt.doctor.avatarUrl,
                            date = appt.date,
                            time = appt.time,
                            location = appt.location,
                            isOnline = appt.isOnline,
                            status = appt.status
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppointmentCard(
    doctorName: String,
    specialty: String,
    avatarUrl: String,
    date: String,
    time: String,
    location: String,
    isOnline: Boolean,
    status: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = doctorName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = doctorName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.textPrimary)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = specialty,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.textSecondary)
                        )
                    }
                }
                
                if (status != null) {
                    Box(
                        modifier = Modifier
                            .background(colorResource(id = R.color.primaryBlueLight), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = status,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.primaryBlue)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Details block
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.bgLight), RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = colorResource(id = R.color.textSecondary),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = date, fontSize = 14.sp, color = colorResource(id = R.color.textPrimary))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Time",
                        tint = colorResource(id = R.color.textSecondary),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = time, fontSize = 14.sp, color = colorResource(id = R.color.textPrimary))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isOnline) Icons.Default.Videocam else Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = if (isOnline) colorResource(id = R.color.primaryBlue) else colorResource(id = R.color.textSecondary),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = location,
                        fontSize = 14.sp,
                        color = if (isOnline) colorResource(id = R.color.primaryBlue) else colorResource(id = R.color.textPrimary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Button
            OutlinedButton(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colorResource(id = R.color.primaryBlue)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(id = R.color.primaryBlue))
            ) {
                Text("Chi tiết", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
