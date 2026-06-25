package com.example.medical.data.repository

import com.example.medical.data.remote.ApiService
import com.example.medical.domain.model.Doctor
import com.example.medical.domain.model.WorkingTimeSlot
import com.example.medical.domain.repository.DoctorProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek

class DoctorProfileRepositoryImpl(
    private val apiService: ApiService
) : DoctorProfileRepository {

    private val _doctorProfile = MockSharedData.doctorProfile

    private val allTimeSlots = listOf(
        // Morning
        "08:00 - 08:30", "08:30 - 09:00", "09:00 - 09:30", "09:30 - 10:00",
        "10:00 - 10:30", "10:30 - 11:00", "11:00 - 11:30",
        // Afternoon
        "13:30 - 14:00", "14:00 - 14:30", "14:30 - 15:00", "15:00 - 15:30",
        "15:30 - 16:00", "16:00 - 16:30", "16:30 - 17:00"
    )

    init {
        val initialSchedule = mutableMapOf<DayOfWeek, List<WorkingTimeSlot>>()
        DayOfWeek.values().forEach { day ->
            initialSchedule[day] = generateDefaultSlots(isWeekend = day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY)
        }
        _doctorProfile.update { it.copy(workingSchedule = initialSchedule) }
        updateDoctorProfileSummary()
    }

    override fun getDoctorProfile(): Flow<Doctor> = flow {
        try {
            val user = apiService.getProfile()
            
            // Convert string keys back to DayOfWeek
            val serverSchedule = user.doctorProfile?.workingSchedule
            val scheduleMap = mutableMapOf<DayOfWeek, List<WorkingTimeSlot>>()
            if (serverSchedule != null) {
                serverSchedule.forEach { (key, value) ->
                    try {
                        val day = DayOfWeek.valueOf(key)
                        scheduleMap[day] = value.map { WorkingTimeSlot(it.time, it.isSelected, it.isAvailable) }
                    } catch (e: Exception) {
                        // Ignore invalid keys
                    }
                }
            } else {
                // Keep initial mock schedule if none from server
                scheduleMap.putAll(_doctorProfile.value.workingSchedule)
            }
            
            _doctorProfile.update { it.copy(
                id = user.id, 
                name = user.fullName, 
                avatarUrl = user.avatarUrl,
                specialty = user.doctorProfile?.specialty ?: "",
                hospital = user.doctorProfile?.hospital ?: "",
                experience = "${user.doctorProfile?.yearsOfExperience ?: 0} năm",
                bio = user.doctorProfile?.bio ?: "",
                workingSchedule = scheduleMap
            ) }
            updateDoctorProfileSummary()
            emit(_doctorProfile.value)
        } catch (e: Exception) {
            emit(_doctorProfile.value)
        }
    }

    override fun toggleOnlineConsultation(isEnabled: Boolean): Flow<Boolean> = flow {
        _doctorProfile.update { it.copy(isOnlineConsultationEnabled = isEnabled) }
        emit(true)
    }

    override fun toggleInPersonConsultation(isEnabled: Boolean): Flow<Boolean> = flow {
        _doctorProfile.update { it.copy(isInPersonConsultationEnabled = isEnabled) }
        emit(true)
    }

    override fun getWorkingTimeSlots(dayOfWeek: DayOfWeek): Flow<List<WorkingTimeSlot>> = flow {
        val slots = _doctorProfile.value.workingSchedule[dayOfWeek] ?: emptyList()
        emit(slots)
    }



    override fun updateWorkingTimeSlots(dayOfWeek: DayOfWeek, slots: List<WorkingTimeSlot>): Flow<Boolean> = flow {
        try {
            val currentSchedule = _doctorProfile.value.workingSchedule.toMutableMap()
            currentSchedule[dayOfWeek] = slots
            
            // Map to string keys for API
            val scheduleDto = currentSchedule.mapKeys { it.key.name }.mapValues { entry ->
                entry.value.map { com.example.medical.data.remote.WorkingTimeSlotDto(it.time, it.isSelected, it.isAvailable) }
            }
            
            apiService.updateProfile(
                com.example.medical.data.remote.UpdateProfileRequest(
                    workingSchedule = scheduleDto
                )
            )
            
            _doctorProfile.update { it.copy(workingSchedule = currentSchedule) }
            updateDoctorProfileSummary()
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    override fun updateProfile(name: String, specialty: String, hospital: String, experience: String, bio: String): Flow<Boolean> = flow {
        try {
            val years = experience.filter { it.isDigit() }.toIntOrNull() ?: 0
            val user = apiService.updateProfile(
                com.example.medical.data.remote.UpdateProfileRequest(
                    fullName = name,
                    specialty = specialty,
                    hospital = hospital,
                    yearsOfExperience = years,
                    bio = bio
                )
            )
            _doctorProfile.update { 
                it.copy(
                    name = user.fullName, 
                    specialty = user.doctorProfile?.specialty ?: specialty, 
                    hospital = user.doctorProfile?.hospital ?: hospital, 
                    experience = "${user.doctorProfile?.yearsOfExperience ?: years} năm",
                    bio = user.doctorProfile?.bio ?: bio
                ) 
            }
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    override fun updateFees(onlineFee: Long, inPersonFee: Long): Flow<Boolean> = flow {
        _doctorProfile.update { it.copy(onlineConsultationFee = onlineFee, inPersonConsultationFee = inPersonFee) }
        emit(true)
    }

    override fun updateAvatar(uri: String): Flow<Boolean> = flow {
        _doctorProfile.update { it.copy(avatarUrl = uri) }
        emit(true)
    }

    private fun generateDefaultSlots(isWeekend: Boolean): List<WorkingTimeSlot> {
        return allTimeSlots.map { time ->
            val isSelected = if (isWeekend) false else time in listOf("08:00 - 08:30", "08:30 - 09:00", "09:00 - 09:30", "09:30 - 10:00", "10:00 - 10:30", "10:30 - 11:00", "11:00 - 11:30", "13:30 - 14:00", "14:00 - 14:30", "14:30 - 15:00", "15:00 - 15:30", "15:30 - 16:00", "16:00 - 16:30", "16:30 - 17:00")
            WorkingTimeSlot(
                time = time,
                isSelected = isSelected,
                isAvailable = isSelected
            )
        }
    }

    private fun updateDoctorProfileSummary() {
        val summaryBuilder = StringBuilder()
        val schedule = _doctorProfile.value.workingSchedule
        for (day in DayOfWeek.values()) {
            val slots = schedule[day] ?: continue
            val selectedSlots = slots.filter { it.isSelected }.map { it.time }
            val dayName = when(day) {
                DayOfWeek.MONDAY -> "Thứ 2"
                DayOfWeek.TUESDAY -> "Thứ 3"
                DayOfWeek.WEDNESDAY -> "Thứ 4"
                DayOfWeek.THURSDAY -> "Thứ 5"
                DayOfWeek.FRIDAY -> "Thứ 6"
                DayOfWeek.SATURDAY -> "Thứ 7"
                DayOfWeek.SUNDAY -> "CN"
            }
            if (selectedSlots.isEmpty()) {
                summaryBuilder.append("$dayName: Nghỉ\n")
            } else {
                val morningSlots = selectedSlots.filter { it < "12:00" }
                val afternoonSlots = selectedSlots.filter { it >= "12:00" }
                
                var daySummary = "$dayName: "
                if (morningSlots.isNotEmpty()) {
                   val start = morningSlots.first().substringBefore(" -")
                   val end = morningSlots.last().substringAfter("- ")
                   daySummary += "$start-$end"
                }
                if (afternoonSlots.isNotEmpty()) {
                   if (morningSlots.isNotEmpty()) daySummary += ", "
                   val start = afternoonSlots.first().substringBefore(" -")
                   val end = afternoonSlots.last().substringAfter("- ")
                   daySummary += "$start-$end"
                }
                summaryBuilder.append(daySummary).append("\n")
            }
        }
        val finalSummary = summaryBuilder.toString().trim()
        _doctorProfile.update { it.copy(workingHoursSummary = finalSummary) }
    }
}
