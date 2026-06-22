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
            hospital = "Bệnh viện Đại học Y",
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
            hospital = "Bệnh viện Nhi Đồng",
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
            hospital = "Phòng khám tư",
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
        return flowOf(
            listOf(
                BookingDate("12", "THL 2", "Tháng 5"),
                BookingDate("13", "THL 3", "Tháng 5"),
                BookingDate("14", "THL 4", "Tháng 5"),
                BookingDate("15", "THL 5", "Tháng 5"),
                BookingDate("16", "THL 6", "Tháng 5")
            )
        )
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
