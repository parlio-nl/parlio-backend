package nl.parlio.api.tweedekamer.person.svc

import nl.parlio.api.tweedekamer.person.dto.PersonChangeEventDto
import nl.parlio.api.tweedekamer.person.dto.PersonDto
import nl.parlio.api.tweedekamer.shared.types.changes.ChangeEntryDto

interface PersonService {

    fun findPeople(ids: Set<Long>): Map<Long, PersonDto>

    fun findMultipleChangeHistory(personIds: Set<Long>): Map<Long, List<PersonChangeEventDto>>

    fun findChangeLog(changeEventIds: Set<Long>): Map<Long, List<ChangeEntryDto>>
}
