package com.example.medical.di

import com.example.medical.data.repository.AuthRepositoryImpl
import com.example.medical.domain.repository.AuthRepository
import com.example.medical.domain.usecase.LoginUseCase
import com.example.medical.presentation.ui.auth.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val networkModule = module {
    // TODO: Cấu hình Retrofit, OkHttpClient ở đây
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    single<com.example.medical.domain.repository.PatientHomeRepository> { com.example.medical.data.repository.PatientHomeRepositoryImpl() }
    single<com.example.medical.domain.repository.DoctorRepository> { com.example.medical.data.repository.DoctorRepositoryImpl() }
}

val useCaseModule = module {
    factory { LoginUseCase(get()) }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { com.example.medical.presentation.ui.patient_home.PatientHomeViewModel(get()) }
    viewModel { com.example.medical.presentation.ui.doctor_list.DoctorListViewModel(get(), get()) }
}

val appModule = module {
    includes(networkModule, repositoryModule, viewModelModule, useCaseModule)
}
