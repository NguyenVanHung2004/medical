package com.example.medical.domain.usecase

import com.example.medical.domain.model.DoctorDetail

class FilterDoctorsUseCase {
    operator fun invoke(
        doctors: List<DoctorDetail>,
        query: String = "",
        specialty: String? = null,
        rating: String? = null,
        location: String? = null,
        availability: String? = null
    ): List<DoctorDetail> {
        var filteredList = doctors

        // Search Query
        val q = query.trim().lowercase()
        if (q.isNotEmpty()) {
            filteredList = filteredList.filter {
                it.name.lowercase().contains(q) || it.specialty.lowercase().contains(q)
            }
        }

        // Specialty
        if (specialty != null && specialty != "Tất cả") {
            filteredList = filteredList.filter { it.specialty.contains(specialty, ignoreCase = true) }
        }

        // Rating
        if (rating != null) {
            val minRating = when (rating) {
                "Từ 4.5 sao" -> 4.5
                "Từ 4.0 sao" -> 4.0
                else -> 0.0
            }
            filteredList = filteredList.filter { it.rating >= minRating }
        }

        // Location
        if (location != null && location != "Tất cả" && location != "Gần tôi nhất") {
            filteredList = filteredList.filter { it.hospital.contains(location, ignoreCase = true) }
        }

        // Availability
        if (availability == "Sẵn sàng hôm nay") {
            filteredList = filteredList.filter { !it.isFullyBookedToday }
        }

        return filteredList
    }
}
