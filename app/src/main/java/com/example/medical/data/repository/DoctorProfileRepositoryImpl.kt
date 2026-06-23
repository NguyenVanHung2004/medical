package com.example.medical.data.repository

import com.example.medical.domain.model.Doctor
import com.example.medical.domain.model.WorkingTimeSlot
import com.example.medical.domain.repository.DoctorProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek

class DoctorProfileRepositoryImpl : DoctorProfileRepository {

    private val _doctorProfile = MutableStateFlow(
        Doctor(
            id = "doc123",
            name = "BS. Nguyễn Văn An",
            avatarUrl = null,
            specialty = "CHUYÊN KHOA TIM MẠCH",
            hospital = "Bệnh viện Bạch Mai",
            experience = "15 năm kinh nghiệm",
            isOnlineConsultationEnabled = true,
            onlineConsultationFee = 200000,
            isInPersonConsultationEnabled = true,
            inPersonConsultationFee = 300000,
            workingHoursSummary = "Tuỳ chỉnh theo ngày"
        )
    )

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

    override fun getDoctorProfile(): Flow<Doctor> = _doctorProfile

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
        val currentSchedule = _doctorProfile.value.workingSchedule.toMutableMap()
        currentSchedule[dayOfWeek] = slots
        _doctorProfile.update { it.copy(workingSchedule = currentSchedule) }
        updateDoctorProfileSummary()
        emit(true)
    }

    override fun updateProfile(name: String, specialty: String, hospital: String, experience: String): Flow<Boolean> = flow {
        _doctorProfile.update { it.copy(name = name, specialty = specialty, hospital = hospital, experience = experience) }
        emit(true)
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
            WorkingTimeSlot(
                time = time,
                isSelected = if (isWeekend) false else time in listOf("08:00 - 08:30", "08:30 - 09:00", "09:00 - 09:30", "09:30 - 10:00", "10:00 - 10:30", "10:30 - 11:00", "11:00 - 11:30", "13:30 - 14:00", "14:00 - 14:30", "14:30 - 15:00", "15:00 - 15:30", "15:30 - 16:00", "16:00 - 16:30", "16:30 - 17:00"),
                isAvailable = true
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
