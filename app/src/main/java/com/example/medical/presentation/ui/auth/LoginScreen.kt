package com.example.medical.presentation.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medical.R
import com.example.medical.domain.model.Result
import com.example.medical.domain.model.User
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRoute(
    viewModel: AuthViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LoginScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onPasswordVisibilityChange = viewModel::onPasswordVisibilityChange,
        onLoginClick = viewModel::login,
        onLoginSuccess = onLoginSuccess,
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
    onLoginClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onResetState: () -> Unit
) {


    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Premium Header
            Text(
                text = "k",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Vui lòng tục",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(48.dp))

            // Email Field
            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = { Text(stringResource(id = R.string.email_hint)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email, 
                        contentDescription = "Email Icon", 
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password Field
            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = { Text(stringResource(id = R.string.password_hint)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock, 
                        contentDescription = "Lock Icon", 
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    val image = if (uiState.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { onPasswordVisibilityChange(!uiState.passwordVisible) }) {
                        Icon(imageVector = image, contentDescription = "Toggle Password", tint = MaterialTheme.colorScheme.primary.copy(alpha=0.7f))
                    }
                },
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Forgot Password
            Text(
                text = stringResource(id = R.string.forgot_password),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { /* Handle forgot password */ }
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Solid Primary Login Button
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(),
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp), 
                        color = Color.White, 
                        strokeWidth = 3.dp
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.login_button),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    )
                }
            }

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

            Spacer(modifier = Modifier.height(48.dp))

            // Social Login Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                Text(
                    text = "Hoặc đăng nhập với",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Google Login Button
            OutlinedButton(
                onClick = { /* TODO: Implement Google Login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            ) {
                // You can replace the text "G" with an actual Google logo using an Image composable later
                Text(
                    text = "G",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color(0xFFDB4437) // Google Red
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Đăng nhập bằng Google",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
        
        LaunchedEffect(uiState.isSuccess) {
            if (uiState.isSuccess) {
                onLoginSuccess()
                onResetState()
            }
        }
    }
}
