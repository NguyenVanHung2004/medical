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
}

val useCaseModule = module {
    factory { LoginUseCase(get()) }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { com.example.medical.presentation.ui.patient_home.PatientHomeViewModel(get()) }
}

val appModule = module {
    includes(networkModule, repositoryModule, viewModelModule, useCaseModule)
}
