package com.example.medical.di

import com.example.medical.data.repository.AuthRepositoryImpl
import com.example.medical.domain.repository.AuthRepository
import com.example.medical.domain.usecase.LoginUseCase
import com.example.medical.presentation.ui.auth.AuthViewModel
import com.example.medical.presentation.ui.patient.booking.BookingViewModel
import com.example.medical.presentation.ui.patient.booking_success.BookingSuccessViewModel
import com.example.medical.presentation.ui.patient.patient_home.PatientHomeViewModel
import com.example.medical.presentation.ui.patient.doctor_list.DoctorListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.medical.data.repository.AppointmentRepositoryImpl
import com.example.medical.domain.repository.AppointmentRepository
import com.example.medical.presentation.ui.patient.appointments.AppointmentsViewModel


val networkModule = module {
    // TODO: Cấu hình Retrofit, OkHttpClient ở đây
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    single<com.example.medical.domain.repository.PatientHomeRepository> { com.example.medical.data.repository.PatientHomeRepositoryImpl() }
    single<com.example.medical.domain.repository.DoctorRepository> { com.example.medical.data.repository.DoctorRepositoryImpl() }
    single<AppointmentRepository> { AppointmentRepositoryImpl() }
}

val useCaseModule = module {
    factory { LoginUseCase(get()) }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { PatientHomeViewModel(get()) }
    viewModel { DoctorListViewModel(get(), get()) }
    viewModel { BookingViewModel(get(), get()) }
    viewModel { BookingSuccessViewModel(get(), get()) }
    viewModel { AppointmentsViewModel(get()) }
}
val appModule = module {
    includes(networkModule, repositoryModule, viewModelModule, useCaseModule)
}
