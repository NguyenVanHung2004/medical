package com.example.medical.data.repository

import com.example.medical.domain.model.ConsultationType
import com.example.medical.domain.model.DoctorDetail
import com.example.medical.domain.repository.DoctorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DoctorRepositoryImpl : DoctorRepository {
    override fun getDoctors(consultationType: ConsultationType?): Flow<List<DoctorDetail>> {
        val allDoctors = listOf(
            DoctorDetail(
                id = "1",
                name = "BS. Nguyễn Văn A",
                specialty = "Chuyên khoa Tim mạch",
                hospital = "Bệnh viện Chợ Rẫy",
                avatarUrl = "https://i.pravatar.cc/150?img=11",
                rating = 4.9,
                yearsOfExperience = 15,
                isOnline = true,
                supportedTypes = listOf(ConsultationType.ONLINE, ConsultationType.OFFLINE)
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
                supportedTypes = listOf(ConsultationType.ONLINE)
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
                isFullyBookedToday = true
            )
        )
        
        return if (consultationType != null) {
            flowOf(allDoctors.filter { it.supportedTypes.contains(consultationType) })
        } else {
            flowOf(allDoctors)
        }
    }
}
