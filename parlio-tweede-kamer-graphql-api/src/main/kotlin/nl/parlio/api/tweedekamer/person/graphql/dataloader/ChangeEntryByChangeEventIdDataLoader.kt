package nl.parlio.api.tweedekamer.person.graphql.dataloader

import com.netflix.graphql.dgs.DgsDataLoader
import nl.parlio.api.tweedekamer.DataLoaderUtils.fillMissingKeys
import nl.parlio.api.tweedekamer.person.svc.PersonService
import nl.parlio.tweedekamer.gen.graphql.types.ChangeEntry
import org.dataloader.MappedBatchLoader
import org.springframework.core.convert.ConversionService
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@DgsDataLoader(name = "ChangeEntryByChangeEventIdDataLoader")
class ChangeEntryByChangeEventIdDataLoader(
    private val personService: PersonService,
    private val conversionService: ConversionService
) : MappedBatchLoader<Long, List<ChangeEntry>> {
    override fun load(keys: Set<Long>): CompletionStage<Map<Long, List<ChangeEntry>>> {
        return CompletableFuture.supplyAsync {
            personService.findChangeLog(keys)
                .mapValues { (_, entries) ->
                    entries.mapNotNull { entry ->
                        conversionService.convert(entry, ChangeEntry::class.java)
                    }
                }
                .fillMissingKeys(keys) { emptyList() }
        }
    }
}
