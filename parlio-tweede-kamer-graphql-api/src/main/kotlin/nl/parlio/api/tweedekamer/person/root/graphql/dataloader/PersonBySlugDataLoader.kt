package nl.parlio.api.tweedekamer.person.root.graphql.dataloader

import com.netflix.graphql.dgs.DgsDataLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import nl.parlio.api.core.ext.convertValues
import nl.parlio.api.core.ext.fillMissingKeys
import nl.parlio.api.tweedekamer.person.root.dto.PersonDto
import nl.parlio.api.tweedekamer.person.root.svc.PersonService
import nl.parlio.tweedekamer.gen.graphql.types.Person
import org.dataloader.MappedBatchLoader
import org.dataloader.Try
import org.springframework.core.convert.ConversionService

@DgsDataLoader(name = "PersonBySlugDataLoader")
class PersonBySlugDataLoader(
    private val personService: PersonService,
    private val conversionService: ConversionService
) : MappedBatchLoader<String, Try<Person>> {
    override fun load(keys: Set<String>): CompletionStage<Map<String, Try<Person>>> {
        return CompletableFuture.supplyAsync {
            personService
                .findPeopleBySlugs(keys)
                .convertValues<String, PersonDto, Person>(conversionService)
                .mapValues { Try.succeeded(it.value) }
                .fillMissingKeys(keys) {
                    Try.failed(Throwable("User with slug $it does not exist"))
                }
        }
    }
}
