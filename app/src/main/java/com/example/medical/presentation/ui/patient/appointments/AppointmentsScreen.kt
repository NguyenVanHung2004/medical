package com.example.medical.presentation.ui.patient.appointments

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.medical.R
import com.example.medical.domain.model.AppointmentStatus
import com.example.medical.domain.model.AppointmentType
import com.example.medical.presentation.ui.patient.appointment_detail.getStatusStringRes
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppointmentsRoute(
    onNavigateToHome: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    viewModel: AppointmentsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    AppointmentsScreen(
        uiState = uiState,
        onNavigateToHome = onNavigateToHome,
        onNavigateToDetail = onNavigateToDetail,
        onCancelRequest = { id -> viewModel.requestCancelAppointment(id) },
        onConfirmCancel = { viewModel.confirmCancelAppointment() },
        onDismissCancel = { viewModel.hideCancelDialog() },
        onRescheduleClick = { id -> viewModel.rescheduleAppointment(id) }
    )
}

@Composable
fun AppointmentsScreen(
    uiState: AppointmentsUiState,
    onNavigateToHome: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onCancelRequest: (String) -> Unit,
    onConfirmCancel: () -> Unit,
    onDismissCancel: () -> Unit,
    onRescheduleClick: (String) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(id = R.string.tab_upcoming_appointments),
        stringResource(id = R.string.tab_history_appointments)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
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
                    contentDescription = stringResource(id = R.string.back_button_description),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = stringResource(id = R.string.my_appointments_title),
                modifier = Modifier.align(Alignment.Center),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = MaterialTheme.colorScheme.primary,
                    height = 3.dp
                )
            },
            divider = {
                HorizontalDivider(
                    Modifier,
                    DividerDefaults.Thickness,
                    color = MaterialTheme.colorScheme.outline
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
                            color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        // List
        if (uiState.isLoading) {
            Box(modifier = Modifier
                .fillMaxSize()
                .weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (selectedTabIndex == 0) {
                    if (uiState.upcomingAppointments.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(id = R.string.no_upcoming_appointments),
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        items(uiState.upcomingAppointments) { appt ->
                            AppointmentCard(
                                doctorName = appt.doctor.name,
                                specialty = appt.doctor.specialty,
                                avatarUrl = appt.doctor.avatarUrl,
                                date = appt.date,
                                time = appt.timeRange,
                                location = appt.location ?: appt.doctor.hospital,
                                status = appt.status,
                                onClickDetail = { onNavigateToDetail(appt.id) },
                                onCancelClick = { onCancelRequest(appt.id) },
                                isOnline = appt.type == AppointmentType.ONLINE
                            )
                        }
                    }
                } else {
                    if (uiState.historyAppointments.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(id = R.string.no_history_appointments),
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { /* TODO */ },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(
                                        Icons.Default.PictureAsPdf,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(stringResource(id = R.string.export_pdf), fontSize = 12.sp)
                                }
                                OutlinedButton(
                                    onClick = { /* TODO */ },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        colorResource(id = R.color.primaryBlue)
                                    ),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = colorResource(
                                            id = R.color.primaryBlue
                                        )
                                    )
                                ) {
                                    Icon(
                                        Icons.Default.Share,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(stringResource(id = R.string.share_info), fontSize = 12.sp)
                                }
                            }
                        }
                        items(uiState.historyAppointments) { appt ->
                            HistoryAppointmentCard(
                                doctorName = appt.doctor.name,
                                specialty = appt.doctor.specialty,
                                avatarUrl = appt.doctor.avatarUrl,
                                date = appt.date,
                                time = appt.timeRange,
                                reason = appt.reason,
                                notes = "",
                                onRescheduleClick = { onRescheduleClick(appt.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (uiState.appointmentToCancel != null) {
        AlertDialog(
            onDismissRequest = onDismissCancel,
            title = { Text("Hủy Lịch Hẹn", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Bạn có chắc chắn muốn hủy lịch hẹn này không?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Chính sách hủy: Nếu bạn hủy trước 24 giờ, bạn sẽ được hoàn tiền 100%. " +
                                "Hủy trong vòng 24 giờ sẽ chịu phí 30%.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirmCancel,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Xác nhận Hủy")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissCancel) {
                    Text("Đóng", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}


@Composable
fun AppointmentCard(
    doctorName: String,
    specialty: String,
    avatarUrl: String?,
    date: String,
    time: String,
    location: String?,
    isOnline: Boolean,
    status: AppointmentStatus,
    onClickDetail: () -> Unit,
    onCancelClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = specialty,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (status != null) {
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = stringResource(id = getStatusStringRes(status)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Details block
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = date, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Time",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = time, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isOnline) Icons.Default.Videocam else Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = if (isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isOnline) stringResource(id = R.string.online_consultation_type) else (location
                            ?: stringResource(id = R.string.no_address)),
                        fontSize = 14.sp,
                        color = if (isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Button
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color.Red),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = androidx.compose.ui.graphics.Color.Red)
                ) {
                    Text(
                        "Hủy",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
                Button(
                    onClick = onClickDetail,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = if (isOnline) stringResource(id = R.string.btn_enter_clinic) else stringResource(
                            id = R.string.btn_details
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryAppointmentCard(
    doctorName: String,
    specialty: String,
    avatarUrl: String?,
    date: String,
    time: String,
    reason: String?,
    notes: String?,
    onRescheduleClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = doctorName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = doctorName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = specialty,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Details block
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "$time, $date",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (reason != null) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    Column {
                        Text(
                            text = stringResource(id = R.string.reason_for_visit_label),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = reason,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                if (notes != null) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    Column {
                        Text(
                            text = stringResource(id = R.string.notes_label),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = notes,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onRescheduleClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, colorResource(id = R.color.primaryBlue)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(id = R.color.primaryBlue))
                ) {
                    Text(
                        stringResource(id = R.string.rebook_appointment),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        stringResource(id = R.string.follow_up_appointment),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


