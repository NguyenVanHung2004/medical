package com.example.medical.presentation.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import com.example.medical.presentation.ui.common.MedicalTextField
import com.example.medical.presentation.ui.common.PrimaryButton
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import com.example.medical.presentation.theme.GoogleRed

@Composable
fun RegisterRoute(
    viewModel: RegisterViewModel = koinViewModel(),
    isDoctor: Boolean,
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(isDoctor) {
        viewModel.onDoctorRoleChange(isDoctor)
    }

    RegisterScreen(
        uiState = uiState,
        onTabChange = viewModel::onTabChange,
        onEmailChange = viewModel::onEmailChange,
        onPhoneChange = viewModel::onPhoneChange,
        onOtpChange = viewModel::onOtpChange,
        onPasswordChange = viewModel::onPasswordChange,
        onPasswordVisibilityChange = viewModel::onPasswordVisibilityChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onConfirmPasswordVisibilityChange = viewModel::onConfirmPasswordVisibilityChange,
        onDoctorRoleChange = viewModel::onDoctorRoleChange,
        onGoogleLoginSuccess = viewModel::onGoogleLoginSuccess,
        onRegisterClick = viewModel::register,
        onBackClick = onBackClick,
        onLoginClick = onLoginClick,
        onRegisterSuccess = onRegisterSuccess,
        onResetState = viewModel::resetState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onTabChange: (Int) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onOtpChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onConfirmPasswordVisibilityChange: (Boolean) -> Unit,
    onDoctorRoleChange: (Boolean) -> Unit,
    onGoogleLoginSuccess: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onResetState: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showOtpDialog by remember { mutableStateOf(false) }
    var hasRequestedOtp by remember { mutableStateOf(false) }
    var blinkTrigger by remember { mutableStateOf(0) }
    var isBlinking by remember { mutableStateOf(false) }

    LaunchedEffect(blinkTrigger) {
        if (blinkTrigger > 0) {
            repeat(3) {
                isBlinking = true
                kotlinx.coroutines.delay(150)
                isBlinking = false
                kotlinx.coroutines.delay(150)
            }
        }
    }

    if (showOtpDialog) {
        AlertDialog(
            onDismissRequest = { showOtpDialog = false },
            title = { Text(stringResource(id = R.string.otp_mock_dialog_title)) },
            text = { Text(stringResource(id = R.string.otp_mock_dialog_message)) },
            confirmButton = {
                TextButton(onClick = { showOtpDialog = false }) {
                    Text(stringResource(id = R.string.close))
                }
            }
        )
    }

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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 24.dp)
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
                    text = stringResource(id = R.string.create_new_account),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            Image(
                painter = painterResource(
                    id = if (uiState.isDoctor) R.drawable.doctor_icon else R.drawable.patient_icon
                ),
                contentDescription = "Role Icon",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(4.dp)
                        .background(
                            color = if (uiState.selectedTab == 0) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onTabChange(0) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.email_hint),
                        color = if (uiState.selectedTab == 0) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(4.dp)
                        .background(
                            color = if (uiState.selectedTab == 1) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onTabChange(1) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.phone_hint),
                        color = if (uiState.selectedTab == 1) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.selectedTab == 0) {
                MedicalTextField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    label = stringResource(id = R.string.email_address),
                    leadingIcon = Icons.Outlined.Email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(12.dp))

                MedicalTextField(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    label = stringResource(id = R.string.password_hint),
                    leadingIcon = Icons.Outlined.Lock,
                    trailingIcon = if (uiState.passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                    onTrailingIconClick = { onPasswordVisibilityChange(!uiState.passwordVisible) },
                    visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(12.dp))

                MedicalTextField(
                    value = uiState.confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = stringResource(id = R.string.confirm_password_hint),
                    leadingIcon = Icons.Outlined.Lock,
                    trailingIcon = if (uiState.confirmPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                    onTrailingIconClick = { onConfirmPasswordVisibilityChange(!uiState.confirmPasswordVisible) },
                    visualTransformation = if (uiState.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(id = R.string.password_requirement_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            } else {
                MedicalTextField(
                    value = uiState.phone,
                    onValueChange = onPhoneChange,
                    label = stringResource(id = R.string.phone_hint),
                    leadingIcon = Icons.Outlined.Info,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.otp,
                    onValueChange = onOtpChange,
                    label = { Text(stringResource(id = R.string.otp_hint)) },
                    placeholder = { Text(stringResource(id = R.string.enter_otp_placeholder), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "OTP",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            VerticalDivider(
                                modifier = Modifier.height(24.dp).padding(horizontal = 4.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                            )
                            val color = if (isBlinking) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            TextButton(onClick = { 
                                showOtpDialog = true 
                                hasRequestedOtp = true
                            }) {
                                Text(stringResource(id = R.string.get_code), color = color)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = stringResource(id = R.string.register_button),
                onClick = {
                    if (uiState.selectedTab == 1 && !hasRequestedOtp) {
                        blinkTrigger++
                        android.widget.Toast.makeText(context, context.getString(R.string.press_get_code), android.widget.Toast.LENGTH_SHORT).show()
                    } else {
                        onRegisterClick()
                    }
                },
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

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                Text(
                    text = stringResource(id = R.string.or_register_with),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { 
                    scope.launch {
                        val token = GoogleAuthHelper.doGoogleLogin(context)
                        if (token != null) {
                            onGoogleLoginSuccess(token)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            ) {
                Text(
                    text = "G",
                    style = MaterialTheme.typography.titleLarge,
                    color = GoogleRed
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(id = R.string.register_with_google),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.already_have_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                TextButton(onClick = onLoginClick, contentPadding = PaddingValues(horizontal = 8.dp)) {
                    Text(
                        text = stringResource(id = R.string.login_now),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        LaunchedEffect(uiState.isSuccess) {
            if (uiState.isSuccess) {
                onRegisterSuccess()
                onResetState()
            }
        }
    }
}

@Composable
fun SocialButton(text: String, color: Color, onClick: () -> Unit = {}) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.size(56.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = color
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    MedicalAppTheme {
        RegisterScreen(
            uiState = RegisterUiState(),
            onTabChange = {},
            onEmailChange = {},
            onPhoneChange = {},
            onOtpChange = {},
            onPasswordChange = {},
            onPasswordVisibilityChange = {},
            onConfirmPasswordChange = {},
            onConfirmPasswordVisibilityChange = {},
            onRegisterClick = {},
            onBackClick = {},
            onLoginClick = {},
            onRegisterSuccess = {},
            onResetState = {},
            onGoogleLoginSuccess = {},
            onDoctorRoleChange = {}
        )
    }
}

