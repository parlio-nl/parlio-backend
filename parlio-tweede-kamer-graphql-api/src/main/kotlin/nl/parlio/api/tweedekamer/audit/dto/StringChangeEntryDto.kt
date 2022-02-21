package nl.parlio.api.tweedekamer.audit.dto

data class StringChangeEntryDto(
    val oldValue: String?,
    val newValue: String?,
    override val key: ChangeEntryKeyDto
) : ChangeEntryDto
