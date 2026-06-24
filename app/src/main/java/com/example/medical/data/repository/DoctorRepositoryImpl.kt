package com.example.medical.data.repository

import com.example.medical.data.remote.ApiService
import com.example.medical.domain.model.ConsultationType
import com.example.medical.domain.model.DoctorDetail
import com.example.medical.domain.model.BookingDate
import com.example.medical.domain.model.TimeSlot
import com.example.medical.domain.repository.DoctorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class DoctorRepositoryImpl(
    private val apiService: ApiService
) : DoctorRepository {

    override fun getDoctors(consultationType: ConsultationType?): Flow<List<DoctorDetail>> = flow {
        try {
            val doctorDtos = apiService.getDoctors()
            val doctors = doctorDtos.map { dto ->
                DoctorDetail(
                    id = dto.id,
                    name = dto.name,
                    specialty = dto.specialty ?: "",
                    hospital = dto.hospital ?: "",
                    avatarUrl = dto.avatarUrl ?: "",
                    rating = dto.rating ?: 0.0,
                    yearsOfExperience = dto.experience?.filter { it.isDigit() }?.toIntOrNull() ?: 0,
                    isOnline = false,
                    supportedTypes = listOf(ConsultationType.ONLINE, ConsultationType.OFFLINE),
                    reviewCount = dto.reviewCount ?: 0
                )
            }
            if (consultationType != null) {
                emit(doctors.filter { it.supportedTypes.contains(consultationType) })
            } else {
                emit(doctors)
            }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override fun getDoctorById(id: String): Flow<DoctorDetail?> = flow {
        try {
            val dto = apiService.getDoctorDetail(id)
            val listDto = try { apiService.getDoctors().find { it.id == id } } catch (e: Exception) { null }
            val doctor = DoctorDetail(
                id = dto.id,
                name = dto.name,
                specialty = dto.specialty ?: "",
                hospital = dto.hospital ?: "",
                avatarUrl = dto.avatarUrl ?: "",
                rating = dto.rating ?: 0.0,
                yearsOfExperience = dto.yearsOfExperience ?: 0,
                isOnline = false,
                supportedTypes = listOf(ConsultationType.ONLINE, ConsultationType.OFFLINE),
                reviewCount = dto.reviewCount ?: 0,
                bio = dto.bio ?: "",
                workingSchedule = mapWorkingSchedule(dto.workingSchedule ?: dto.doctorProfile?.workingSchedule ?: listDto?.workingSchedule)
            )
            emit(doctor)
        } catch (e: Exception) {
            emit(null)
        }
    }

    override fun getBookingDates(): Flow<List<BookingDate>> {
        val today = java.time.LocalDate.now()
        val dates = (0..4).map { i ->
            val date = today.plusDays(i.toLong())
            val dayOfWeek = when (date.dayOfWeek) {
                java.time.DayOfWeek.MONDAY -> "Th 2"
                java.time.DayOfWeek.TUESDAY -> "Th 3"
                java.time.DayOfWeek.WEDNESDAY -> "Th 4"
                java.time.DayOfWeek.THURSDAY -> "Th 5"
                java.time.DayOfWeek.FRIDAY -> "Th 6"
                java.time.DayOfWeek.SATURDAY -> "Th 7"
                java.time.DayOfWeek.SUNDAY -> "CN"
            }
            BookingDate(
                dateString = date.dayOfMonth.toString(),
                dayOfWeek = if (i == 0) "Hôm nay" else dayOfWeek,
                month = "Tháng ${date.monthValue}",
                dayOfWeekEnum = date.dayOfWeek
            )
        }
        return flowOf(dates)
    }

    override fun getTimeSlots(dateString: String): Flow<List<TimeSlot>> {
        return MockSharedData.doctorProfile.map { doctor ->
            val scheduleSlots = doctor.workingSchedule.values.firstOrNull { it.isNotEmpty() } ?: emptyList()
            scheduleSlots.mapIndexed { index, slot ->
                TimeSlot(
                    id = index.toString(),
                    timeRange = slot.time,
                    isAvailable = slot.isSelected
                )
            }
        }
    }

    private fun mapWorkingSchedule(dto: Map<String, List<com.example.medical.data.remote.WorkingTimeSlotDto>>?): Map<java.time.DayOfWeek, List<com.example.medical.domain.model.WorkingTimeSlot>> {
        val scheduleMap = mutableMapOf<java.time.DayOfWeek, List<com.example.medical.domain.model.WorkingTimeSlot>>()
        if (dto != null) {
            dto.forEach { (key, value) ->
                try {
                    val day = java.time.DayOfWeek.valueOf(key)
                    scheduleMap[day] = value.map { com.example.medical.domain.model.WorkingTimeSlot(it.time, it.isSelected, it.isAvailable) }
                } catch (e: Exception) {
                }
            }
        }
        return scheduleMap
    }
}
