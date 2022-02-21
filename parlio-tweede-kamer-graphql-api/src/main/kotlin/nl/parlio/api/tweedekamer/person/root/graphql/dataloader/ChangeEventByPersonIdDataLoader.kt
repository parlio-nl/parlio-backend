package nl.parlio.api.tweedekamer.person.root.graphql.dataloader

import com.netflix.graphql.dgs.DgsDataLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import nl.parlio.api.core.ext.fillMissingKeys
import nl.parlio.api.core.relay.Relay
import nl.parlio.api.tweedekamer.person.root.dto.PersonChangeEventDto
import nl.parlio.api.tweedekamer.person.root.dto.PersonSyncFeedUpdateDto
import nl.parlio.api.tweedekamer.person.root.svc.PersonService
import nl.parlio.tweedekamer.gen.graphql.types.ChangeEvent
import nl.parlio.tweedekamer.gen.graphql.types.Person
import nl.parlio.tweedekamer.gen.graphql.types.PersonSyncFeedUpdateEvent
import org.dataloader.MappedBatchLoader

@DgsDataLoader(name = "ChangeEventByPersonIdDataLoader")
class ChangeEventByPersonIdDataLoader(private val personService: PersonService) :
    MappedBatchLoader<Long, List<ChangeEvent>> {
    override fun load(keys: Set<Long>): CompletionStage<Map<Long, List<ChangeEvent>>> {
        return CompletableFuture.supplyAsync {
            personService
                .findMultipleChangeHistory(keys)
                .mapValues { it.value.map(::mapPersonChangeEventTo) }
                .fillMissingKeys(keys) { emptyList() }
        }
    }

    fun mapPersonChangeEventTo(left: PersonChangeEventDto): ChangeEvent {
        if (left is PersonSyncFeedUpdateDto) {
            return PersonSyncFeedUpdateEvent(
                Relay.toGlobalId("ChangeEvent", left.id),
                Person.newBuilder().id(Relay.toGlobalId("Person", left.personId)).build(),
                left.op,
                null
            )
        }
        TODO()
    }
}
