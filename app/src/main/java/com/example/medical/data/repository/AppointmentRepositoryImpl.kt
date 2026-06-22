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
        return flowOf(
            listOf(
                Appointment(
                    id = "3",
                    doctor = Doctor(
                        id = "d3",
                        name = "BS. Lê Văn C",
                        specialty = "Da liễu",
                        hospital = "Phòng khám Da liễu Sài Gòn",
                        avatarUrl = "https://i.pravatar.cc/150?img=32"
                    ),
                    date = "Thứ Hai, 10 Tháng 4, 2024",
                    time = "10:00 - 10:30 AM",
                    hoursRemaining = 0,
                    location = "Phòng khám Da liễu Sài Gòn - Q3",
                    isOnline = false,
                    status = "Hoàn thành",
                    reason = "Kiểm tra dị ứng da, nổi mẩn đỏ sau khi ăn hải sản.",
                    notes = "Bệnh nhân cần kiêng hải sản trong 2 tuần, bôi thuốc mỡ ngày 2 lần."
                ),
                Appointment(
                    id = "4",
                    doctor = Doctor(
                        id = "d4",
                        name = "BS. Phạm Thị D",
                        specialty = "Nội tiết",
                        hospital = "Bệnh viện Chợ Rẫy",
                        avatarUrl = "https://i.pravatar.cc/150?img=45"
                    ),
                    date = "Thứ Năm, 15 Tháng 3, 2024",
                    time = "15:00 - 15:30 PM",
                    hoursRemaining = 0,
                    location = "Tư vấn trực tuyến",
                    isOnline = true,
                    status = "Hoàn thành",
                    reason = "Khám sức khỏe tổng quát định kỳ.",
                    notes = "Các chỉ số bình thường, tiếp tục duy trì chế độ ăn uống lành mạnh."
                )
            )
        )
    }
}
