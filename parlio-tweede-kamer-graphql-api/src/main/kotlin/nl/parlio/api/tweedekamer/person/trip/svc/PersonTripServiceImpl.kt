package nl.parlio.api.tweedekamer.person.trip.svc

import nl.parlio.api.core.ext.convertList
import nl.parlio.api.core.relay.connection.RelayConnectionArgs
import nl.parlio.api.tweedekamer.person.trip.dto.PersonTripDto
import nl.parlio.api.tweedekamer.shared.utils.RelayConnectionOoq.relayLimit
import nl.parlio.api.tweedekamer.shared.utils.RelayConnectionOoq.relayOrderBy
import nl.parlio.api.tweedekamer.shared.utils.RelayConnectionOoq.relayWhere
import nl.parlio.tweedekamer.gen.jooq.tables.PersonTripTable.PERSON_TRIP
import org.jooq.DSLContext
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service

@Service
class PersonTripServiceImpl(
    private val dsl: DSLContext,
    private val conversionService: ConversionService
) : PersonTripService {

    override fun findTripsByPerson(
        personId: Long,
        args: RelayConnectionArgs<Long>
    ): List<PersonTripDto> {
        val trips =
            with(PERSON_TRIP) {
                dsl.select(PERSON_TRIP_ID, START_DATE, DESTINATION, PURPOSE, END_DATE, PAID_BY)
                    .from(this)
                    .where(relayWhere(PERSON_TRIP_ID, PERSON_ID, personId, args))
                    .orderBy(relayOrderBy(PERSON_TRIP_ID, args))
                    .limit(relayLimit(args))
                    .fetchInto(recordType)
            }

        return conversionService.convertList(trips)
    }
}
