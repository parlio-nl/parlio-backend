package nl.parlio.api.tweedekamer.person.trip.dto

import java.time.LocalDate

@JvmRecord
data class PersonTripDto(
    val id: Long,
    val destination: String?,
    val endDate: LocalDate?,
    val startDate: LocalDate?,
    val purpose: String?,
    val paidBy: String?
)
