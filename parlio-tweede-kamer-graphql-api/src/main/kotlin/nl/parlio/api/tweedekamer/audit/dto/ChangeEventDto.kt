package nl.parlio.api.tweedekamer.audit.dto

interface ChangeEventDto {
    val id: Long
    val op: String
}
