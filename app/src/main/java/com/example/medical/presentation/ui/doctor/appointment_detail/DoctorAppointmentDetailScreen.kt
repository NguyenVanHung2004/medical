package com.example.medical.presentation.ui.doctor.appointment_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.medical.R
import org.koin.androidx.compose.koinViewModel
import com.example.medical.domain.model.AppointmentType
import androidx.compose.foundation.BorderStroke

@Composable
fun DoctorAppointmentDetailRoute(
    appointmentId: String,
    onNavigateBack: () -> Unit,
    viewModel: DoctorAppointmentDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(appointmentId) {
        viewModel.loadAppointmentDetail(appointmentId)
    }

    DoctorAppointmentDetailScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorAppointmentDetailScreen(
    uiState: DoctorAppointmentDetailUiState,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.appointment_detail_title),
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.textPrimary)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_description),
                            tint = colorResource(id = R.color.textPrimary)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = colorResource(id = R.color.textPrimary)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.white))
            )
        },
        containerColor = colorResource(id = R.color.bgLight)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage,
                    color = colorResource(id = R.color.errorRed),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (uiState.appointment != null) {
                val appointment = uiState.appointment
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val isTablet = maxWidth > 600.dp
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(if (isTablet) 0.7f else 1f)
                                .padding(bottom = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Card 1: Status & ID
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .background(colorResource(id = R.color.primaryBlueLight), RoundedCornerShape(16.dp))
                                            .padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircleOutline,
                                            contentDescription = null,
                                            tint = colorResource(id = R.color.primaryBlue),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = stringResource(id = R.string.status_confirmed),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = colorResource(id = R.color.primaryBlue)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "ID: #${appointment.id}",
                                        fontSize = 14.sp,
                                        color = colorResource(id = R.color.textSecondary),
                                        letterSpacing = 1.sp
                                    )
                                }
                            }

                            // Card 2: Patient Info (Adapted from Doctor Info)
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (appointment.patientAvatarUrl.isNullOrEmpty()) {
                                            Box(
                                                modifier = Modifier
                                                    .size(64.dp)
                                                    .clip(CircleShape)
                                                    .background(colorResource(id = R.color.primaryBlueLight)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = appointment.patientInitial,
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = colorResource(id = R.color.primaryBlue)
                                                )
                                            }
                                        } else {
                                            AsyncImage(
                                                model = appointment.patientAvatarUrl,
                                                contentDescription = "Avatar",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(64.dp)
                                                    .clip(CircleShape)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Column {
                                            Text(
                                                text = appointment.patientName,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = colorResource(id = R.color.textPrimary)
                                            )
                                            Text(
                                                text = stringResource(id = R.string.patient_info_format, appointment.patientGender, appointment.patientAge, appointment.patientIdStr),
                                                fontSize = 14.sp,
                                                color = colorResource(id = R.color.textSecondary),
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                    }

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = colorResource(id = R.color.dividerColor))

                                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                        OutlinedButton(
                                            onClick = { /* TODO */ },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp),
                                            border = BorderStroke(1.dp, colorResource(id = R.color.primaryBlue)),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(id = R.color.primaryBlue))
                                        ) {
                                            Text(stringResource(id = R.string.view_profile))
                                        }
                                    }
                                }
                            }

                            // Card 3: Appointment details
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.Top) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = "Time",
                                            tint = colorResource(id = R.color.primaryBlue),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = stringResource(id = R.string.time_label),
                                                fontSize = 12.sp,
                                                color = colorResource(id = R.color.textSecondary)
                                            )
                                            Text(
                                                text = "${appointment.timeRange}\n${appointment.date}",
                                                fontSize = 16.sp,
                                                color = colorResource(id = R.color.textPrimary)
                                            )
                                        }
                                    }

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = colorResource(id = R.color.dividerColor))

                                    Row(verticalAlignment = Alignment.Top) {
                                        Icon(
                                            imageVector = if (appointment.type == AppointmentType.ONLINE) Icons.Default.Videocam else Icons.Default.LocationOn,
                                            contentDescription = "Consultation Type",
                                            tint = colorResource(id = R.color.primaryBlue),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                text = stringResource(id = R.string.consultation_type_label),
                                                fontSize = 12.sp,
                                                color = colorResource(id = R.color.textSecondary)
                                            )
                                            Text(
                                                text = if (appointment.type == AppointmentType.ONLINE) stringResource(id = R.string.online_consultation) else stringResource(id = R.string.offline_consultation),
                                                fontSize = 16.sp,
                                                color = colorResource(id = R.color.textPrimary)
                                            )
                                            
                                            val locationText = appointment.location ?: appointment.doctor.hospital
                                            if (appointment.type == AppointmentType.OFFLINE && !locationText.isNullOrEmpty()) {
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = locationText,
                                                    fontSize = 14.sp,
                                                    color = colorResource(id = R.color.textPrimary)
                                                )
                                            }

                                            if (appointment.type == AppointmentType.ONLINE) {
                                                Spacer(modifier = Modifier.height(12.dp))
                                                Button(
                                                    onClick = { /* TODO */ },
                                                    modifier = Modifier.fillMaxWidth(),
                                                    shape = RoundedCornerShape(8.dp),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = colorResource(id = R.color.primaryBlueLight),
                                                        contentColor = colorResource(id = R.color.primaryBlue)
                                                    )
                                                ) {
                                                    Icon(imageVector = Icons.Default.Link, contentDescription = null, modifier = Modifier.size(18.dp))
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(stringResource(id = R.string.join_clinic_room), fontWeight = FontWeight.Medium)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Card 4: Reason for Visit & Symptoms
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = stringResource(id = R.string.reason_for_visit_label),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(id = R.color.textPrimary)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = appointment.reason,
                                        fontSize = 14.sp,
                                        color = colorResource(id = R.color.textPrimary),
                                        lineHeight = 20.sp
                                    )
                                }
                            }

                            // Card 5: Payment / Actions (Adapting Privacy card)
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.NoteAlt,
                                            contentDescription = null,
                                            tint = colorResource(id = R.color.textPrimary),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = stringResource(id = R.string.notes_label),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = colorResource(id = R.color.textPrimary)
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    OutlinedTextField(
                                        value = "",
                                        onValueChange = {},
                                        placeholder = { Text("Thêm ghi chú y tế cho bệnh nhân...") },
                                        modifier = Modifier.fillMaxWidth().height(100.dp),
                                        shape = RoundedCornerShape(8.dp)
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

