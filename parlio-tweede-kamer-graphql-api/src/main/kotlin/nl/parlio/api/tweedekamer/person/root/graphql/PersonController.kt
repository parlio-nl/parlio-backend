package nl.parlio.api.tweedekamer.person.root.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.relay.Connection
import java.util.concurrent.CompletableFuture
import nl.parlio.api.core.ext.convertExact
import nl.parlio.api.core.ext.getMappedBatchLoader
import nl.parlio.api.core.relay.Relay
import nl.parlio.api.core.relay.connection.RelayConnection
import nl.parlio.api.tweedekamer.audit.graphql.dataloader.ChangeEventBySearchCompositeDataLoader
import nl.parlio.api.tweedekamer.audit.types.ChangeEventModel
import nl.parlio.api.tweedekamer.audit.types.ChangeEventSearchComposite
import nl.parlio.api.tweedekamer.person.root.graphql.dataloader.PersonByIdDataLoader
import nl.parlio.api.tweedekamer.person.root.graphql.dataloader.PersonBySlugDataLoader
import nl.parlio.api.tweedekamer.person.root.svc.PersonService
import nl.parlio.tweedekamer.gen.graphql.DgsConstants
import nl.parlio.tweedekamer.gen.graphql.types.ChangeEvent
import nl.parlio.tweedekamer.gen.graphql.types.Person
import nl.parlio.tweedekamer.gen.graphql.types.PersonChangeEvent
import org.dataloader.Try
import org.springframework.core.convert.ConversionService

@DgsComponent
class PersonController(
    private val personService: PersonService,
    private val conversionService: ConversionService
) {

    @DgsQuery
    fun rawPerson(
        @InputArgument rawId: String,
        dfe: DgsDataFetchingEnvironment
    ): CompletableFuture<Try<Person>> {
        val dl = dfe.getMappedBatchLoader<Long, Try<Person>, PersonByIdDataLoader>()
        return dl.load(rawId.toLong())
    }

    @DgsQuery(field = DgsConstants.QUERY.Person)
    fun person(
        @InputArgument("slug") slug: String,
        dfe: DgsDataFetchingEnvironment
    ): CompletableFuture<Try<Person>> {
        val dl = dfe.getMappedBatchLoader<String, Try<Person>, PersonBySlugDataLoader>()
        return dl.load(slug)
    }

    @DgsQuery(field = DgsConstants.QUERY.People)
    fun people(dfe: DgsDataFetchingEnvironment): CompletableFuture<Connection<Person>> {
        val connArgs =
            RelayConnection.extractConnectionArguments(dfe, Relay.LongCursor::assertAndExtract)
        return CompletableFuture.supplyAsync {
            val findPeople = personService.findPeopleByConnection(connArgs)
            return@supplyAsync RelayConnection.createConnectionOnEach(
                findPeople,
                { Relay.LongCursor.toCursor(it.id) },
                { conversionService.convertExact<Person>(it) }
            )
        }
    }

    @DgsData(parentType = DgsConstants.PERSON.TYPE_NAME, field = DgsConstants.PERSON.DisplayName)
    fun personDisplayName(dfe: DgsDataFetchingEnvironment): String? {
        val person = dfe.getSource<Person>()
        var displayName: String? = null
        if (person.firstName != null) {
            displayName = person.firstName
        }
        if (person.familyName != null) {
            if (displayName == null) {
                displayName = person.familyName
            } else {
                displayName += (" " + person.familyName)
            }
        }
        return displayName
    }

    @DgsData(parentType = DgsConstants.PERSON.TYPE_NAME)
    fun changeHistory(dfe: DgsDataFetchingEnvironment): CompletableFuture<List<ChangeEvent>> {
        val parentGlobalId = dfe.getSource<Person>().id
        val personId = Relay.assertAndExtractId(parentGlobalId, "User")
        val dl =
            dfe.getMappedBatchLoader<
                ChangeEventSearchComposite<Long>,
                List<ChangeEvent>,
                ChangeEventBySearchCompositeDataLoader>()
        return dl.load(ChangeEventSearchComposite(ChangeEventModel.PERSON, personId))
    }

    @DgsData(parentType = DgsConstants.PERSONCHANGEEVENT.TYPE_NAME)
    fun person(dfe: DgsDataFetchingEnvironment): CompletableFuture<Try<Person>> {
        val globalId = dfe.getSource<PersonChangeEvent>().person.id
        val personId = Relay.assertAndExtractId(globalId, "User")
        val dl = dfe.getMappedBatchLoader<Long, Try<Person>, PersonByIdDataLoader>()
        return dl.load(personId)
    }
}
