package nl.parlio.api.tweedekamer.audit.dto

data class GenericChangeEventDto(override val id: Long, override val op: String) : ChangeEventDto
