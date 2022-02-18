package nl.parlio.api.tweedekamer.person.dto

data class PersonDto(
    val id: Long,
    val slug: String,
    val firstName: String,
    val familyName: String,
    val initials: String
)
