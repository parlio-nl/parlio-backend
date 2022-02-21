package nl.parlio.api.tweedekamer.shared.utils

import nl.parlio.api.core.relay.connection.RelayConnection
import nl.parlio.api.core.relay.connection.RelayConnectionArgs
import nl.parlio.api.core.relay.connection.RelayCursorException
import org.jooq.Condition
import org.jooq.OrderField
import org.jooq.TableField

internal object RelayConnectionOoq {

    private fun <T> buildRelayTableConstraint(
        tableField: TableField<*, T>,
        connectionArgs: RelayConnectionArgs<T>
    ): Condition? {
        val afterCondition =
            if (connectionArgs.after != null) {
                tableField.greaterThan(connectionArgs.after)
            } else null

        if (connectionArgs.before != null) {
            val lastCond = tableField.lessThan(connectionArgs.before)
            return afterCondition?.and(lastCond) ?: lastCond
        }

        return afterCondition
    }

    fun <T, ID> relayWhere(
        cursorField: TableField<*, T>,
        idField: TableField<*, ID>,
        idToEq: ID,
        connectionArgs: RelayConnectionArgs<T>
    ): Condition {
        val connectionWhere = buildRelayTableConstraint(cursorField, connectionArgs)
        val idEq = idField.eq(idToEq)
        return connectionWhere?.and(idEq) ?: idEq
    }

    fun <T> relayWhere(
        cursorField: TableField<*, T>,
        connectionArgs: RelayConnectionArgs<T>
    ): Condition? {
        return buildRelayTableConstraint(cursorField, connectionArgs)
    }

    private fun isAscending(connectionArgs: RelayConnectionArgs<*>): Boolean {
        if (connectionArgs.first != null && connectionArgs.last != null) {
            throw RelayCursorException("Specifying both `first` and `last` is not supported")
            // TODO in `extractConnectionArguments`?
        }
        return connectionArgs.first != null
    }

    fun <T> relayOrderBy(
        idField: TableField<*, T>,
        connectionArgs: RelayConnectionArgs<T>
    ): OrderField<T> {
        return if (isAscending(connectionArgs)) idField.asc() else idField.desc()
    }

    fun relayLimit(connectionArgs: RelayConnectionArgs<*>): Number {
        return RelayConnection.getCount(connectionArgs)
    }
}
