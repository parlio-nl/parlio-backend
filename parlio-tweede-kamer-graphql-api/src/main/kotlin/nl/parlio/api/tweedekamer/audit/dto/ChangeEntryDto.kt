package nl.parlio.api.tweedekamer.audit.dto

sealed interface ChangeEntryDto {
    val key: ChangeEntryKeyDto
}
