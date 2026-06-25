package com.example.medical.presentation.ui.doctor.patient_detail

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.medical.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun PatientDetailRoute(
    patientId: String,
    onNavigateBack: () -> Unit,
    viewModel: PatientDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(patientId) {
        viewModel.loadPatientDetail(patientId)
    }

    PatientDetailScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailScreen(
    uiState: PatientDetailUiState,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.profile_title),
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
            } else if (uiState.patientDetail != null) {
                val profile = uiState.patientDetail
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
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Avatar & Name
                            if (profile.avatarUrl.isNullOrEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .background(colorResource(id = R.color.primaryBlueLight)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = profile.fullName.take(1).uppercase(),
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(id = R.color.primaryBlue)
                                    )
                                }
                            } else {
                                AsyncImage(
                                    model = profile.avatarUrl,
                                    contentDescription = "Avatar",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = profile.fullName,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.textPrimary)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "ID: #${profile.id}",
                                fontSize = 14.sp,
                                color = colorResource(id = R.color.textSecondary),
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Section 1: Personal Info
                            InfoCard(title = stringResource(id = R.string.personal_info), icon = Icons.Default.PersonOutline) {
                                InfoRow(label = stringResource(id = R.string.full_name), value = profile.fullName)
                                HorizontalDivider(color = colorResource(id = R.color.dividerColor), modifier = Modifier.padding(vertical = 12.dp))
                                InfoRow(label = stringResource(id = R.string.dob), value = profile.dob)
                                HorizontalDivider(color = colorResource(id = R.color.dividerColor), modifier = Modifier.padding(vertical = 12.dp))
                                InfoRow(label = stringResource(id = R.string.gender), value = profile.gender)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Section 2: Contact
                            InfoCard(title = stringResource(id = R.string.contact_info), icon = Icons.Default.ContactMail) {
                                InfoRow(label = "Email", value = profile.email)
                                HorizontalDivider(color = colorResource(id = R.color.dividerColor), modifier = Modifier.padding(vertical = 12.dp))
                                InfoRow(label = stringResource(id = R.string.phone_hint), value = profile.phone)
                                HorizontalDivider(color = colorResource(id = R.color.dividerColor), modifier = Modifier.padding(vertical = 12.dp))
                                InfoRow(label = stringResource(id = R.string.address), value = profile.address)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Section 3: Medical Info
                            InfoCard(title = stringResource(id = R.string.medical_info), icon = Icons.Default.FavoriteBorder, iconTint = colorResource(id = R.color.errorRed)) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(colorResource(id = R.color.bgLight), RoundedCornerShape(8.dp))
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.blood_type),
                                        fontSize = 12.sp,
                                        color = colorResource(id = R.color.textSecondary)
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.WaterDrop,
                                            contentDescription = null,
                                            tint = colorResource(id = R.color.errorRed),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = profile.bloodType ?: "N/A",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = colorResource(id = R.color.errorRed)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(colorResource(id = R.color.bgLight), RoundedCornerShape(8.dp))
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.allergies),
                                        fontSize = 12.sp,
                                        color = colorResource(id = R.color.textSecondary)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = profile.allergies ?: "N/A",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(id = R.color.textPrimary)
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

@Composable
private fun InfoCard(
    title: String,
    icon: ImageVector,
    iconTint: Color = colorResource(id = R.color.primaryBlue),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.textPrimary)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = colorResource(id = R.color.textSecondary),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.textPrimary),
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.End
        )
    }
}
