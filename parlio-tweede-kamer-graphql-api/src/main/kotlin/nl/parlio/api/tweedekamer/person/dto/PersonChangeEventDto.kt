package nl.parlio.api.tweedekamer.person.dto

import nl.parlio.api.tweedekamer.shared.types.ChangeEventTo

sealed interface PersonChangeEventDto : ChangeEventTo {
    val personId: Long
}