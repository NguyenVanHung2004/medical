package com.example.medical.data.remote

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.Doctor
import com.example.medical.domain.model.DoctorDetail
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// Cấu trúc Data Transfer Objects (DTO) để nhận trả response nếu cần
data class LoginRequest(val email: String, val password: String, val role: String)
data class LoginResponse(val token: String, val user: UserDto)
data class PatientProfileDto(
    val dob: String?,
    val gender: String?,
    val address: String?,
    val bloodType: String?,
    val allergies: String?,
    val insuranceInfo: String?
)

data class DoctorProfileDto(
    val specialty: String?,
    val hospital: String?,
    val bio: String?,
    val yearsOfExperience: Int?,
    val rating: Double?,
    val reviewCount: Int?,
    val workingSchedule: Map<String, List<WorkingTimeSlotDto>>? = null
)

data class UserDto(
    val id: String, 
    val email: String, 
    val fullName: String, 
    val role: String, 
    val avatarUrl: String?,
    val phone: String?,
    val patientProfile: PatientProfileDto?,
    val doctorProfile: DoctorProfileDto?
)

data class RegisterRequest(val email: String, val phone: String, val password: String, val fullName: String, val role: String)
data class WorkingTimeSlotDto(
    val time: String,
    val isSelected: Boolean,
    val isAvailable: Boolean = true
)

data class UpdateProfileRequest(
    val fullName: String? = null,
    val dob: String? = null,
    val gender: String? = null,
    val address: String? = null,
    val bloodType: String? = null,
    val allergies: String? = null,
    val insuranceInfo: String? = null,
    val specialty: String? = null,
    val hospital: String? = null,
    val yearsOfExperience: Int? = null,
    val bio: String? = null,
    val workingSchedule: Map<String, List<WorkingTimeSlotDto>>? = null
)

data class BookAppointmentRequest(
    val doctorId: String,
    val date: String,
    val timeRange: String,
    val reason: String,
    val type: String
)

data class UpdateAppointmentStatusRequest(val status: String)

data class DoctorListItemDto(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val specialty: String?,
    val hospital: String?,
    val experience: String?,
    val rating: Double?,
    val reviewCount: Int?,
    val workingSchedule: Map<String, List<WorkingTimeSlotDto>>? = null
)

data class DoctorDetailDto(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val specialty: String?,
    val hospital: String?,
    val bio: String?,
    val yearsOfExperience: Int?,
    val rating: Double?,
    val reviewCount: Int?,
    val workingSchedule: Map<String, List<WorkingTimeSlotDto>>? = null,
    val doctorProfile: DoctorProfileDto? = null
)

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): LoginResponse

    @GET("api/users/profile")
    suspend fun getProfile(): UserDto
    
    @PUT("api/users/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): UserDto
    
    @GET("api/doctors")
    suspend fun getDoctors(@Query("specialty") specialty: String? = null, @Query("search") search: String? = null): List<DoctorListItemDto>

    @GET("api/doctors/{id}")
    suspend fun getDoctorDetail(@Path("id") doctorId: String): DoctorDetailDto

    @GET("api/appointments")
    suspend fun getAppointments(@Query("status") status: String? = null): List<Appointment>

    @POST("api/appointments")
    suspend fun bookAppointment(@Body request: BookAppointmentRequest): Appointment

    @PATCH("api/appointments/{id}/status")
    suspend fun updateAppointmentStatus(@Path("id") appointmentId: String, @Body request: UpdateAppointmentStatusRequest): Appointment
}
