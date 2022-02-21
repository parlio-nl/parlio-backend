package nl.parlio.api.core.relay.connection

import graphql.relay.Connection
import graphql.relay.DefaultConnection
import graphql.relay.DefaultConnectionCursor
import graphql.relay.DefaultEdge
import graphql.relay.DefaultPageInfo
import graphql.schema.DataFetchingEnvironment

object RelayConnection {

    fun <T> extractConnectionArguments(
        dfe: DataFetchingEnvironment,
        cursorDecoder: (String) -> T
    ): RelayConnectionArgs<T> {
        val afterArg: Any? = dfe.getArgument("after")
        val beforeArg: Any? = dfe.getArgument("before")
        val firstArg: Any? = dfe.getArgument("first")
        val lastArg: Any? = dfe.getArgument("last")
        if (firstArg != null && lastArg != null) {
            throw RelayCursorException(
                "Specifying both `first` and `last` to paginate `${dfe.field.name}` is not supported"
            )
        }
        if (afterArg !is String?) {
            throw RelayCursorException("Argument `after` is not a String (should never happen)")
        }
        if (beforeArg !is String?) {
            throw RelayCursorException("Argument `before` is not a String (should never happen)")
        }
        if (firstArg !is Int?) {
            throw RelayCursorException("Argument `first` is not an Int (should never happen)")
        }
        if (lastArg !is Int?) {
            throw RelayCursorException("Argument `last` is not an Int (should never happen)")
        }
        if (firstArg is Int && firstArg < 0) {
            throw RelayCursorException("Argument `first` must not be negative")
        }
        if (lastArg is Int && lastArg < 0) {
            throw RelayCursorException("Argument `last` must not be negative")
        }
        val after: T? =
            try {
                afterArg?.run(cursorDecoder)
            } catch (e: Throwable) {
                throw RelayCursorException("Failed to decode `after` cursor", e)
            }
        val before: T? =
            try {
                beforeArg?.run(cursorDecoder)
            } catch (e: Throwable) {
                throw RelayCursorException("Failed to decode `before` cursor", e)
            }
        return RelayConnectionArgs(after, before, firstArg, lastArg)
    }

    fun getCount(connectionArgs: RelayConnectionArgs<*>): Number {
        if (connectionArgs.first != null && connectionArgs.last != null) {
            throw RelayCursorException("Specifying both `first` and `last` is not supported")
            // TODO in `extractConnectionArguments`?
        }
        return connectionArgs.first
            ?: connectionArgs.last
                ?: throw RelayCursorException("Either `first` or `last` must be specified")
    }

    @Deprecated("TODO - Remove?")
    // TODO - Remove?
    private fun <T> createConnection(
        fromList: List<T>,
        cursorFactory: (T) -> String
    ): Connection<T> {
        val edges = fromList.map { DefaultEdge(it, DefaultConnectionCursor(cursorFactory(it))) }
        val pageInfo =
            DefaultPageInfo(edges.firstOrNull()?.cursor, edges.lastOrNull()?.cursor, false, false)
        return DefaultConnection(edges, pageInfo)
    }

    fun <I, O> createConnectionOnEach(
        fromList: List<I>,
        cursorFactory: (I) -> String,
        edgeFactory: (I) -> O
    ): Connection<O> {
        val edges =
            fromList.map {
                DefaultEdge(edgeFactory(it), DefaultConnectionCursor(cursorFactory(it)))
            }
        val pageInfo =
            DefaultPageInfo(edges.firstOrNull()?.cursor, edges.lastOrNull()?.cursor, false, false)
        return DefaultConnection(edges, pageInfo)
    }
}
