package com.example.medical.domain.repository

import com.example.medical.domain.model.Doctor
import com.example.medical.domain.model.WorkingTimeSlot
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek

interface DoctorProfileRepository {
    fun getDoctorProfile(): Flow<Doctor>
    fun toggleOnlineConsultation(isEnabled: Boolean): Flow<Boolean>
    fun toggleInPersonConsultation(isEnabled: Boolean): Flow<Boolean>
    fun getWorkingTimeSlots(dayOfWeek: DayOfWeek): Flow<List<WorkingTimeSlot>>
    fun updateWorkingTimeSlots(dayOfWeek: DayOfWeek, slots: List<WorkingTimeSlot>): Flow<Boolean>
    fun updateProfile(name: String, specialty: String, hospital: String, experience: String): Flow<Boolean>
    fun updateFees(onlineFee: Long, inPersonFee: Long): Flow<Boolean>
    fun updateAvatar(uri: String): Flow<Boolean>
}
