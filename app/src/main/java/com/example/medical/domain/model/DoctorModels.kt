package com.example.medical.domain.model

enum class ConsultationType {
    ONLINE, OFFLINE, HOME_VISIT
}

enum class DoctorSpecialty(val displayName: String) {
    CARDIOLOGY("TIM MẠCH"),
    PEDIATRICS("NHI KHOA"),
    DERMATOLOGY("DA LIỄU"),
    DENTISTRY("RĂNG HÀM MẶT"),
    NEUROLOGY("THẦN KINH"),
    ORTHOPEDICS("CHẤN THƯƠNG CHỈNH HÌNH"),
    OPHTHALMOLOGY("NHÃN KHOA"),
    GENERAL("ĐA KHOA")
}

data class DoctorDetail(
    val id: String,
    val name: String,
    val specialty: String,
    val hospital: String,
    val avatarUrl: String,
    val rating: Double,
    val yearsOfExperience: Int,
    val isOnline: Boolean,
    val supportedTypes: List<ConsultationType>,
    val isFullyBookedToday: Boolean = false,
    val reviewCount: Int = 0,
    val bio: String = "",
    val workingSchedule: Map<java.time.DayOfWeek, List<WorkingTimeSlot>> = emptyMap()
)
data class Doctor(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val specialty: String = "",
    val hospital: String = "",
    val experience: String = "",
    val bio: String = "",
    val isOnlineConsultationEnabled: Boolean = false,
    val onlineConsultationFee: Long = 0,
    val isInPersonConsultationEnabled: Boolean = false,
    val inPersonConsultationFee: Long = 0,
    val workingHoursSummary: String = "Thứ 2 - Thứ 6\n08:00 - 11:30\n13:30 - 17:00",
    val workingSchedule: Map<java.time.DayOfWeek, List<WorkingTimeSlot>> = emptyMap()
)

data class WorkingTimeSlot(
    val time: String,
    val isSelected: Boolean,
    val isAvailable: Boolean = true
)

data class AppointmentRequest(
    val id: String,
    val patientName: String,
    val patientInitial: String,
    val timeRange: String,
    val reason: String,
    val type: ConsultationType = ConsultationType.ONLINE,
    val location: String? = null
)

enum class AppointmentStatus {
    PENDING, CONFIRMED, UPCOMING, HAPPENING, COMPLETED, CANCELLED
}

