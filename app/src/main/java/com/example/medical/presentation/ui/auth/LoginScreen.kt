package com.example.medical.presentation.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medical.R
import com.example.medical.domain.model.Result
import com.example.medical.domain.model.User
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import com.example.medical.presentation.theme.GoogleRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRoute(
    viewModel: AuthViewModel = koinViewModel(),
    isDoctor: Boolean,
    onBackClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(isDoctor) {
        viewModel.onDoctorRoleChange(isDoctor)
    }

    LoginScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onPasswordVisibilityChange = viewModel::onPasswordVisibilityChange,
        onDoctorRoleChange = viewModel::onDoctorRoleChange,
        onGoogleLoginSuccess = viewModel::onGoogleLoginSuccess,
        onBackClick = onBackClick,
        onLoginClick = viewModel::login,
        onLoginSuccess = onLoginSuccess,
        onRegisterClick = onRegisterClick,
        onForgotPasswordClick = onForgotPasswordClick,
        onResetState = viewModel::resetState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    onDoctorRoleChange: (Boolean) -> Unit,
    onGoogleLoginSuccess: (String) -> Unit,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onResetState: () -> Unit
) {


    val backgroundColor = MaterialTheme.colorScheme.background
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
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
                    text = stringResource(id = if (uiState.isDoctor) R.string.for_doctor else R.string.for_patient),
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
            
            Spacer(modifier = Modifier.height(20.dp))

            MedicalTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = stringResource(id = R.string.email_hint),
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(10.dp))

            MedicalTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = stringResource(id = R.string.password_hint),
                leadingIcon = Icons.Default.Lock,
                trailingIcon = if (uiState.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                onTrailingIconClick = { onPasswordVisibilityChange(!uiState.passwordVisible) },
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(12.dp))

            var rememberMe by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { rememberMe = !rememberMe }
                        .padding(end = 8.dp, top = 4.dp, bottom = 4.dp)
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.remember_me),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = stringResource(id = R.string.forgot_password),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onForgotPasswordClick() }
                        .padding(vertical = 8.dp, horizontal = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            PrimaryButton(
                text = stringResource(id = R.string.login_button),
                onClick = onLoginClick,
                isLoading = uiState.isLoading
            )

            AnimatedVisibility(visible = uiState.errorMessage != null) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                Text(
                    text = stringResource(id = R.string.or_login_with),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            }
            
            Spacer(modifier = Modifier.height(18.dp))
            
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
                    text = stringResource(id = R.string.login_with_google),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.no_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                TextButton(onClick = onRegisterClick, contentPadding = PaddingValues(horizontal = 8.dp)) {
                    Text(
                        text = stringResource(id = if (uiState.isDoctor) R.string.register_now_doctor else R.string.register_now_patient),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        if (uiState.isSuccess) {
            val title = stringResource(id = if (uiState.isDoctor) R.string.login_doctor_success else R.string.login_patient_success)
            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
                text = { Text(text = stringResource(id = R.string.welcome_back_message), style = MaterialTheme.typography.bodyLarge) },
                confirmButton = {
                    Button(onClick = {
                        onLoginSuccess()
                        onResetState()
                    }) {
                        Text(stringResource(id = R.string.continue_text))
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    MedicalAppTheme {
        LoginScreen(
            uiState = LoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordVisibilityChange = {},
            onDoctorRoleChange = {},
            onGoogleLoginSuccess = {},
            onBackClick = {},
            onLoginClick = {},
            onLoginSuccess = {},
            onRegisterClick = {},
            onForgotPasswordClick = {},
            onResetState = {}
        )
    }
}

@Preview
@Composable
fun LoginScreenDoctorPreview() {
    MedicalAppTheme {
        LoginScreen(
            uiState = LoginUiState(isDoctor = true),
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordVisibilityChange = {},
            onDoctorRoleChange = {},
            onGoogleLoginSuccess = {},
            onBackClick = {},
            onLoginClick = {},
            onLoginSuccess = {},
            onRegisterClick = {},
            onForgotPasswordClick = {},
            onResetState = {}
        )
    }
}