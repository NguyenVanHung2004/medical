package com.example.medical.data.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.Article
import com.example.medical.domain.model.Doctor
import com.example.medical.domain.model.Specialty
import com.example.medical.domain.repository.PatientHomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PatientHomeRepositoryImpl : PatientHomeRepository {
    override fun getUpcomingAppointment(): Flow<Appointment?> {
        return flowOf(
            Appointment(
                id = "1",
                doctor = Doctor(
                    id = "d1",
                    name = "BS. Nguyễn Văn A",
                    specialty = "Chuyên khoa Tim mạch",
                    hospital = "Bệnh viện Chợ Rẫy",
                    avatarUrl = "https://i.pravatar.cc/150?img=11"
                ),
                date = "15/10/2023",
                time = "09:00 - 09:30",
                hoursRemaining = 2
            )
        )
    }

    override fun getPopularSpecialties(): Flow<List<Specialty>> {
        return flowOf(
            listOf(
                Specialty("1", "Khám nội chung", null),
                Specialty("2", "Nhi khoa", null),
                Specialty("3", "Tim mạch", null),
                Specialty("4", "Răng hàm mặt", null),
                Specialty("5", "Da liễu", null),
                Specialty("6", "Thần kinh", null),
                Specialty("7", "Mắt", null),
                Specialty("8", "Tai mũi họng", null)
            )
        )
    }

    override fun getHealthArticles(): Flow<List<Article>> {
        return flowOf(
            listOf(
                Article(
                    id = "1",
                    title = "5 cách phòng ngừa cúm mùa hiệu quả",
                    category = "Góc sức khỏe",
                    imageUrl = ""
                )
            )
        )
    }

    override fun getUserName(): Flow<String> {
        return flowOf("Minh Tuấn")
    }
}
