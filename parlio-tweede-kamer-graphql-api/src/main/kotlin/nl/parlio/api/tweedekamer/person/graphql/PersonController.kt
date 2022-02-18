package nl.parlio.api.tweedekamer.person.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.relay.Connection
import graphql.relay.DefaultConnection
import graphql.relay.DefaultPageInfo
import nl.parlio.api.core.ext.getMappedBatchLoader
import nl.parlio.api.core.relay.Relay
import nl.parlio.api.tweedekamer.person.graphql.dataloader.ChangeEntryByChangeEventIdDataLoader
import nl.parlio.api.tweedekamer.person.graphql.dataloader.ChangeEventByPersonIdDataLoader
import nl.parlio.api.tweedekamer.person.graphql.dataloader.PersonByIdDataLoader
import nl.parlio.tweedekamer.gen.graphql.DgsConstants
import nl.parlio.tweedekamer.gen.graphql.types.ChangeEntry
import nl.parlio.tweedekamer.gen.graphql.types.ChangeEvent
import nl.parlio.tweedekamer.gen.graphql.types.Person
import nl.parlio.tweedekamer.gen.graphql.types.PersonChangeEvent
import org.dataloader.Try
import java.util.concurrent.CompletableFuture

@DgsComponent
class PersonController {

    @DgsQuery
    fun allPeople(): Connection<Person> {
        return DefaultConnection(emptyList(), DefaultPageInfo(null, null, false, false))
    }

    @DgsQuery
    fun rawPerson(
        @InputArgument rawId: String,
        dfe: DgsDataFetchingEnvironment
    ): CompletableFuture<Try<Person>> {
        val dl = dfe.getMappedBatchLoader<Long, Try<Person>, PersonByIdDataLoader>()
        return dl.load(rawId.toLong())
    }

    @DgsData(parentType = DgsConstants.PERSON.TYPE_NAME)
    fun changeHistory(dfe: DgsDataFetchingEnvironment): CompletableFuture<List<ChangeEvent>> {
        val parentGlobalId = dfe.getSource<Person>().id
        val personId = Relay.assertAndExtractId(parentGlobalId, "User")
        val dl =
            dfe.getMappedBatchLoader<Long, List<ChangeEvent>, ChangeEventByPersonIdDataLoader>()
        return dl.load(personId)
    }

    @DgsData(parentType = DgsConstants.CHANGEEVENT.TYPE_NAME)
    fun log(dfe: DgsDataFetchingEnvironment): CompletableFuture<List<ChangeEntry>> {
        val parentGlobalId = dfe.getSource<ChangeEvent>().id
        val id = Relay.assertAndExtractId(parentGlobalId, "ChangeEvent")
        val dl =
            dfe.getMappedBatchLoader<
                Long, List<ChangeEntry>, ChangeEntryByChangeEventIdDataLoader>()
        return dl.load(id)
    }

    @DgsData(parentType = DgsConstants.PERSONCHANGEEVENT.TYPE_NAME)
    fun person(dfe: DgsDataFetchingEnvironment): CompletableFuture<Try<Person>> {
        val globalId = dfe.getSource<PersonChangeEvent>().person.id
        val personId = Relay.assertAndExtractId(globalId, "User")
        val dl = dfe.getMappedBatchLoader<Long, Try<Person>, PersonByIdDataLoader>()
        return dl.load(personId)
    }
}
