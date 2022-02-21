package nl.parlio.api.tweedekamer.person.gift.svc

import nl.parlio.api.core.relay.connection.RelayConnectionArgs
import nl.parlio.api.tweedekamer.person.gift.dto.PersonGiftDto

interface PersonGiftService {
    fun findGiftsByPerson(personId: Long, args: RelayConnectionArgs<Long>): List<PersonGiftDto>
}
