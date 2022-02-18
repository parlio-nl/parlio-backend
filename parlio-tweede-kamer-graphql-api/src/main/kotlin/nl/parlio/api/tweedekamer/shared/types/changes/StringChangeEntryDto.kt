package nl.parlio.api.tweedekamer.shared.types.changes

data class StringChangeEntryDto(
    val oldValue: String?,
    val newValue: String?,
    override val key: ChangeEntryKeyDto
) : ChangeEntryDto
