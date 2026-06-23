    package com.example.medical.data.repository

import com.example.medical.domain.model.ConsultationType
import com.example.medical.domain.model.DoctorDetail
import com.example.medical.domain.model.BookingDate
import com.example.medical.domain.model.TimeSlot
import com.example.medical.domain.repository.DoctorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DoctorRepositoryImpl : DoctorRepository {
    private val allDoctors = listOf(
        DoctorDetail(
            id = "1",
            name = "PGS. TS. BS. Nguyễn Văn An",
            specialty = "Chuyên khoa Tim mạch",
            hospital = "Bệnh viện Đại học Y Hà Nội",
            avatarUrl = "https://i.pravatar.cc/150?img=11",
            rating = 4.9,
            yearsOfExperience = 20,
            isOnline = true,
            supportedTypes = listOf(ConsultationType.ONLINE, ConsultationType.OFFLINE),
            reviewCount = 128,
            bio = "Hơn 20 năm kinh nghiệm trong chẩn đoán và điều trị các bệnh lý tim mạch. Nguyên Trưởng khoa Tim mạch can thiệp. Thế mạnh trong điều trị suy tim, tăng huyết áp..."
        ),
        DoctorDetail(
            id = "2",
            name = "BS. Trần Thị B",
            specialty = "Nhi khoa",
            hospital = "Bệnh viện Nhi Đồng TP.HCM",
            avatarUrl = "https://i.pravatar.cc/150?img=5",
            rating = 4.8,
            yearsOfExperience = 8,
            isOnline = true,
            supportedTypes = listOf(ConsultationType.ONLINE),
            reviewCount = 95,
            bio = "Bác sĩ tận tâm với 8 năm kinh nghiệm trong lĩnh vực Nhi khoa. Đặc biệt chuyên sâu về các bệnh hô hấp và tiêu hóa ở trẻ sơ sinh."
        ),
        DoctorDetail(
            id = "3",
            name = "BS. Lê Văn C",
            specialty = "Da liễu",
            hospital = "Phòng khám tư Đà Nẵng",
            avatarUrl = "https://i.pravatar.cc/150?img=13",
            rating = 4.7,
            yearsOfExperience = 12,
            isOnline = false,
            supportedTypes = listOf(ConsultationType.OFFLINE),
            isFullyBookedToday = true,
            reviewCount = 201,
            bio = "Chuyên gia da liễu với nhiều năm tu nghiệp tại Pháp. Tư vấn và điều trị mụn, sẹo, rụng tóc và các bệnh lý da liễu phức tạp."
        )
    )

    override fun getDoctors(consultationType: ConsultationType?): Flow<List<DoctorDetail>> {
        
        return if (consultationType != null) {
            flowOf(allDoctors.filter { it.supportedTypes.contains(consultationType) })
        } else {
            flowOf(allDoctors)
        }
    }

    override fun getDoctorById(id: String): Flow<DoctorDetail?> {
        return flowOf(allDoctors.find { it.id == id })
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
                month = "Tháng ${date.monthValue}"
            )
        }
        return flowOf(dates)
    }

    override fun getTimeSlots(dateString: String): Flow<List<TimeSlot>> {
        return flowOf(
            listOf(
                TimeSlot("1", "08:00 - 08:30", isAvailable = false),
                TimeSlot("2", "08:30 - 09:00", isAvailable = false),
                TimeSlot("3", "09:00 - 09:30", isAvailable = true),
                TimeSlot("4", "09:30 - 10:00", isAvailable = true),
                TimeSlot("5", "10:00 - 10:30", isAvailable = true),
                TimeSlot("6", "10:30 - 11:00", isAvailable = true),
                TimeSlot("7", "14:00 - 14:30", isAvailable = true),
                TimeSlot("8", "14:30 - 15:00", isAvailable = true)
            )
        )
    }
}
