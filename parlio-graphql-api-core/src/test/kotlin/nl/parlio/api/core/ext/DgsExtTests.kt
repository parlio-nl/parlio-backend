package nl.parlio.api.core.ext

import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.DgsDataLoader
import graphql.ExecutionInput
import graphql.GraphQL
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.InstanceOfAssertFactories
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderRegistry
import org.dataloader.MappedBatchLoader
import org.junit.jupiter.api.Test

class DgsExtTests {

    @DgsDataLoader(name = "test")
    object TestMappedBatchLoader : MappedBatchLoader<String, String> {
        override fun load(keys: Set<String>): CompletionStage<Map<String, String>> {
            return CompletableFuture.completedFuture(keys.associateWith { it })
        }
    }

    @Test
    fun testGetMappedBatchLoader() {
        val typeRegistry = SchemaParser().parse("type Query { ping(pong: String!): String! }")
        val runtimeWiring =
            RuntimeWiring.newRuntimeWiring()
                .type("Query") {
                    it.dataFetcher("ping") { env ->
                        DgsDataFetchingEnvironment(env)
                            .getMappedBatchLoader<
                                String, String, TestMappedBatchLoader>() // The function to test
                            .load(env.getArgument("pong"))
                    }
                }
                .build()
        val schema = SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring)
        val loaderDispatcher = DataLoaderDispatcherInstrumentation()
        val dataLoaderRegistry =
            DataLoaderRegistry.newRegistry()
                .register("test", DataLoaderFactory.newMappedDataLoader(TestMappedBatchLoader))
                .build()
        val ql = GraphQL.newGraphQL(schema).instrumentation(loaderDispatcher).build()
        val result =
            ql.execute(
                    ExecutionInput.newExecutionInput()
                        .query("{ a: ping(pong: \"a\") b: ping(pong: \"b\") }")
                        .dataLoaderRegistry(dataLoaderRegistry)
                        .build()
                )
                .toSpecification()
        assertThat(result)
            .hasSize(1)
            .extractingByKey("data", InstanceOfAssertFactories.MAP)
            .hasSize(2)
            .containsOnlyKeys("a", "b")
            .satisfies {
                assertThat(it).extractingByKey("a").isEqualTo("a")
                assertThat(it).extractingByKey("b").isEqualTo("b")
            }
    }
}
