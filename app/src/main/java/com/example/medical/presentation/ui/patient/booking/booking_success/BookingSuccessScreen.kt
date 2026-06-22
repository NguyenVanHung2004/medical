package com.example.medical.presentation.ui.patient.booking_success

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medical.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookingSuccessRoute(
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit,
    viewModel: BookingSuccessViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    BookingSuccessScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNavigateHome = onNavigateHome
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSuccessScreen(
    uiState: BookingSuccessUiState,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit
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
        containerColor = colorResource(id = R.color.bgLight)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Success Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)) // Light green
                    .border(3.dp, Color(0xFF2E7D32), CircleShape), // Dark green
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title and Subtitle
            Text(
                text = "Đặt lịch thành công!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.textPrimary)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Cảm ơn bạn. Lịch hẹn của bạn đã được xác nhận.",
                fontSize = 16.sp,
                color = colorResource(id = R.color.textSecondary),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Booking Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MÃ THAM CHIẾU",
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.textSecondary),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = uiState.referenceCode,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.primaryBlue)
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        thickness = DividerDefaults.Thickness, color = colorResource(id = R.color.dividerColor)
                    )

                    // Doctor Info
                    DetailRow(
                        icon = Icons.Default.Person,
                        text = uiState.doctor?.let { "${it.name} - ${it.specialty}" } ?: "Đang tải..."
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Date & Time Info
                    DetailRow(
                        icon = Icons.Default.CalendarToday,
                        text = "${uiState.date} lúc ${uiState.time}"
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Location Info
                    DetailRow(
                        icon = Icons.Default.LocationOn,
                        text = uiState.location
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Buttons
            OutlinedButton(
                onClick = { /* TODO: Add to calendar */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, colorResource(id = R.color.primaryBlue)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(id = R.color.primaryBlue))
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Thêm vào lịch", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onNavigateHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Trở về trang chủ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.primaryBlue)
                )
            }
        }
    }
}

@Composable
fun DetailRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorResource(id = R.color.textSecondary),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 15.sp,
            color = colorResource(id = R.color.textPrimary)
        )
    }
}
