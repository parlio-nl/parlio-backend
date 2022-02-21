package nl.parlio.api.tweedekamer.person.root.svc

import nl.parlio.api.core.relay.connection.RelayConnectionArgs
import nl.parlio.api.tweedekamer.person.root.dto.PersonDto

interface PersonService {

    fun findPeople(ids: Set<Long>): Map<Long, PersonDto>

    fun findPeopleByConnection(args: RelayConnectionArgs<Long>): List<PersonDto>
}
