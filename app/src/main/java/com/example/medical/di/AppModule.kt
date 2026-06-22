package com.example.medical.di

import com.example.medical.data.repository.AuthRepositoryImpl
import com.example.medical.domain.repository.AuthRepository
import com.example.medical.domain.usecase.LoginUseCase
import com.example.medical.presentation.ui.auth.AuthViewModel
import com.example.medical.presentation.ui.patient.booking.BookingViewModel
import com.example.medical.presentation.ui.patient.booking_success.BookingSuccessViewModel
import com.example.medical.presentation.ui.patient.patient_home.PatientHomeViewModel
import com.example.medical.presentation.ui.patient.doctor_list.DoctorListViewModel
import com.example.medical.presentation.ui.auth.RegisterViewModel
import com.example.medical.domain.usecase.RegisterUseCase
import com.example.medical.presentation.ui.doctor.home.DoctorHomeViewModel
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
    single<com.example.medical.domain.repository.ProfileRepository> { com.example.medical.data.repository.ProfileRepositoryImpl() }
    single<com.example.medical.domain.repository.NotificationRepository> { com.example.medical.data.repository.NotificationRepositoryImpl() }
    single<com.example.medical.domain.repository.DoctorHomeRepository> { com.example.medical.data.repository.DoctorHomeRepositoryImpl() }
    single<com.example.medical.domain.repository.DoctorAppointmentRepository> { com.example.medical.data.repository.DoctorAppointmentRepositoryImpl() }
    single<com.example.medical.domain.repository.DoctorNotificationRepository> { com.example.medical.data.repository.DoctorNotificationRepositoryImpl() }
    single<com.example.medical.domain.repository.DoctorProfileRepository> { com.example.medical.data.repository.DoctorProfileRepositoryImpl() }
}

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { com.example.medical.domain.usecase.GetDoctorHomeDataUseCase(get()) }
    factory { com.example.medical.domain.usecase.GetDoctorAppointmentsUseCase(get()) }
    factory { com.example.medical.domain.usecase.GetNotificationsUseCase(get()) }
    factory { com.example.medical.domain.usecase.MarkAllNotificationsAsReadUseCase(get()) }
    factory { com.example.medical.domain.usecase.ConfirmAppointmentUseCase(get()) }
    factory { com.example.medical.domain.usecase.RejectAppointmentUseCase(get()) }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { com.example.medical.presentation.ui.doctor.home.DoctorHomeViewModel(get()) }
    viewModel { com.example.medical.presentation.ui.doctor.appointment.DoctorAppointmentViewModel(get()) }
    viewModel { com.example.medical.presentation.ui.doctor.notification.DoctorNotificationViewModel(get(), get(), get(), get()) }
    viewModel { com.example.medical.presentation.ui.doctor.profile.DoctorProfileViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { PatientHomeViewModel(get()) }
    viewModel { DoctorListViewModel(get(), get()) }
    viewModel { BookingViewModel(get(), get()) }
    viewModel { BookingSuccessViewModel(get(), get()) }
    viewModel { AppointmentsViewModel(get()) }
    viewModel { com.example.medical.presentation.ui.patient.profile.ProfileViewModel(get()) }
    viewModel { com.example.medical.presentation.ui.patient.notifications.NotificationsViewModel(get()) }
}
val appModule = module {
    includes(networkModule, repositoryModule, viewModelModule, useCaseModule)
}
