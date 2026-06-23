package com.example.medical.presentation.ui.doctor.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.medical.R
import com.example.medical.domain.model.Doctor
import com.example.medical.domain.model.WorkingTimeSlot
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
import java.util.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import coil.compose.AsyncImage

@Composable
fun DoctorProfileRoute(
    viewModel: DoctorProfileViewModel = koinViewModel(),
    onLogout: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    DoctorProfileScreen(
        uiState = uiState,
        onLogout = onLogout,
        onToggleOnline = viewModel::toggleOnlineConsultation,
        onToggleOffline = viewModel::toggleInPersonConsultation,
        onShowWorkingHoursDialog = viewModel::showWorkingHoursDialog,
        onHideWorkingHoursDialog = viewModel::hideWorkingHoursDialog,
        onSelectDayOfWeek = viewModel::selectDayOfWeek,
        onToggleTimeSlot = viewModel::toggleTimeSlot,
        onConfirmWorkingHoursUpdate = viewModel::confirmWorkingHoursUpdate,
        onShowEditProfileDialog = viewModel::showEditProfileDialog,
        onHideEditProfileDialog = viewModel::hideEditProfileDialog,
        onSaveProfile = viewModel::saveProfile,
        onShowEditFeesDialog = viewModel::showEditFeesDialog,
        onHideEditFeesDialog = viewModel::hideEditFeesDialog,
        onSaveFees = viewModel::saveFees,
        onAvatarSelected = viewModel::updateAvatar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileScreen(
    uiState: DoctorProfileUiState,
    onLogout: () -> Unit,
    onToggleOnline: (Boolean) -> Unit,
    onToggleOffline: (Boolean) -> Unit,
    onShowWorkingHoursDialog: () -> Unit,
    onHideWorkingHoursDialog: () -> Unit,
    onSelectDayOfWeek: (DayOfWeek) -> Unit,
    onToggleTimeSlot: (WorkingTimeSlot) -> Unit,
    onConfirmWorkingHoursUpdate: () -> Unit,
    onShowEditProfileDialog: () -> Unit,
    onHideEditProfileDialog: () -> Unit,
    onSaveProfile: (String, String, String, String) -> Unit,
    onShowEditFeesDialog: () -> Unit,
    onHideEditFeesDialog: () -> Unit,
    onSaveFees: (Long, Long) -> Unit,
    onAvatarSelected: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.profile_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            uiState.doctor?.let { doctor ->
                BoxWithConstraints(modifier = Modifier.padding(paddingValues)) {
                    val contentModifier = if (maxWidth > 600.dp) {
                        Modifier
                            .width(600.dp)
                            .align(Alignment.Center)
                    } else {
                        Modifier.fillMaxWidth()
                    }

                    LazyColumn(
                        modifier = contentModifier
                            .fillMaxHeight()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            DoctorInfoCard(
                                doctor = doctor,
                                onEditClick = onShowEditProfileDialog,
                                onAvatarSelected = onAvatarSelected
                            )
                        }
                        item {
                            ServicesAndFeesCard(
                                doctor = doctor,
                                onToggleOnline = onToggleOnline,
                                onToggleOffline = onToggleOffline,
                                onEditClick = onShowEditFeesDialog
                            )
                        }
                        item {
                            WorkingHoursCard(
                                doctor = doctor,
                                onUpdateWorkingHours = onShowWorkingHoursDialog
                            )
                        }
                        item {
                            SettingsList()
                        }
                        item {
                            LogoutButton(onLogout = onLogout)
                        }
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }

        if (uiState.isWorkingHoursDialogVisible) {
            WorkingHoursDialog(
                selectedDayOfWeek = uiState.selectedDayOfWeek,
                timeSlots = uiState.timeSlotsForSelectedDay,
                onDismiss = onHideWorkingHoursDialog,
                onSelectDayOfWeek = onSelectDayOfWeek,
                onToggleTimeSlot = onToggleTimeSlot,
                onConfirm = onConfirmWorkingHoursUpdate
            )
        }

        if (uiState.isEditProfileDialogVisible && uiState.doctor != null) {
            EditProfileDialog(
                doctor = uiState.doctor,
                onDismiss = onHideEditProfileDialog,
                onSave = onSaveProfile
            )
        }

        if (uiState.isEditFeesDialogVisible && uiState.doctor != null) {
            EditFeesDialog(
                doctor = uiState.doctor,
                onDismiss = onHideEditFeesDialog,
                onSave = onSaveFees
            )
        }
    }
}

@Composable
fun DoctorInfoCard(doctor: Doctor, onEditClick: () -> Unit, onAvatarSelected: (String) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            onAvatarSelected(it.toString())
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (doctor.avatarUrl != null) {
                        AsyncImage(
                            model = doctor.avatarUrl,
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 8.dp, y = 8.dp)
                        .size(32.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .clip(CircleShape)
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Change Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = doctor.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = doctor.specialty.uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            if (doctor.hospital.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = doctor.hospital,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = doctor.experience,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(
                onClick = onEditClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.edit_profile))
            }
        }
    }
}

@Composable
fun ServicesAndFeesCard(
    doctor: Doctor,
    onToggleOnline: (Boolean) -> Unit,
    onToggleOffline: (Boolean) -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.services_and_fees),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onEditClick, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Fees",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.online_consultation_service),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${String.format(Locale.US, "%,d", doctor.onlineConsultationFee)}đ",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Switch(
                    checked = doctor.isOnlineConsultationEnabled,
                    onCheckedChange = onToggleOnline,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
            Divider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.in_person_consultation_service),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${
                            String.format(
                                Locale.US,
                                "%,d",
                                doctor.inPersonConsultationFee
                            )
                        }đ",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Switch(
                    checked = doctor.isInPersonConsultationEnabled,
                    onCheckedChange = onToggleOffline,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Composable
fun WorkingHoursCard(doctor: Doctor, onUpdateWorkingHours: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = stringResource(R.string.working_hours),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            val hasSchedule = DayOfWeek.values().any { day ->
                doctor.workingSchedule[day]?.any { it.isSelected } == true
            }

            if (!hasSchedule) {
                Text(
                    text = "Chưa có lịch làm việc",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                DayOfWeek.values().forEach { day ->
                    val slots = doctor.workingSchedule[day]?.filter { it.isSelected } ?: emptyList()
                    if (slots.isNotEmpty()) {
                        val dayName = when(day) {
                            DayOfWeek.MONDAY -> "Thứ 2"
                            DayOfWeek.TUESDAY -> "Thứ 3"
                            DayOfWeek.WEDNESDAY -> "Thứ 4"
                            DayOfWeek.THURSDAY -> "Thứ 5"
                            DayOfWeek.FRIDAY -> "Thứ 6"
                            DayOfWeek.SATURDAY -> "Thứ 7"
                            DayOfWeek.SUNDAY -> "Chủ nhật"
                        }
                        
                        Text(
                            text = dayName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val morningSlots = slots.filter { it.time < "12:00" }
                        val afternoonSlots = slots.filter { it.time >= "12:00" }
                        val maxRows = maxOf(morningSlots.size, afternoonSlots.size)
                        
                        for (i in 0 until maxRows) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                val morningTime = morningSlots.getOrNull(i)?.time ?: ""
                                val afternoonTime = afternoonSlots.getOrNull(i)?.time ?: ""
                                
                                Text(
                                    text = morningTime,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = afternoonTime,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
            Button(
                onClick = onUpdateWorkingHours,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.EditCalendar,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.update_working_hours))
            }
        }
    }
}

@Composable
fun SettingsList() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            SettingItem(
                icon = Icons.Default.NotificationsNone,
                title = stringResource(R.string.notification_settings)
            )
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            SettingItem(
                icon = Icons.Default.Security,
                title = stringResource(R.string.account_security)
            )
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            SettingItem(
                icon = Icons.Default.HelpOutline,
                title = stringResource(R.string.help_and_faq)
            )
        }
    }
}

