package nl.parlio.api.tweedekamer.audit.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import java.util.concurrent.CompletableFuture
import nl.parlio.api.core.ext.getMappedBatchLoader
import nl.parlio.api.core.relay.Relay
import nl.parlio.api.tweedekamer.audit.graphql.dataloader.ChangeEntryByChangeEventIdDataLoader
import nl.parlio.tweedekamer.gen.graphql.DgsConstants
import nl.parlio.tweedekamer.gen.graphql.types.ChangeEntry
import nl.parlio.tweedekamer.gen.graphql.types.ChangeEvent

@DgsComponent
class AuditLogController {

    @DgsData(
        parentType = DgsConstants.CHANGEEVENT.TYPE_NAME,
        field = DgsConstants.CHANGEEVENT.Changes
    )
    fun changes(dfe: DgsDataFetchingEnvironment): CompletableFuture<List<ChangeEntry>> {
        val parentGlobalId = dfe.getSource<ChangeEvent>().id
        val id = Relay.assertAndExtractId(parentGlobalId, "ChangeEvent")
        val dl =
            dfe.getMappedBatchLoader<
                Long, List<ChangeEntry>, ChangeEntryByChangeEventIdDataLoader>()
        return dl.load(id)
    }
}
