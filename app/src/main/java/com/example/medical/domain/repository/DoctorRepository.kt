package com.example.medical.domain.repository

import com.example.medical.domain.model.ConsultationType
import com.example.medical.domain.model.DoctorDetail
import com.example.medical.domain.model.BookingDate
import com.example.medical.domain.model.TimeSlot
import kotlinx.coroutines.flow.Flow

interface DoctorRepository {
    fun getDoctors(consultationType: ConsultationType? = null): Flow<List<DoctorDetail>>
    fun getDoctorById(id: String): Flow<DoctorDetail?>
    fun getBookingDates(): Flow<List<BookingDate>>
    fun getTimeSlots(dateString: String): Flow<List<TimeSlot>>
}
