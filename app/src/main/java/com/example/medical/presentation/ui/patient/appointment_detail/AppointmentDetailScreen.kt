package com.example.medical.presentation.ui.patient.appointment_detail

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.medical.R
import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentStatus
import com.example.medical.domain.model.AppointmentType
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.ui.zIndex
import com.example.medical.presentation.ui.common.SecondaryButton

@Composable
fun AppointmentDetailRoute(
    appointmentId: String,
    onNavigateBack: () -> Unit,
    onNavigateToChangeDoctor: (String) -> Unit,
    onNavigateToReschedule: (String) -> Unit,
    viewModel: AppointmentDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    if (uiState.isLoading && uiState.appointment == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else if (uiState.appointment != null) {
        AppointmentDetailScreen(
            appointment = uiState.appointment!!,
            showCancelDialog = uiState.showCancelDialog,
            isSubmitting = uiState.isSubmitting,
            onNavigateBack = onNavigateBack,
            onNavigateToChangeDoctor = { onNavigateToChangeDoctor(uiState.appointment!!.doctor.id) },
            onNavigateToReschedule = { onNavigateToReschedule(uiState.appointment!!.doctor.id) },
            onCancelRequest = viewModel::showCancelDialog,
            onConfirmCancel = viewModel::cancelAppointment,
            onDismissCancel = viewModel::hideCancelDialog
        )
        
        var toastData by remember { mutableStateOf<com.example.medical.presentation.ui.common.ToastData?>(null) }
        
        LaunchedEffect(uiState.successMessage) {
            if (uiState.successMessage != null) {
                toastData = com.example.medical.presentation.ui.common.ToastData(uiState.successMessage!!, com.example.medical.presentation.ui.common.ToastType.SUCCESS)
                kotlinx.coroutines.delay(2000)
                toastData = null
                viewModel.clearMessages()
            }
        }
        
        LaunchedEffect(uiState.error) {
            if (uiState.error != null) {
                toastData = com.example.medical.presentation.ui.common.ToastData(uiState.error!!, com.example.medical.presentation.ui.common.ToastType.ERROR)
                kotlinx.coroutines.delay(3000)
                toastData = null
                viewModel.clearMessages()
            }
        }
        
        Box(modifier = Modifier.fillMaxSize()) {
            com.example.medical.presentation.ui.common.MedicalToast(
                toastData = toastData,
                modifier = Modifier.align(Alignment.TopCenter).zIndex(100f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailScreen(
    appointment: Appointment,
    showCancelDialog: Boolean,
    isSubmitting: Boolean,
    onNavigateBack: () -> Unit,
    onNavigateToChangeDoctor: () -> Unit,
    onNavigateToReschedule: () -> Unit,
    onCancelRequest: () -> Unit,
    onConfirmCancel: () -> Unit,
    onDismissCancel: () -> Unit
) {
    var prescriptionAllowed by remember { mutableStateOf(true) }
    var shareResultsAllowed by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.appointment_detail_title),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack, enabled = !isSubmitting) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.back_button_description),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO */ }, enabled = !isSubmitting) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Card 1: Status & ID
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (appointment.status == AppointmentStatus.CANCELLED) Icons.Default.Cancel else Icons.Default.CheckCircleOutline,
                                contentDescription = null,
                                tint = if (appointment.status == AppointmentStatus.CANCELLED) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(id = getStatusStringRes(appointment.status)),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (appointment.status == AppointmentStatus.CANCELLED) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "ID: ${appointment.id}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Card 2: Doctor Info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = appointment.doctor.avatarUrl,
                                contentDescription = "Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = appointment.doctor.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = appointment.doctor.specialty.uppercase(),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = Color(0xFFFFB300),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "4.9",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = " (120 đánh giá)",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = MaterialTheme.colorScheme.outline
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            OutlinedButton(
                                onClick = { /* TODO */ },
                                modifier = Modifier.weight(1f),
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
                                Text(stringResource(id = R.string.view_profile))
                            }
                            OutlinedButton(
                                onClick = onNavigateToChangeDoctor,
                                modifier = Modifier.weight(1f),
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
                                Text(stringResource(id = R.string.change_doctor))
                            }
                        }
                    }
                }

                // Card 3: Appointment details
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Time",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = stringResource(id = R.string.time_label),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${appointment.timeRange}\n${appointment.date}",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = MaterialTheme.colorScheme.outline
                        )

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = if (appointment.type == AppointmentType.ONLINE) Icons.Default.Videocam else Icons.Default.LocationOn,
                                contentDescription = "Consultation Type",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(id = R.string.consultation_type_label),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (appointment.type == AppointmentType.ONLINE) stringResource(
                                        id = R.string.online_consultation_type
                                    ) else stringResource(id = R.string.offline_consultation_type),
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                if (appointment.type == AppointmentType.ONLINE) {
                                    Button(
                                        onClick = { /* TODO */ },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                            contentColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Link,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            stringResource(id = R.string.join_clinic_room),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                } else {
                                    Button(
                                        onClick = { /* TODO */ },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                            contentColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Map,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            stringResource(id = R.string.view_directions),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Card 4: Patient details
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(id = R.string.patient_label),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = appointment.patientName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = MaterialTheme.colorScheme.outline
                        )

                        Text(
                            text = stringResource(id = R.string.reason_for_visit_label),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = appointment.reason ?: "Không có thông tin",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )
                    }
                }

                // Card 5: Privacy
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(id = R.string.privacy_and_profile),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.allow_view_prescription_history),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = prescriptionAllowed,
                                onCheckedChange = { prescriptionAllowed = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFF00695C) // Green from mockup
                                )
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.share_test_results),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = shareResultsAllowed,
                                onCheckedChange = { shareResultsAllowed = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFF00695C)
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (appointment.status != AppointmentStatus.CANCELLED) {
                    // Action Buttons
                    SecondaryButton(
                        text = stringResource(id = R.string.change_appointment),
                        onClick = onNavigateToReschedule
                    )

                    TextButton(
                        onClick = onCancelRequest,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel_appointment_action),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            if (showCancelDialog) {
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

            if (isSubmitting) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .zIndex(200f)
                        .clickable(enabled = false) {}, // Intercept clicks
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
    fun getStatusStringRes(status: AppointmentStatus): Int {
        return when (status) {
            AppointmentStatus.UPCOMING -> R.string.status_upcoming
            AppointmentStatus.HAPPENING -> R.string.status_happening
            AppointmentStatus.COMPLETED -> R.string.status_completed
            AppointmentStatus.CANCELLED -> R.string.status_cancelled
            AppointmentStatus.PENDING -> R.string.status_pending
            AppointmentStatus.CONFIRMED -> R.string.status_confirmed
        }
    }


