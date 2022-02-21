package nl.parlio.api.tweedekamer.person.trip.svc

import nl.parlio.api.core.relay.connection.RelayConnectionArgs
import nl.parlio.api.tweedekamer.person.trip.dto.PersonTripDto

interface PersonTripService {
    fun findTripsByPerson(personId: Long, args: RelayConnectionArgs<Long>): List<PersonTripDto>
}
