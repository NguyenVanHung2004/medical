package com.example.medical.presentation.ui.patient.booking

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.medical.R
import com.example.medical.domain.model.BookingDate
import com.example.medical.domain.model.DoctorDetail
import com.example.medical.domain.model.TimeSlot
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookingRoute(
    onNavigateBack: () -> Unit,
    onNavigateToNext: (String, String, String) -> Unit,
    viewModel: BookingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    BookingScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNavigateToNext = {
            val doctorId = uiState.doctor?.id ?: return@BookingScreen
            val date = uiState.selectedDate?.let { "${it.dateString} ${it.month}" } ?: return@BookingScreen
            val time = uiState.selectedTimeSlot?.timeRange?.split(" - ")?.firstOrNull() ?: return@BookingScreen
            onNavigateToNext(doctorId, date, time)
        },
        onDateSelected = viewModel::selectDate,
        onTimeSlotSelected = viewModel::selectTimeSlot
    )
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    uiState: BookingUiState,
    onNavigateBack: () -> Unit,
    onNavigateToNext: () -> Unit,
    onDateSelected: (BookingDate) -> Unit,
    onTimeSlotSelected: (TimeSlot) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MediConnect",
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
                actions = { Spacer(modifier = Modifier.width(48.dp)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.white))
            )
        },
        bottomBar = {
            Surface(
                color = colorResource(id = R.color.white),
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = onNavigateToNext,
                    enabled = uiState.selectedTimeSlot != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.primaryBlue),
                        disabledContainerColor = Color.LightGray
                    )
                ) {
                    Text("Tiếp tục", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = colorResource(id = R.color.bgLight)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    uiState.doctor?.let { doctor ->
                        DoctorInfoCard(doctor)
                    }

                    // Select Date
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "Chọn ngày khám",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.textPrimary)
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(uiState.dates) { date ->
                                DateCard(
                                    date = date,
                                    isSelected = uiState.selectedDate == date,
                                    onClick = { onDateSelected(date) }
                                )
                            }
                        }
                    }

                    // Select Time Slot
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "Khung giờ có sẵn",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.textPrimary)
                        )
                        
                        // Using fixed height and LazyVerticalGrid with fixed columns
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.height(200.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            userScrollEnabled = false
                        ) {
                            items(uiState.timeSlots) { slot ->
                                TimeSlotCard(
                                    slot = slot,
                                    isSelected = uiState.selectedTimeSlot == slot,
                                    onClick = { onTimeSlotSelected(slot) }
                                )
                            }
                        }
                        
                        // Legend
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LegendItem(color = colorResource(id = R.color.primaryBlue), text = "Đang chọn", isFilled = true)
                            LegendItem(color = colorResource(id = R.color.primaryBlue), text = "Còn trống", isFilled = false)
                            LegendItem(color = Color.LightGray, text = "Kín lịch", isFilled = false, isTextGray = true)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun DoctorInfoCard(doctor: DoctorDetail) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                AsyncImage(
                    model = doctor.avatarUrl,
                    contentDescription = doctor.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = doctor.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.textPrimary)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${doctor.specialty} - ${doctor.hospital}",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.textSecondary)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(${doctor.reviewCount} đánh giá)",
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.textSecondary)
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = DividerDefaults.Thickness,
                color = colorResource(id = R.color.dividerColor)
            )

            Text(
                text = doctor.bio,
                fontSize = 14.sp,
                color = colorResource(id = R.color.textSecondary),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Đọc thêm",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.primaryBlue),
                modifier = Modifier.clickable { /* TODO */ }
            )
        }
    }
}

@Composable
fun DateCard(date: BookingDate, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) colorResource(id = R.color.primaryBlue) else colorResource(id = R.color.white)
    val contentColor = if (isSelected) colorResource(id = R.color.white) else colorResource(id = R.color.textPrimary)
    val borderColor = if (isSelected) Color.Transparent else colorResource(id = R.color.dividerColor)

    Column(
        modifier = Modifier
            .width(70.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = date.dayOfWeek, fontSize = 12.sp, color = contentColor)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = date.dateString, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = contentColor)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = date.month, fontSize = 12.sp, color = contentColor)
    }
}

@Composable
fun TimeSlotCard(slot: TimeSlot, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = when {
        isSelected -> colorResource(id = R.color.primaryBlue)
        else -> colorResource(id = R.color.white)
    }
    val contentColor = when {
        !slot.isAvailable -> Color.Gray
        isSelected -> colorResource(id = R.color.white)
        else -> colorResource(id = R.color.primaryBlue)
    }
    val borderColor = when {
        !slot.isAvailable -> Color.Transparent
        isSelected -> Color.Transparent
        else -> colorResource(id = R.color.primaryBlue)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(enabled = slot.isAvailable) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = slot.timeRange,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }
}

@Composable
fun LegendItem(color: Color, text: String, isFilled: Boolean, isTextGray: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(if (isFilled) color else Color.Transparent)
                .border(1.dp, color, RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (isTextGray) Color.Gray else colorResource(id = R.color.textSecondary)
        )
    }
}
