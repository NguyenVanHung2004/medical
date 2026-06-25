package com.example.medical.presentation.ui.auth.forgot_password

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medical.R
import com.example.medical.presentation.theme.MedicalAppTheme
import org.koin.androidx.compose.koinViewModel
import com.example.medical.presentation.ui.common.MedicalTextField
import com.example.medical.presentation.ui.common.PrimaryButton
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordStep1Route(
    viewModel: ForgotPasswordViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onNavigateNext: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isStep1Success) {
        if (uiState.isStep1Success) {
            viewModel.resetNavigation()
            onNavigateNext()
        }
    }

    ForgotPasswordStep1Screen(
        uiState = uiState,
        onEmailOrPhoneChange = viewModel::onEmailOrPhoneChange,
        onBackClick = onBackClick,
        onSubmit = viewModel::submitStep1
    )
}

@Composable
fun ForgotPasswordStep1Screen(
    uiState: ForgotPasswordUiState,
    onEmailOrPhoneChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSubmit: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = stringResource(id = R.string.forgot_password_title),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = stringResource(id = R.string.forgot_password_desc),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))

            MedicalTextField(
                value = uiState.emailOrPhone,
                onValueChange = onEmailOrPhoneChange,
                label = stringResource(id = R.string.email_or_phone_hint),
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = stringResource(id = R.string.continue_button),
                onClick = onSubmit,
                isLoading = uiState.isLoading
            )

            AnimatedVisibility(visible = uiState.errorMessage != null) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordStep2Route(
    viewModel: ForgotPasswordViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onNavigateNext: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isStep2Success) {
        if (uiState.isStep2Success) {
            viewModel.resetNavigation()
            onNavigateNext()
        }
    }

    ForgotPasswordStep2Screen(
        uiState = uiState,
        onOtpChange = viewModel::onOtpChange,
        onBackClick = onBackClick,
        onSubmit = viewModel::submitStep2
    )
}

@Composable
fun ForgotPasswordStep2Screen(
    uiState: ForgotPasswordUiState,
    onOtpChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSubmit: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = stringResource(id = R.string.enter_otp_title),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "${stringResource(id = R.string.enter_otp_desc)}${uiState.emailOrPhone}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))

            MedicalTextField(
                value = uiState.otp,
                onValueChange = onOtpChange,
                label = stringResource(id = R.string.otp_hint),
                leadingIcon = Icons.Default.Lock,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = stringResource(id = R.string.verify_button),
                onClick = onSubmit,
                isLoading = uiState.isLoading
            )

            AnimatedVisibility(visible = uiState.errorMessage != null) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordStep3Route(
    viewModel: ForgotPasswordViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onNavigateSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ForgotPasswordStep3Screen(
        uiState = uiState,
        onNewPasswordChange = viewModel::onNewPasswordChange,
        onPasswordVisibilityChange = viewModel::onPasswordVisibilityChange,
        onBackClick = onBackClick,
        onSubmit = viewModel::submitStep3,
        onNavigateSuccess = onNavigateSuccess,
        onResetState = viewModel::resetState
    )
}

@Composable
fun ForgotPasswordStep3Screen(
    uiState: ForgotPasswordUiState,
    onNewPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onSubmit: () -> Unit,
    onNavigateSuccess: () -> Unit,
    onResetState: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = stringResource(id = R.string.reset_password_title),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = stringResource(id = R.string.reset_password_desc),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))

            MedicalTextField(
                value = uiState.newPassword,
                onValueChange = onNewPasswordChange,
                label = stringResource(id = R.string.new_password_hint),
                leadingIcon = Icons.Default.Lock,
                trailingIcon = if (uiState.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                onTrailingIconClick = { onPasswordVisibilityChange(!uiState.passwordVisible) },
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = stringResource(id = R.string.reset_password_button),
                onClick = onSubmit,
                isLoading = uiState.isLoading
            )

            AnimatedVisibility(visible = uiState.errorMessage != null) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        if (uiState.isStep3Success) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = stringResource(id = R.string.reset_success_title), style = MaterialTheme.typography.titleLarge) },
                text = { Text(text = stringResource(id = R.string.reset_success_desc), style = MaterialTheme.typography.bodyLarge) },
                confirmButton = {
                    Button(onClick = {
                        onResetState()
                        onNavigateSuccess()
                    }) {
                        Text(stringResource(id = R.string.close))
                    }
                }
            )
        }
    }
}

