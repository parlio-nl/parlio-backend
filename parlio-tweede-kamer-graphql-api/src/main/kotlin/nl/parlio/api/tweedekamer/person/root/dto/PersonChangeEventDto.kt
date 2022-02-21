package nl.parlio.api.tweedekamer.person.root.dto

import nl.parlio.api.tweedekamer.audit.dto.ChangeEventDto

sealed interface PersonChangeEventDto : ChangeEventDto {
    val personId: Long
}