@Composable
fun SettingItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}

@Composable
fun LogoutButton(onLogout: () -> Unit) {
    OutlinedButton(
        onClick = onLogout,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
    ) {
        Icon(
            imageVector = Icons.Default.Logout,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(R.string.logout), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun WorkingHoursDialog(
    selectedDayOfWeek: DayOfWeek,
    timeSlots: List<WorkingTimeSlot>,
    onDismiss: () -> Unit,
    onSelectDayOfWeek: (DayOfWeek) -> Unit,
    onToggleTimeSlot: (WorkingTimeSlot) -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.change_date_and_time),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Days selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val daysOfWeek = DayOfWeek.values()
                    daysOfWeek.forEach { day ->
                        val isSelected = selectedDayOfWeek == day
                        val dayName = when (day) {
                            DayOfWeek.MONDAY -> "T2"
                            DayOfWeek.TUESDAY -> "T3"
                            DayOfWeek.WEDNESDAY -> "T4"
                            DayOfWeek.THURSDAY -> "T5"
                            DayOfWeek.FRIDAY -> "T6"
                            DayOfWeek.SATURDAY -> "T7"
                            DayOfWeek.SUNDAY -> "CN"
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                                .clickable { onSelectDayOfWeek(day) }
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = dayName,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Morning slots
                Text(
                    text = stringResource(R.string.morning),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                TimeSlotGrid(
                    slots = timeSlots.filter { it.time < "12:00" },
                    onToggle = onToggleTimeSlot
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Afternoon slots
                Text(
                    text = stringResource(R.string.afternoon),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                TimeSlotGrid(
                    slots = timeSlots.filter { it.time >= "12:00" },
                    onToggle = onToggleTimeSlot
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = stringResource(R.string.confirm_schedule_change))
                }
            }
        }
    }
}

@Composable
fun TimeSlotGrid(
    slots: List<WorkingTimeSlot>,
    onToggle: (WorkingTimeSlot) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (i in slots.indices step 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TimeSlotItem(
                    slot = slots[i],
                    onToggle = onToggle,
                    modifier = Modifier.weight(1f)
                )
                if (i + 1 < slots.size) {
                    TimeSlotItem(
                        slot = slots[i + 1],
                        onToggle = onToggle,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun TimeSlotItem(
    slot: WorkingTimeSlot,
    onToggle: (WorkingTimeSlot) -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor =
        if (slot.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor =
        if (slot.isSelected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
    val borderColor =
        if (slot.isSelected) Color.Transparent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(containerColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onToggle(slot) }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = slot.time,
            style = MaterialTheme.typography.bodySmall,
            color = contentColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    doctor: Doctor,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(doctor.name) }
    var specialty by remember { mutableStateOf(doctor.specialty) }
    var hospital by remember { mutableStateOf(doctor.hospital) }
    var experience by remember { mutableStateOf(doctor.experience) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.edit_profile),
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Cập nhật thông tin cá nhân",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Họ và Tên") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = specialty,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Chuyên khoa") },
                        leadingIcon = { Icon(Icons.Default.MedicalServices, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        com.example.medical.domain.model.DoctorSpecialty.values().forEach { spec ->
                            DropdownMenuItem(
                                text = { Text(spec.displayName) },
                                onClick = {
                                    specialty = spec.displayName
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = hospital,
                    onValueChange = { hospital = it },
                    label = { Text("Bệnh viện / Nơi công tác") },
                    leadingIcon = { Icon(Icons.Default.LocalHospital, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = experience,
                    onValueChange = { experience = it },
                    label = { Text("Kinh nghiệm") },
                    leadingIcon = { Icon(Icons.Default.WorkOutline, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { onSave(name, specialty, hospital, experience) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Lưu Thay Đổi", style = MaterialTheme.typography.titleMedium, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun EditFeesDialog(
    doctor: Doctor,
    onDismiss: () -> Unit,
    onSave: (Long, Long) -> Unit
) {
    var onlineFeeStr by remember { mutableStateOf(doctor.onlineConsultationFee.toString()) }
    var inPersonFeeStr by remember { mutableStateOf(doctor.inPersonConsultationFee.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Chỉnh sửa chi phí",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Thiết lập chi phí dịch vụ khám",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = onlineFeeStr,
                    onValueChange = { onlineFeeStr = it },
                    label = { Text("Chi phí khám trực tuyến (VNĐ)") },
                    leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = inPersonFeeStr,
                    onValueChange = { inPersonFeeStr = it },
                    label = { Text("Chi phí khám tại viện (VNĐ)") },
                    leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        val onlineFee = onlineFeeStr.toLongOrNull() ?: doctor.onlineConsultationFee
                        val inPersonFee = inPersonFeeStr.toLongOrNull() ?: doctor.inPersonConsultationFee
                        onSave(onlineFee, inPersonFee)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Lưu Thay Đổi", style = MaterialTheme.typography.titleMedium, color = Color.White)
                }
            }
        }
    }
}
