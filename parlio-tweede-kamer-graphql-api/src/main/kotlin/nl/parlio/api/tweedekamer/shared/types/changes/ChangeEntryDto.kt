package nl.parlio.api.tweedekamer.shared.types.changes

sealed interface ChangeEntryDto {
    val key: ChangeEntryKeyDto
}
