package com.example.medical.presentation.ui.doctor.appointment

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medical.R
import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import com.example.medical.domain.model.AppointmentType
import org.koin.androidx.compose.koinViewModel
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.fadeOut

@Composable
fun DoctorAppointmentRoute(
    viewModel: DoctorAppointmentViewModel = koinViewModel(),
    onNavigateToAppointmentDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    DoctorAppointmentScreen(
        uiState = uiState,
        onNavigateToAppointmentDetail = onNavigateToAppointmentDetail,
        onHandleRequest = { id, isAccept ->
            viewModel.handleRequestAction(id, isAccept)
        },
        onSelectDate = { date ->
            viewModel.selectDate(date)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorAppointmentScreen(
    uiState: DoctorAppointmentUiState,
    onNavigateToAppointmentDetail: (String) -> Unit,
    onHandleRequest: (String, Boolean) -> Unit,
    onSelectDate: (java.time.LocalDate) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.pending_requests),
        stringResource(R.string.status_confirmed)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.manage_appointments),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                },
                actions = {
                    IconButton(onClick = { /* Filter */ }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    // Empty navigation icon to balance the title in the center
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            BoxWithConstraints(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                if (maxWidth > 600.dp) {
                    // Tablet Layout: Split screen into two columns
                    Row(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.pending_requests),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            PendingRequestsList(uiState.pendingRequests, onHandleRequest)
                        }
                        VerticalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.status_confirmed),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            ScheduledAppointmentsList(
                                uiState = uiState,
                                onNavigateToAppointmentDetail = onNavigateToAppointmentDetail,
                                onSelectDate = onSelectDate
                            )
                        }
                    }
                } else {
                    // Mobile Layout: Segmented Control
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(4.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                tabs.forEachIndexed { index, title ->
                                    val isSelected = selectedTabIndex == index
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                                            .clickable { selectedTabIndex = index }
                                            .padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = title,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }

                        AnimatedContent(
                            targetState = selectedTabIndex,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    (slideInHorizontally { width -> width } + fadeIn())
                                        .togetherWith(slideOutHorizontally { width -> -width } + fadeOut())
                                } else {
                                    (slideInHorizontally { width -> -width } + fadeIn())
                                        .togetherWith(slideOutHorizontally { width -> width } + fadeOut())
                                }
                            },
                            label = "tab_transition"
                        ) { targetIndex ->
                            if (targetIndex == 0) {
                                PendingRequestsList(uiState.pendingRequests, onHandleRequest)
                            } else {
                                ScheduledAppointmentsList(
                                    uiState = uiState,
                                    onNavigateToAppointmentDetail = onNavigateToAppointmentDetail,
                                    onSelectDate = onSelectDate
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PendingRequestsList(requests: List<AppointmentRequest>, onHandleRequest: (String, Boolean) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(requests) { request ->
            PendingRequestCard(request, onHandleRequest)
        }
    }
}

@Composable
fun PendingRequestCard(request: AppointmentRequest, onHandleRequest: (String, Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = request.patientInitial,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Event,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = request.timeRange, // Should have date here in a real app
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (request.type == AppointmentType.ONLINE) Icons.Default.Videocam else Icons.Default.LocalHospital,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (request.type == AppointmentType.ONLINE) stringResource(R.string.online) else stringResource(R.string.offline),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = request.reason,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
            
            if (request.type == AppointmentType.OFFLINE && request.location != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp).padding(top = 2.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = request.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onHandleRequest(request.id, false) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(stringResource(R.string.reject), color = MaterialTheme.colorScheme.onSurface)
                }
                Button(
                    onClick = { onHandleRequest(request.id, true) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(R.string.confirm), color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ScheduledAppointmentsList(
    uiState: DoctorAppointmentUiState, 
    onNavigateToAppointmentDetail: (String) -> Unit,
    onSelectDate: (java.time.LocalDate) -> Unit
) {
    val appointments = uiState.scheduledAppointments
    val availableDates = uiState.availableDates
    val selectedDate = uiState.selectedDate

    Column(modifier = Modifier.fillMaxSize()) {
        val daysOfWeek = listOf("CN", "T2", "T3", "T4", "T5", "T6", "T7")
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(availableDates.size) { index ->
                val date = availableDates[index]
                val isSelected = date == selectedDate
                val dayOfWeekStr = daysOfWeek[date.dayOfWeek.value % 7]
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                        .clickable { onSelectDate(date) },
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = dayOfWeekStr,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSelected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))

        if (appointments.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Không có lịch hẹn nào.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(appointments.size) { index ->
                    val appointment = appointments[index]
                    val isLast = index == appointments.size - 1
                    ScheduledAppointmentCard(appointment, isLast, onNavigateToAppointmentDetail)
                }
            }
        }
    }
}



@Composable
fun ScheduledAppointmentCard(appointment: Appointment, isLast: Boolean = false, onNavigateToAppointmentDetail: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        // Time
        Text(
            text = appointment.timeRange.split(" - ")[0],
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.width(48.dp),
            textAlign = TextAlign.End
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Axis Line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(MaterialTheme.colorScheme.background, CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Card
        Card(
            modifier = Modifier.weight(1f).padding(top = 4.dp, bottom = 16.dp).clickable { onNavigateToAppointmentDetail(appointment.id) },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header with Avatar, Name, Details and Chip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = appointment.patientInitial,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                            // Appointment Type Chip
                            val isOnline = appointment.type == AppointmentType.ONLINE
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = if (isOnline) Icons.Default.Videocam else Icons.Default.LocalHospital,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isOnline) stringResource(R.string.online) else stringResource(R.string.offline),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = appointment.patientName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        // Date & Time
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Event,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${appointment.date}, ${appointment.timeRange}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        
                        // Reason
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp).padding(top = 2.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = appointment.reason,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        
                        // Location (if offline)
                        val locationText = appointment.location ?: appointment.doctor.hospital
                        if (appointment.type == AppointmentType.OFFLINE && !locationText.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp).padding(top = 2.dp),
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = locationText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Actions
                if (appointment.type == AppointmentType.ONLINE) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { /* Enter room */ },
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.enter_clinic),
                                textAlign = TextAlign.Center,
                                lineHeight = 16.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                        }
                        OutlinedButton(
                            onClick = { onNavigateToAppointmentDetail(appointment.id) },
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.view_details),
                                textAlign = TextAlign.Center,
                                lineHeight = 16.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = { onNavigateToAppointmentDetail(appointment.id) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.view_details),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

