package nl.parlio.api.tweedekamer.person.root.graphql.dataloader

import com.netflix.graphql.dgs.DgsDataLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import nl.parlio.api.core.ext.fillMissingKeys
import nl.parlio.api.tweedekamer.person.root.svc.PersonService
import nl.parlio.tweedekamer.gen.graphql.types.Person
import org.dataloader.MappedBatchLoader
import org.dataloader.Try
import org.springframework.core.convert.ConversionService

@DgsDataLoader(name = "PersonByIdDataLoader")
class PersonByIdDataLoader(
    private val personService: PersonService,
    private val conversionService: ConversionService
) : MappedBatchLoader<Long, Try<Person>> {
    override fun load(keys: Set<Long>): CompletionStage<Map<Long, Try<Person>>> {
        return CompletableFuture.supplyAsync {
            personService
                .findPeople(keys)
                .mapValues { conversionService.convert(it.value, Person::class.java)!! }
                .mapValues { Try.succeeded(it.value) }
                .fillMissingKeys(keys) { Try.failed(Throwable("User $it does not exist")) }
        }
    }
}
