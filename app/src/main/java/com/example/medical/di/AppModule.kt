package com.example.medical.di

import org.koin.android.ext.koin.androidContext

import com.example.medical.data.local.TokenManager
import com.example.medical.data.remote.ApiService
import com.example.medical.data.remote.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
import com.example.medical.presentation.ui.auth.forgot_password.ForgotPasswordViewModel
import com.example.medical.presentation.ui.patient.complete_profile.CompleteProfileViewModel
import com.example.medical.presentation.ui.doctor.home.DoctorHomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.medical.data.repository.AppointmentRepositoryImpl
import com.example.medical.data.repository.DoctorAppointmentRepositoryImpl
import com.example.medical.data.repository.DoctorHomeRepositoryImpl
import com.example.medical.data.repository.DoctorNotificationRepositoryImpl
import com.example.medical.data.repository.DoctorProfileRepositoryImpl
import com.example.medical.data.repository.DoctorRepositoryImpl
import com.example.medical.data.repository.NotificationRepositoryImpl
import com.example.medical.data.repository.PatientHomeRepositoryImpl
import com.example.medical.data.repository.ProfileRepositoryImpl
import com.example.medical.domain.repository.AppointmentRepository
import com.example.medical.domain.repository.DoctorAppointmentRepository
import com.example.medical.domain.repository.DoctorHomeRepository
import com.example.medical.domain.repository.DoctorNotificationRepository
import com.example.medical.domain.repository.DoctorProfileRepository
import com.example.medical.domain.repository.DoctorRepository
import com.example.medical.domain.repository.NotificationRepository
import com.example.medical.domain.repository.PatientHomeRepository
import com.example.medical.domain.repository.ProfileRepository
import com.example.medical.domain.usecase.FilterDoctorsUseCase
import com.example.medical.presentation.ui.patient.appointments.AppointmentsViewModel
import com.example.medical.domain.usecase.appointment.GetUpcomingAppointmentsUseCase
import com.example.medical.domain.usecase.appointment.GetHistoryAppointmentsUseCase
import com.example.medical.domain.usecase.appointment.CancelAppointmentUseCase
import com.example.medical.domain.usecase.appointment.RescheduleAppointmentUseCase
import com.example.medical.domain.usecase.appointment.BookAppointmentUseCase
import com.example.medical.domain.usecase.appointment.GetAppointmentByIdUseCase
import com.example.medical.domain.usecase.doctor.ConfirmAppointmentUseCase
import com.example.medical.domain.usecase.doctor.GetDoctorAppointmentDetailUseCase
import com.example.medical.domain.usecase.doctor.GetDoctorAppointmentsUseCase
import com.example.medical.domain.usecase.doctor.GetDoctorHomeDataUseCase
import com.example.medical.domain.usecase.doctor.GetNotificationsUseCase
import com.example.medical.domain.usecase.doctor.MarkAllNotificationsAsReadUseCase
import com.example.medical.domain.usecase.doctor.RejectAppointmentUseCase
import com.example.medical.presentation.ui.doctor.appointment.DoctorAppointmentViewModel
import com.example.medical.presentation.ui.doctor.appointment_detail.DoctorAppointmentDetailViewModel
import com.example.medical.presentation.ui.doctor.notification.DoctorNotificationViewModel
import com.example.medical.presentation.ui.doctor.profile.DoctorProfileViewModel
import com.example.medical.presentation.ui.patient.appointment_detail.AppointmentDetailViewModel
import com.example.medical.presentation.ui.patient.notifications.NotificationsViewModel
import com.example.medical.presentation.ui.patient.profile.ProfileViewModel

val networkModule = module {
    single { TokenManager(androidContext()) }

    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val authInterceptor = AuthInterceptor(get())
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://medical-server.zeabur.app/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(ApiService::class.java) }
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<PatientHomeRepository> { PatientHomeRepositoryImpl(get()) }
    single<DoctorRepository> { DoctorRepositoryImpl(get()) }
    single<AppointmentRepository> { AppointmentRepositoryImpl(get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
    single<NotificationRepository> { NotificationRepositoryImpl() }
    single<DoctorHomeRepository> { DoctorHomeRepositoryImpl(get()) }
    single<DoctorAppointmentRepository> { DoctorAppointmentRepositoryImpl(get()) }
    single<DoctorNotificationRepository> { DoctorNotificationRepositoryImpl() }
    single<DoctorProfileRepository> { DoctorProfileRepositoryImpl(get()) }
}

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }

    factory { GetUpcomingAppointmentsUseCase(get()) }
    factory { GetHistoryAppointmentsUseCase(get()) }
    factory { CancelAppointmentUseCase(get()) }
    factory { RescheduleAppointmentUseCase(get()) }
    factory { BookAppointmentUseCase(get()) }
    factory { GetAppointmentByIdUseCase(get()) }
    factory { FilterDoctorsUseCase() }
    factory { GetDoctorHomeDataUseCase(get()) }
    factory { GetDoctorAppointmentsUseCase(get()) }
    factory { GetNotificationsUseCase(get()) }
    factory { MarkAllNotificationsAsReadUseCase(get()) }
    factory { ConfirmAppointmentUseCase(get()) }
    factory { RejectAppointmentUseCase(get()) }
    factory { GetDoctorAppointmentDetailUseCase(get()) }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { DoctorHomeViewModel(get()) }
    viewModel { DoctorAppointmentViewModel(get()) }
    viewModel { DoctorNotificationViewModel(get(), get(), get(), get()) }
    viewModel { DoctorProfileViewModel(get()) }
    viewModel { DoctorAppointmentDetailViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { PatientHomeViewModel(get()) }
    viewModel { DoctorListViewModel(get(), get(), get()) }
    viewModel { BookingViewModel(get(), get()) }
    viewModel { BookingSuccessViewModel(get(), get(), get()) }
    viewModel { AppointmentsViewModel(get(), get(), get(), get()) }
    viewModel { AppointmentDetailViewModel(get(), get(), get()) }
    viewModel { CompleteProfileViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { com.example.medical.presentation.ui.doctor.complete_profile.CompleteDoctorProfileViewModel(get()) }
    viewModel { NotificationsViewModel(get()) }
}
val appModule = module {
    includes(networkModule, repositoryModule, viewModelModule, useCaseModule)
}
