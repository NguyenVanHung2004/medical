package com.example.medical.domain.model

data class BookingDate(
    val dateString: String, // e.g. "12"
    val dayOfWeek: String, // e.g. "THL 2"
    val month: String, // e.g. "Tháng 5"
    val dayOfWeekEnum: java.time.DayOfWeek = java.time.DayOfWeek.MONDAY
)

data class TimeSlot(
    val id: String,
    val timeRange: String, // e.g. "09:00 - 09:30"
    val isAvailable: Boolean
)
