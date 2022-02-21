package nl.parlio.api.tweedekamer.person.gift.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import graphql.relay.Connection
import java.util.concurrent.CompletableFuture
import nl.parlio.api.core.ext.convertExact
import nl.parlio.api.core.ext.getMappedBatchLoader
import nl.parlio.api.core.relay.Relay
import nl.parlio.api.core.relay.connection.RelayConnection
import nl.parlio.api.tweedekamer.audit.graphql.dataloader.ChangeEventBySearchCompositeDataLoader
import nl.parlio.api.tweedekamer.audit.types.ChangeEventModel
import nl.parlio.api.tweedekamer.audit.types.ChangeEventSearchComposite
import nl.parlio.api.tweedekamer.person.gift.svc.PersonGiftService
import nl.parlio.tweedekamer.gen.graphql.DgsConstants
import nl.parlio.tweedekamer.gen.graphql.types.ChangeEvent
import nl.parlio.tweedekamer.gen.graphql.types.Person
import nl.parlio.tweedekamer.gen.graphql.types.PersonGift
import org.springframework.core.convert.ConversionService

@DgsComponent
class PersonGiftController(
    private val conversionService: ConversionService,
    private val personGiftService: PersonGiftService
) {

    @DgsData(parentType = DgsConstants.PERSON.TYPE_NAME, field = DgsConstants.PERSON.Gifts)
    fun gifts(dfe: DgsDataFetchingEnvironment): CompletableFuture<Connection<PersonGift>> {
        val parentPerson: Person = dfe.getSource()
        val personId = Relay.assertAndExtractId(parentPerson.id, "Person")
        val connArgs =
            RelayConnection.extractConnectionArguments(dfe, Relay.LongCursor::assertAndExtract)
        return CompletableFuture.supplyAsync {
            val findGiftsByPerson = personGiftService.findGiftsByPerson(personId, connArgs)
            return@supplyAsync RelayConnection.createConnectionOnEach(
                findGiftsByPerson,
                { Relay.LongCursor.toCursor(it.id) },
                { conversionService.convertExact<PersonGift>(it) }
            )
        }
    }

    @DgsData(
        parentType = DgsConstants.PERSONGIFT.TYPE_NAME,
        field = DgsConstants.PERSONGIFT.ChangeHistory
    )
    fun changeHistory(dfe: DgsDataFetchingEnvironment): CompletableFuture<List<ChangeEvent>> {
        val parentGlobalId = dfe.getSource<PersonGift>().id
        val personGiftId = Relay.assertAndExtractId(parentGlobalId, "PersonGift")
        val dl =
            dfe.getMappedBatchLoader<
                ChangeEventSearchComposite<Long>,
                List<ChangeEvent>,
                ChangeEventBySearchCompositeDataLoader>()
        return dl.load(ChangeEventSearchComposite(ChangeEventModel.PERSON_GIFT, personGiftId))
    }
}
