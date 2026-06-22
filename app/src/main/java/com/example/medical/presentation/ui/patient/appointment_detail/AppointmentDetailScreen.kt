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

@Composable
fun AppointmentDetailRoute(
    appointmentId: String,
    onNavigateBack: () -> Unit,
    onNavigateToChangeDoctor: () -> Unit, // Optional placeholder
    onNavigateToReschedule: () -> Unit // Optional placeholder
) {
    // Hardcoded mock data to match UI design
    AppointmentDetailScreen(onNavigateBack = onNavigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailScreen(
    onNavigateBack: () -> Unit
) {
    var prescriptionAllowed by remember { mutableStateOf(true) }
    var shareResultsAllowed by remember { mutableStateOf(true) }

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
                        text = "ID: #MC-2024-89A2",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.textSecondary),
                        letterSpacing = 1.sp
                    )
                }
            }

            // Card 2: Doctor Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = "https://i.pravatar.cc/150?img=11",
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "BS. Nguyễn Văn An",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.textPrimary)
                            )
                            Text(
                                text = "TIM MẠCH",
                                fontSize = 12.sp,
                                color = colorResource(id = R.color.textSecondary),
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
                                    color = colorResource(id = R.color.textPrimary)
                                )
                                Text(
                                    text = " (120 đánh giá)",
                                    fontSize = 14.sp,
                                    color = colorResource(id = R.color.textSecondary)
                                )
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = colorResource(id = R.color.dividerColor))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedButton(
                            onClick = { /* TODO */ },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, colorResource(id = R.color.primaryBlue)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(id = R.color.primaryBlue))
                        ) {
                            Text(stringResource(id = R.string.view_profile))
                        }
                        OutlinedButton(
                            onClick = { /* TODO */ },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, colorResource(id = R.color.primaryBlue)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(id = R.color.primaryBlue))
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
                                text = "09:00 - 09:30\nThứ Ba, 25/10/2024",
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.textPrimary)
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = colorResource(id = R.color.dividerColor))

                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
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
                                text = "Khám tư vấn trực tuyến",
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.textPrimary)
                            )
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

            // Card 4: Patient details
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(id = R.string.patient_label),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.textSecondary)
                    )
                    Text(
                        text = "Trần Thị Bích",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.textPrimary)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colorResource(id = R.color.dividerColor))

                    Text(
                        text = stringResource(id = R.string.reason_for_visit_label),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.textSecondary)
                    )
                    Text(
                        text = "Tái khám định kỳ, kiểm tra huyết áp và các triệu chứng mệt mỏi gần đây.",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.textPrimary),
                        lineHeight = 20.sp
                    )
                }
            }

            // Card 5: Privacy
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = colorResource(id = R.color.textPrimary),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.privacy_and_profile),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.textPrimary)
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
                            color = colorResource(id = R.color.textPrimary),
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
                            color = colorResource(id = R.color.textPrimary),
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

            // Action Buttons
            OutlinedButton(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colorResource(id = R.color.primaryBlue)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(id = R.color.primaryBlue))
            ) {
                Text(stringResource(id = R.string.change_appointment), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            TextButton(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.cancel_appointment_action),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.errorRed)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
