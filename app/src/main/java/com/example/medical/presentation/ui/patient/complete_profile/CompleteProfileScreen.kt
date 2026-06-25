package com.example.medical.presentation.ui.patient.complete_profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medical.R
import org.koin.androidx.compose.koinViewModel
import com.example.medical.presentation.ui.common.MedicalTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CompleteProfileRoute(
    viewModel: CompleteProfileViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateNext()
        }
    }

    CompleteProfileScreen(
        uiState = uiState,
        onFullNameChange = viewModel::onFullNameChange,
        onDateOfBirthChange = viewModel::onDateOfBirthChange,
        onGenderChange = viewModel::onGenderChange,
        onAddressChange = viewModel::onAddressChange,
        onInsuranceEnabledChange = viewModel::onInsuranceEnabledChange,
        onInsuranceProviderChange = viewModel::onInsuranceProviderChange,
        onInsuranceCodeChange = viewModel::onInsuranceCodeChange,
        onAgreementChange = viewModel::onAgreementChange,
        onSubmit = viewModel::submitProfile,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun CompleteProfileScreen(
    uiState: CompleteProfileUiState,
    onFullNameChange: (String) -> Unit,
    onDateOfBirthChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onInsuranceEnabledChange: (Boolean) -> Unit,
    onInsuranceProviderChange: (String) -> Unit,
    onInsuranceCodeChange: (String) -> Unit,
    onAgreementChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val backgroundColor = MaterialTheme.colorScheme.background

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var showTermsDialog by remember { mutableStateOf(false) }

    val fullNameRequester = remember { BringIntoViewRequester() }
    val dobRequester = remember { BringIntoViewRequester() }
    val genderRequester = remember { BringIntoViewRequester() }
    val addressRequester = remember { BringIntoViewRequester() }
    val insuranceProviderRequester = remember { BringIntoViewRequester() }
    val insuranceCodeRequester = remember { BringIntoViewRequester() }
    val agreementRequester = remember { BringIntoViewRequester() }

    val isFormComplete = uiState.fullName.isNotBlank() && 
            uiState.dateOfBirth.isNotBlank() && 
            uiState.gender.isNotBlank() && 
            uiState.address.isNotBlank() && 
            (!uiState.isInsuranceEnabled || (uiState.insuranceProvider.isNotBlank() && uiState.insuranceCode.isNotBlank())) && 
            uiState.isAgreedToPolicy

    LaunchedEffect(uiState.invalidFields) {
        if (uiState.invalidFields.isNotEmpty()) {
            when {
                "fullName" in uiState.invalidFields -> fullNameRequester.bringIntoView()
                "dateOfBirth" in uiState.invalidFields -> dobRequester.bringIntoView()
                "gender" in uiState.invalidFields -> genderRequester.bringIntoView()
                "address" in uiState.invalidFields -> addressRequester.bringIntoView()
                "insuranceProvider" in uiState.invalidFields -> insuranceProviderRequester.bringIntoView()
                "insuranceCode" in uiState.invalidFields -> insuranceCodeRequester.bringIntoView()
                "agreement" in uiState.invalidFields -> agreementRequester.bringIntoView()
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(millis))
                        onDateOfBirthChange(date)
                    }
                    showDatePicker = false
                }) {
                    Text(stringResource(id = R.string.complete_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(id = R.string.close))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = {
                Text(
                    text = stringResource(id = R.string.terms_dialog_title),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = stringResource(id = R.string.terms_dialog_content),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text(stringResource(id = R.string.close))
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.complete_profile_title),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onSubmit,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFormComplete) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = stringResource(id = R.string.complete_button),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(innerPadding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.personal_info_section),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    
                    Box(modifier = Modifier.bringIntoViewRequester(fullNameRequester)) {
                        MedicalTextField(
                            label = stringResource(id = R.string.full_name_hint),
                            value = uiState.fullName,
                            onValueChange = onFullNameChange,
                            leadingIcon = Icons.Default.Person,
                            isError = "fullName" in uiState.invalidFields
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Box(modifier = Modifier.bringIntoViewRequester(dobRequester)) {
                        MedicalTextField(
                            label = stringResource(id = R.string.dob_hint),
                            value = uiState.dateOfBirth,
                            onValueChange = {},
                            leadingIcon = Icons.Default.CalendarToday,
                            isError = "dateOfBirth" in uiState.invalidFields,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default
                        )
                        // Invisible overlay to intercept clicks
                        Box(modifier = Modifier.matchParentSize().clickable { showDatePicker = true })
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    var expanded by remember { mutableStateOf(false) }
                    
                    Column(modifier = Modifier.fillMaxWidth().bringIntoViewRequester(genderRequester)) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = uiState.gender,
                                onValueChange = {},
                                placeholder = { Text(stringResource(id = R.string.select_gender), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                enabled = false,
                                isError = "gender" in uiState.invalidFields,
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Dropdown",
                                        tint = if ("gender" in uiState.invalidFields) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = if ("gender" in uiState.invalidFields) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                                    disabledBorderColor = if ("gender" in uiState.invalidFields) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                                )
                            )
                            Box(modifier = Modifier.matchParentSize().clickable { expanded = true })
                            
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.fillMaxWidth(0.85f).background(Color.White)
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(id = R.string.gender_male)) },
                                    onClick = { onGenderChange("Nam"); expanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(id = R.string.gender_female)) },
                                    onClick = { onGenderChange("Nữ"); expanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(id = R.string.gender_other)) },
                                    onClick = { onGenderChange("Khác"); expanded = false }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Box(modifier = Modifier.bringIntoViewRequester(addressRequester)) {
                        MedicalTextField(
                            label = stringResource(id = R.string.address_hint),
                            value = uiState.address,
                            onValueChange = onAddressChange,
                            leadingIcon = Icons.Default.LocationOn,
                            isError = "address" in uiState.invalidFields
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (uiState.isInsuranceEnabled) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else Color(0xFFF5F5F5))
                            .clickable { onInsuranceEnabledChange(!uiState.isInsuranceEnabled) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Thông tin bảo hiểm",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "(Tuỳ chọn)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Switch(
                            checked = uiState.isInsuranceEnabled,
                            onCheckedChange = { onInsuranceEnabledChange(it) }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    AnimatedVisibility(visible = uiState.isInsuranceEnabled) {
                        Column {
                            Box(modifier = Modifier.bringIntoViewRequester(insuranceProviderRequester)) {
                                MedicalTextField(
                                    label = stringResource(id = R.string.insurance_provider_hint),
                                    value = uiState.insuranceProvider,
                                    onValueChange = onInsuranceProviderChange,
                                    leadingIcon = Icons.Default.Business,
                                    isError = "insuranceProvider" in uiState.invalidFields
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Box(modifier = Modifier.bringIntoViewRequester(insuranceCodeRequester)) {
                                MedicalTextField(
                                    label = stringResource(id = R.string.insurance_code_hint),
                                    value = uiState.insuranceCode,
                                    onValueChange = onInsuranceCodeChange,
                                    leadingIcon = Icons.Default.Numbers,
                                    isError = "insuranceCode" in uiState.invalidFields
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .bringIntoViewRequester(agreementRequester)
                            .clickable { showTermsDialog = true },
                        verticalAlignment = Alignment.Top
                    ) {
                        Checkbox(
                            checked = uiState.isAgreedToPolicy,
                            onCheckedChange = { onAgreementChange(it) },
                            modifier = Modifier.padding(end = 8.dp),
                            colors = CheckboxDefaults.colors(
                                uncheckedColor = if ("agreement" in uiState.invalidFields) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        
                        Text(
                            text = stringResource(id = R.string.agree_policy),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = if ("agreement" in uiState.invalidFields) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    AnimatedVisibility(visible = uiState.errorMessage != null) {
                        Text(
                            text = uiState.errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
