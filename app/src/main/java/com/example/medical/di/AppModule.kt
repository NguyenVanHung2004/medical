package com.example.medical.di

import com.example.medical.data.repository.AuthRepositoryImpl
import com.example.medical.domain.repository.AuthRepository
import com.example.medical.domain.usecase.LoginUseCase
import com.example.medical.presentation.ui.auth.AuthViewModel
import com.example.medical.presentation.ui.doctor.home.DoctorHomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    // TODO: Cấu hình Retrofit, OkHttpClient ở đây
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    single<com.example.medical.domain.repository.DoctorHomeRepository> { com.example.medical.data.repository.DoctorHomeRepositoryImpl() }
    single<com.example.medical.domain.repository.DoctorAppointmentRepository> { com.example.medical.data.repository.DoctorAppointmentRepositoryImpl() }
}

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { com.example.medical.domain.usecase.GetDoctorHomeDataUseCase(get()) }
    factory { com.example.medical.domain.usecase.GetDoctorAppointmentsUseCase(get()) }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { com.example.medical.presentation.ui.doctor.home.DoctorHomeViewModel(get()) }
    viewModel { com.example.medical.presentation.ui.doctor.appointment.DoctorAppointmentViewModel(get()) }
}

val appModule = module {
    includes(networkModule, repositoryModule, viewModelModule, useCaseModule)
}
