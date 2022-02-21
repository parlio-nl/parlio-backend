package nl.parlio.api.tweedekamer.audit.graphql.dataloader

import com.netflix.graphql.dgs.DgsDataLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import nl.parlio.api.core.ext.convertList
import nl.parlio.api.core.ext.fillMissingKeys
import nl.parlio.api.tweedekamer.audit.dto.ChangeEntryDto
import nl.parlio.api.tweedekamer.audit.svc.AuditLogService
import nl.parlio.tweedekamer.gen.graphql.types.ChangeEntry
import org.dataloader.MappedBatchLoader
import org.springframework.core.convert.ConversionService

@DgsDataLoader(name = "ChangeEntryByChangeEventIdDataLoader")
class ChangeEntryByChangeEventIdDataLoader(
    private val auditLogService: AuditLogService,
    private val conversionService: ConversionService
) : MappedBatchLoader<Long, List<ChangeEntry>> {
    override fun load(keys: Set<Long>): CompletionStage<Map<Long, List<ChangeEntry>>> {
        return CompletableFuture.supplyAsync {
            auditLogService
                .findMultipleChangeEntries(keys)
                .mapValues { (_, entries) ->
                    conversionService.convertList<ChangeEntryDto, ChangeEntry>(entries)
                }
                .fillMissingKeys(keys) { emptyList() }
        }
    }
}
