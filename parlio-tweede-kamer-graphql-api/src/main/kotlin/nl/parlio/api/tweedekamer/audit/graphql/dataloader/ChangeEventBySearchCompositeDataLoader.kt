package nl.parlio.api.tweedekamer.audit.graphql.dataloader

import com.netflix.graphql.dgs.DgsDataLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import nl.parlio.api.core.ext.fillMissingKeys
import nl.parlio.api.core.relay.Relay
import nl.parlio.api.tweedekamer.audit.ChangeEventFactory
import nl.parlio.api.tweedekamer.audit.dto.ChangeEventDto
import nl.parlio.api.tweedekamer.audit.dto.GenericChangeEventDto
import nl.parlio.api.tweedekamer.audit.svc.AuditLogService
import nl.parlio.api.tweedekamer.audit.types.ChangeEventSearchComposite
import nl.parlio.api.tweedekamer.person.root.dto.PersonSyncFeedUpdateDto
import nl.parlio.tweedekamer.gen.graphql.types.ChangeEvent
import nl.parlio.tweedekamer.gen.graphql.types.GenericChangeEvent
import nl.parlio.tweedekamer.gen.graphql.types.Person
import nl.parlio.tweedekamer.gen.graphql.types.PersonSyncFeedUpdateEvent
import org.dataloader.MappedBatchLoader

@DgsDataLoader(name = "ChangeEventBySearchCompositeDataLoader")
class ChangeEventBySearchCompositeDataLoader(
    private val auditLogService: AuditLogService,
    private val changeEventFactory: ChangeEventFactory
) : MappedBatchLoader<ChangeEventSearchComposite<Long>, List<ChangeEvent>> {
    override fun load(
        keys: Set<ChangeEventSearchComposite<Long>>
    ): CompletionStage<Map<ChangeEventSearchComposite<Long>, List<ChangeEvent>>> {
        return CompletableFuture.supplyAsync {
            auditLogService
                .findMultipleChangeHistory(keys)
                .mapValues { it.value.map(::mapPersonChangeEventTo) }
                .fillMissingKeys(keys, emptyList())
        }
    }

    fun mapPersonChangeEventTo(left: ChangeEventDto): ChangeEvent {
        if (left is PersonSyncFeedUpdateDto) {
            return PersonSyncFeedUpdateEvent(
                Relay.toGlobalId("ChangeEvent", left.id),
                Person.newBuilder().id(Relay.toGlobalId("Person", left.personId)).build(),
                left.op,
                null
            )
        }
        if (left is GenericChangeEventDto) {
            return GenericChangeEvent(Relay.toGlobalId("ChangeEvent", left.id), left.op, null)
        }
        TODO()
    }
}
