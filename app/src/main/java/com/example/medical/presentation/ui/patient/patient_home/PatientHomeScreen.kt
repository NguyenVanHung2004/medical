package com.example.medical.presentation.ui.patient.patient_home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.medical.R
import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.Specialty
import com.example.medical.presentation.ui.patient.appointments.AppointmentsRoute
import com.example.medical.presentation.ui.patient.notifications.NotificationsRoute
import com.example.medical.presentation.ui.patient.profile.ProfileRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun PatientHomeRoute(
    onNavigateToDoctorList: (String, String?) -> Unit,
    onNavigateToAppointmentDetail: (String) -> Unit,
    viewModel: PatientHomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    PatientHomeScreen(
        uiState = uiState,
        onNavigateToDoctorList = onNavigateToDoctorList,
        onNavigateToAppointmentDetail = onNavigateToAppointmentDetail
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientHomeScreen(
    uiState: PatientHomeUiState,
    onNavigateToDoctorList: (String, String?) -> Unit,
    onNavigateToAppointmentDetail: (String) -> Unit
) {

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp > 600

    var currentTab by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("home") }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        },
        containerColor = colorResource(id = R.color.bgLight)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(bottom = paddingValues.calculateBottomPadding())) {
            when (currentTab) {
            "home" -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    if (isTablet) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 32.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                HeaderSection(userName = uiState.userName)
                                uiState.upcomingAppointment?.let { appointment ->
                                    UpcomingAppointmentCard(
                                        appointment,
                                        onNavigateToAppointmentDetail
                                    )
                                }
                                QuickActionsSection(onNavigateToDoctorList)
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                PopularSpecialtiesSection(
                                    specialties = uiState.specialties,
                                    onNavigateToDoctorList = onNavigateToDoctorList
                                )
                                HealthCornerSection()
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState())
                                .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            HeaderSection(userName = uiState.userName)
                            uiState.upcomingAppointment?.let { appointment ->
                                UpcomingAppointmentCard(appointment, onNavigateToAppointmentDetail)
                            }
                            QuickActionsSection(onNavigateToDoctorList)
                            PopularSpecialtiesSection(
                                specialties = uiState.specialties,
                                onNavigateToDoctorList = onNavigateToDoctorList
                            )
                            HealthCornerSection()
                        }
                    }
                }
            }

            "appointments" -> {
                AppointmentsRoute(
                    onNavigateToHome = { currentTab = "home" },
                    onNavigateToAppointments = { currentTab = "appointments" },
                    onNavigateToDetail = onNavigateToAppointmentDetail
                )
            }
            "profile" -> {
                ProfileRoute()
            }

            "notifications" -> {
                NotificationsRoute()
            }
        }
        }
    }
}

@Composable
fun HeaderSection(userName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.greeting_morning),
                fontSize = 14.sp,
                color = colorResource(id = R.color.textSecondary)
            )
            Text(
                text = userName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.textPrimary)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = colorResource(id = R.color.textPrimary)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(colorResource(id = R.color.errorRed))
                        .align(Alignment.TopEnd)
                )
            }

            AsyncImage(
                model = "https://i.pravatar.cc/150?img=11",
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun UpcomingAppointmentCard(appointment: Appointment, onClickDetail: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Card Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.upcoming_appointment),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.textSecondary)
                )
                Box(
                    modifier = Modifier
                        .background(
                            color = colorResource(id = R.color.primaryBlueLight),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.in_hours, ""),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.primaryBlue)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Doctor Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = appointment.doctor.avatarUrl,
                    contentDescription = appointment.doctor.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = appointment.doctor.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.textPrimary)
                    )
                    Text(
                        text = appointment.doctor.specialty,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.textSecondary),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date and Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(colorResource(id = R.color.bgLight), RoundedCornerShape(8.dp))
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Ngày khám",
                            tint = colorResource(id = R.color.primaryBlue),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = appointment.date,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(id = R.color.textPrimary)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(colorResource(id = R.color.bgLight), RoundedCornerShape(8.dp))
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info, // Use time icon if available
                            contentDescription = "Thời gian",
                            tint = colorResource(id = R.color.primaryBlue),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = appointment.timeRange,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(id = R.color.textPrimary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { onClickDetail(appointment.id) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(id = R.color.primaryBlue)),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.btn_details),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
                Button(
                    onClick = { onClickDetail(appointment.id) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.primaryBlue)),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.btn_enter_clinic),
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
fun QuickActionsSection(onNavigateToDoctorList: (String, String?) -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClickLabel = stringResource(id = R.string.action_telemedicine)) {
                        onNavigateToDoctorList("online", null)
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                colorResource(id = R.color.primaryBlueLight),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = stringResource(id = R.string.action_telemedicine),
                            tint = colorResource(id = R.color.primaryBlue)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(id = R.string.action_telemedicine),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.textPrimary)
                    )
                    Text(
                        text = stringResource(id = R.string.action_telemedicine_desc),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.textSecondary)
                    )
                }
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClickLabel = stringResource(id = R.string.action_in_person)) {
                        onNavigateToDoctorList("offline", null)
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                colorResource(id = R.color.primaryBlueLight),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = stringResource(id = R.string.action_in_person),
                            tint = colorResource(id = R.color.primaryBlue)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(id = R.string.action_in_person),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.textPrimary)
                    )
                    Text(
                        text = stringResource(id = R.string.action_in_person_desc),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.textSecondary)
                    )
                }
            }
        }
    }
}

