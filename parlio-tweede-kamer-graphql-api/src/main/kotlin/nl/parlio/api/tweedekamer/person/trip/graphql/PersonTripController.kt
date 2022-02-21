package nl.parlio.api.tweedekamer.person.trip.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import graphql.relay.Connection
import java.util.concurrent.CompletableFuture
import nl.parlio.api.core.ext.convertExact
import nl.parlio.api.core.relay.Relay
import nl.parlio.api.core.relay.connection.RelayConnection
import nl.parlio.api.tweedekamer.person.trip.svc.PersonTripService
import nl.parlio.tweedekamer.gen.graphql.DgsConstants
import nl.parlio.tweedekamer.gen.graphql.types.Person
import nl.parlio.tweedekamer.gen.graphql.types.PersonTrip
import org.springframework.core.convert.ConversionService

@DgsComponent
class PersonTripController(
    private val conversionService: ConversionService,
    private val personTripService: PersonTripService
) {

    @DgsData(parentType = DgsConstants.PERSON.TYPE_NAME, field = DgsConstants.PERSON.Trips)
    fun trips(dfe: DgsDataFetchingEnvironment): CompletableFuture<Connection<PersonTrip>> {
        val parentPerson: Person = dfe.getSource()
        val personId = Relay.assertAndExtractId(parentPerson.id, "Person")
        val connArgs =
            RelayConnection.extractConnectionArguments(dfe, Relay.LongCursor::assertAndExtract)
        return CompletableFuture.supplyAsync {
            val findTripsByPerson = personTripService.findTripsByPerson(personId, connArgs)
            return@supplyAsync RelayConnection.createConnectionOnEach(
                findTripsByPerson,
                { Relay.LongCursor.toCursor(it.id) },
                { conversionService.convertExact<PersonTrip>(it) }
            )
        }
    }
}
