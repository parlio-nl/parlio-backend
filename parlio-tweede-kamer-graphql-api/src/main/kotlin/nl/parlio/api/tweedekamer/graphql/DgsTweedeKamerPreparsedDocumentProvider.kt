package nl.parlio.api.tweedekamer.graphql

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.netflix.graphql.dgs.DgsComponent
import graphql.ExecutionInput
import graphql.execution.preparsed.PreparsedDocumentEntry
import graphql.execution.preparsed.PreparsedDocumentProvider
import java.time.Duration
import java.util.function.Function

@DgsComponent
class DgsTweedeKamerPreparsedDocumentProvider : PreparsedDocumentProvider {

    private val cache: Cache<String, PreparsedDocumentEntry> = Caffeine
        .newBuilder()
        .maximumSize(2500)
        .expireAfterAccess(Duration.ofHours(1))
        .build()

    override fun getDocument(
        executionInput: ExecutionInput,
        parseAndValidateFunction: Function<ExecutionInput, PreparsedDocumentEntry>
    ): PreparsedDocumentEntry {
        return cache.get(executionInput.query) { parseAndValidateFunction.apply(executionInput) }!!
    }
}