@Composable
fun PopularSpecialtiesSection(
    specialties: List<Specialty>,
    onNavigateToDoctorList: (String, String?) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.popular_specialties),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.textPrimary)
            )
            Text(
                text = stringResource(id = R.string.see_all),
                fontSize = 14.sp,
                color = colorResource(id = R.color.primaryBlue),
                modifier = Modifier.clickable { onNavigateToDoctorList("", null) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(specialties) { specialty ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        onNavigateToDoctorList("all", specialty.name)
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(colorResource(id = R.color.white), CircleShape)
                            .border(1.dp, colorResource(id = R.color.dividerColor), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getSpecialtyIcon(specialty.name),
                            contentDescription = specialty.name,
                            tint = colorResource(id = R.color.primaryBlue)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = specialty.name,
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.textPrimary)
                    )
                }
            }
        }
    }
}

@Composable
fun HealthCornerSection() {
    Column {
        Text(
            text = stringResource(id = R.string.health_corner),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.textPrimary)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clickable(onClickLabel = stringResource(id = R.string.read_now)) { /* TODO */ },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.primaryBlueLight)), // Fallback background
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Placeholder for background image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.primaryBlueLight))
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .background(colorResource(id = R.color.white), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.health_corner),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.primaryBlue)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "5 cách phòng ngừa\ncúm mùa hiệu quả",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.textPrimary)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.read_now),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.primaryBlue)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(id = R.string.read_now),
                            tint = colorResource(id = R.color.primaryBlue),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentTab: String = "home",
    onTabSelected: (String) -> Unit = {}
) {
    NavigationBar(
        containerColor = colorResource(id = R.color.white),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text(stringResource(id = R.string.bottom_nav_search)) },
            selected = currentTab == "home",
            onClick = { onTabSelected("home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorResource(id = R.color.primaryBlue),
                selectedTextColor = colorResource(id = R.color.primaryBlue),
                unselectedIconColor = colorResource(id = R.color.textSecondary),
                unselectedTextColor = colorResource(id = R.color.textSecondary),
                indicatorColor = colorResource(id = R.color.primaryBlueLight)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Appointments") },
            label = { Text(stringResource(id = R.string.bottom_nav_appointments)) },
            selected = currentTab == "appointments",
            onClick = { onTabSelected("appointments") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorResource(id = R.color.primaryBlue),
                selectedTextColor = colorResource(id = R.color.primaryBlue),
                unselectedIconColor = colorResource(id = R.color.textSecondary),
                unselectedTextColor = colorResource(id = R.color.textSecondary),
                indicatorColor = colorResource(id = R.color.primaryBlueLight)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notifications") },
            label = { Text(stringResource(id = R.string.bottom_nav_notifications)) },
            selected = currentTab == "notifications",
            onClick = { onTabSelected("notifications") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorResource(id = R.color.primaryBlue),
                selectedTextColor = colorResource(id = R.color.primaryBlue),
                unselectedIconColor = colorResource(id = R.color.textSecondary),
                unselectedTextColor = colorResource(id = R.color.textSecondary)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text(stringResource(id = R.string.bottom_nav_profile)) },
            selected = currentTab == "profile",
            onClick = { onTabSelected("profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorResource(id = R.color.primaryBlue),
                selectedTextColor = colorResource(id = R.color.primaryBlue),
                unselectedIconColor = colorResource(id = R.color.textSecondary),
                unselectedTextColor = colorResource(id = R.color.textSecondary),
                indicatorColor = colorResource(id = R.color.primaryBlueLight)
            )
        )
    }
}

fun getSpecialtyIcon(name: String): ImageVector {
    return when {
        name.contains("Nhi", ignoreCase = true) -> Icons.Default.ChildCare
        name.contains("Thần kinh", ignoreCase = true) -> Icons.Default.Psychology
        name.contains("Răng", ignoreCase = true) || name.contains(
            "Nha",
            ignoreCase = true
        ) -> Icons.Default.Face

        name.contains("Tim", ignoreCase = true) -> Icons.Default.Favorite
        name.contains("Mắt", ignoreCase = true) -> Icons.Default.Visibility
        name.contains("Da liễu", ignoreCase = true) -> Icons.Default.PanTool
        name.contains("Nội", ignoreCase = true) -> Icons.Default.MedicalServices
        else -> Icons.Default.LocalHospital
    }
}
