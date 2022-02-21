package nl.parlio.api.tweedekamer.person.gift.dto

import java.time.LocalDate

@JvmRecord
data class PersonGiftDto(
    val id: Long,
    val personId: Long,
    val description: String,
    val date: LocalDate
)
