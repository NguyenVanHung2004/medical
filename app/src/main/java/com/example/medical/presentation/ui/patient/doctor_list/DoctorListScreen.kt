package com.example.medical.presentation.ui.patient.doctor_list

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.medical.R
import com.example.medical.domain.model.ConsultationType
import com.example.medical.domain.model.DoctorDetail
import com.example.medical.presentation.ui.patient.patient_home.BottomNavigationBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun DoctorListRoute(
    onNavigateBack: () -> Unit,
    onNavigateToBooking: (String) -> Unit,
    viewModel: DoctorListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    DoctorListScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onSpecialtySelected = viewModel::onSpecialtySelected,
        onLocationSelected = viewModel::onLocationSelected,
        onRatingSelected = viewModel::onRatingSelected,
        onAvailabilitySelected = viewModel::onAvailabilitySelected,
        onInsuranceSelected = viewModel::onInsuranceSelected,
        onDoctorClick = onNavigateToBooking
    )
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorListScreen(
    uiState: DoctorListUiState,
    onNavigateBack: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSpecialtySelected: (String?) -> Unit,
    onLocationSelected: (String?) -> Unit,
    onRatingSelected: (String?) -> Unit,
    onAvailabilitySelected: (String?) -> Unit,
    onInsuranceSelected: (String?) -> Unit,
    onDoctorClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.title,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.primaryBlue),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colorResource(id = R.color.primaryBlue)
                        )
                    }
                },
                actions = {
                    // Spacer to balance the title centering
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.white))
            )
        },
        bottomBar = {
            BottomNavigationBar()
        },
        containerColor = colorResource(id = R.color.bgLight)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Tìm kiếm bác sĩ, chuyên khoa...", color = colorResource(id = R.color.textSecondary)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = colorResource(id = R.color.textSecondary)) },
                    trailingIcon = { Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = colorResource(id = R.color.primaryBlue)) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(id = R.color.white),
                        unfocusedContainerColor = colorResource(id = R.color.white),
                        focusedBorderColor = colorResource(id = R.color.dividerColor),
                        unfocusedBorderColor = colorResource(id = R.color.dividerColor)
                    ),
                    singleLine = true
                )

                // Filter Chips
                Text(
                    text = "Bộ lọc tìm kiếm",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.textPrimary)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChipWithDropdown(
                            label = "Chuyên khoa",
                            options = listOf("Nhi khoa", "Tim mạch", "Da liễu", "Nha khoa"),
                            selectedValue = uiState.selectedSpecialty,
                            onValueSelected = onSpecialtySelected
                        )
                    }
                    item {
                        FilterChipWithDropdown(
                            label = "Vị trí",
                            options = listOf("Hà Nội", "TP.HCM", "Đà Nẵng", "Gần tôi nhất"),
                            selectedValue = uiState.selectedLocation,
                            onValueSelected = onLocationSelected
                        )
                    }
                    item {
                        FilterChipWithDropdown(
                            label = "Đánh giá",
                            options = listOf("Từ 4.5 sao", "Từ 4.0 sao", "Tất cả đánh giá"),
                            selectedValue = uiState.selectedRating,
                            onValueSelected = onRatingSelected
                        )
                    }
                    item {
                        FilterChipWithDropdown(
                            label = "Lịch hẹn",
                            options = listOf("Sẵn sàng hôm nay", "Trong tuần này"),
                            selectedValue = uiState.selectedAvailability,
                            onValueSelected = onAvailabilitySelected
                        )
                    }
                    item {
                        FilterChipWithDropdown(
                            label = "Bảo hiểm",
                            options = listOf("Bảo hiểm y tế", "Bảo hiểm Bảo Việt", "Liberty"),
                            selectedValue = uiState.selectedInsurance,
                            onValueSelected = onInsuranceSelected
                        )
                    }
                }

                // Doctor List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.doctors) { doctor ->
                        DoctorCard(doctor = doctor, onClick = { onDoctorClick(doctor.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun DoctorCard(doctor: DoctorDetail, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar with Online Indicator
            Box {
                AsyncImage(
                    model = doctor.avatarUrl,
                    contentDescription = doctor.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
                val statusColor = if (doctor.isOnline) Color(0xFF4ADE80) else Color.Gray // Green/Gray
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                        .border(2.dp, colorResource(id = R.color.white), CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Doctor Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = doctor.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.textPrimary)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(colorResource(id = R.color.primaryBlueLight), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFBBF24), modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = doctor.rating.toString(), fontSize = 12.sp, color = colorResource(id = R.color.textPrimary), fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${doctor.specialty} • ${doctor.yearsOfExperience} năm KN",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.textSecondary),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Badges
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (doctor.isFullyBookedToday) {
                        BadgeItem(
                            icon = Icons.Default.CalendarToday,
                            text = "Kín lịch hôm nay",
                            backgroundColor = colorResource(id = R.color.primaryBlueLight),
                            textColor = colorResource(id = R.color.textSecondary),
                            iconColor = colorResource(id = R.color.textSecondary)
                        )
                    } else {
                        if (doctor.supportedTypes.contains(ConsultationType.ONLINE)) {
                            BadgeItem(
                                icon = Icons.Default.Videocam,
                                text = "Trực tuyến",
                                backgroundColor = colorResource(id = R.color.primaryBlue),
                                textColor = colorResource(id = R.color.white),
                                iconColor = colorResource(id = R.color.white)
                            )
                        }
                        if (doctor.supportedTypes.contains(ConsultationType.OFFLINE)) {
                            BadgeItem(
                                icon = Icons.Default.Business,
                                text = "Trực tiếp",
                                backgroundColor = colorResource(id = R.color.dividerColor),
                                textColor = colorResource(id = R.color.textSecondary),
                                iconColor = colorResource(id = R.color.textSecondary)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeItem(
    icon: ImageVector,
    text: String,
    backgroundColor: Color,
    textColor: Color,
    iconColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(12.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontSize = 12.sp, color = textColor, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun FilterChipWithDropdown(
    label: String,
    options: List<String>,
    selectedValue: String?,
    onValueSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        val isSelected = selectedValue != null
        val displayText = selectedValue ?: label
        
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    color = if (isSelected) colorResource(id = R.color.primaryBlue) else colorResource(id = R.color.white)
                )
                .border(
                    width = 1.dp,
                    color = if (isSelected) Color.Transparent else colorResource(id = R.color.dividerColor),
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayText,
                color = if (isSelected) colorResource(id = R.color.white) else colorResource(id = R.color.textPrimary),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = if (isSelected) colorResource(id = R.color.white) else colorResource(id = R.color.textSecondary),
                modifier = Modifier.size(16.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(colorResource(id = R.color.white))
        ) {
            // Option to clear filter
            DropdownMenuItem(
                text = { Text("Tất cả", color = colorResource(id = R.color.textPrimary)) },
                onClick = {
                    onValueSelected(null)
                    expanded = false
                }
            )
            options.forEach { option ->
                DropdownMenuItem(
                    text = { 
                        Text(
                            text = option, 
                            color = if (selectedValue == option) colorResource(id = R.color.primaryBlue) else colorResource(id = R.color.textPrimary),
                            fontWeight = if (selectedValue == option) FontWeight.Bold else FontWeight.Normal
                        ) 
                    },
                    onClick = {
                        onValueSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
