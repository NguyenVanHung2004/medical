package com.example.medical.domain.repository

import com.example.medical.domain.model.ConsultationType
import com.example.medical.domain.model.DoctorDetail
import kotlinx.coroutines.flow.Flow

interface DoctorRepository {
    fun getDoctors(consultationType: ConsultationType? = null): Flow<List<DoctorDetail>>
}
