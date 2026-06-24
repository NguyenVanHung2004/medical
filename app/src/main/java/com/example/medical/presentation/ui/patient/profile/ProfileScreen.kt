package com.example.medical.presentation.ui.patient.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.medical.R
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.BorderStroke
import com.example.medical.presentation.ui.common.SecondaryButton

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = koinViewModel(),
    onLogout: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    ProfileScreen(uiState = uiState, onLogout = onLogout)
}

@Composable
fun ProfileScreen(uiState: ProfileUiState, onLogout: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgLight))
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocalHospital,
                contentDescription = "Logo",
                tint = colorResource(id = R.color.primaryBlue),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "MediConnect",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.primaryBlue)
            )
        }

        uiState.profile?.let { profile ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar & Name
                AsyncImage(
                    model = profile.avatarUrl,
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = profile.fullName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.textPrimary)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { /* TODO */ },
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, colorResource(id = R.color.primaryBlue)),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = colorResource(id = R.color.primaryBlue),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.edit_profile),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.primaryBlue)
                    )
                }

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

                Spacer(modifier = Modifier.height(24.dp))

                // Settings Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(id = R.string.settings_header),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.textSecondary)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Settings Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        SettingsRow(
                            icon = Icons.Default.NotificationsNone,
                            title = stringResource(id = R.string.notification_settings)
                        )
                        HorizontalDivider(color = colorResource(id = R.color.dividerColor))
                        SettingsRow(
                            icon = Icons.Default.Language,
                            title = stringResource(id = R.string.language),
                            value = stringResource(id = R.string.language_vietnamese)
                        )
                        HorizontalDivider(color = colorResource(id = R.color.dividerColor))
                        SettingsRow(
                            icon = Icons.Default.Security,
                            title = stringResource(id = R.string.privacy_settings)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Logout Button
                SecondaryButton(
                    text = stringResource(id = R.string.logout),
                    onClick = onLogout,
                    icon = Icons.AutoMirrored.Filled.Logout,
                    color = colorResource(id = R.color.errorRed)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Delete Account
                Text(
                    text = stringResource(id = R.string.delete_account),
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.textSecondary),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { /* TODO */ }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
fun InfoRow(label: String, value: String) {
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

@Composable
fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorResource(id = R.color.primaryBlue),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            color = colorResource(id = R.color.textPrimary),
            modifier = Modifier.weight(1f)
        )
        if (value != null) {
            Text(
                text = value,
                fontSize = 12.sp,
                color = colorResource(id = R.color.textSecondary)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = colorResource(id = R.color.textSecondary),
            modifier = Modifier.size(20.dp)
        )
    }
}

