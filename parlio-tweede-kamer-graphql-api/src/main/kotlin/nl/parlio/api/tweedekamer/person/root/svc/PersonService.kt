package nl.parlio.api.tweedekamer.person.root.svc

import nl.parlio.api.core.relay.connection.RelayConnectionArgs
import nl.parlio.api.tweedekamer.audit.dto.ChangeEntryDto
import nl.parlio.api.tweedekamer.person.root.dto.PersonChangeEventDto
import nl.parlio.api.tweedekamer.person.root.dto.PersonDto

interface PersonService {

    fun findPeople(ids: Set<Long>): Map<Long, PersonDto>

    fun findMultipleChangeHistory(personIds: Set<Long>): Map<Long, List<PersonChangeEventDto>>

    fun findChangeLog(changeEventIds: Set<Long>): Map<Long, List<ChangeEntryDto>>

    fun findPeopleByConnection(args: RelayConnectionArgs<Long>): List<PersonDto>
}
