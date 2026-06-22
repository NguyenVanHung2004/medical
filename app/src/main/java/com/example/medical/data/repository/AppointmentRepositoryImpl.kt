package com.example.medical.data.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.Doctor
import com.example.medical.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AppointmentRepositoryImpl : AppointmentRepository {
    override fun getUpcomingAppointments(): Flow<List<Appointment>> {
        return flowOf(
            listOf(
                Appointment(
                    id = "1",
                    doctor = Doctor(
                        id = "d1",
                        name = "BS. Nguyễn Văn A",
                        specialty = "Tim mạch",
                        hospital = "Bệnh viện Chợ Rẫy",
                        avatarUrl = "https://i.pravatar.cc/150?img=11"
                    ),
                    date = "Thứ Tư, 24 Tháng 5, 2024",
                    time = "09:00 - 09:30 AM",
                    hoursRemaining = 48,
                    location = "Phòng khám Đa khoa MediConnect - Q1",
                    isOnline = false,
                    status = null
                ),
                Appointment(
                    id = "2",
                    doctor = Doctor(
                        id = "d2",
                        name = "BS. Trần Thị B",
                        specialty = "Nhi khoa",
                        hospital = "Bệnh viện Nhi Đồng",
                        avatarUrl = "https://i.pravatar.cc/150?img=5"
                    ),
                    date = "Thứ Sáu, 26 Tháng 5, 2024",
                    time = "14:00 - 14:30 PM",
                    hoursRemaining = 96,
                    location = "Tư vấn trực tuyến",
                    isOnline = true,
                    status = "Đã xác nhận"
                )
            )
        )
    }

    override fun getHistoryAppointments(): Flow<List<Appointment>> {
        return flowOf(emptyList()) // No history for mock
    }
}
