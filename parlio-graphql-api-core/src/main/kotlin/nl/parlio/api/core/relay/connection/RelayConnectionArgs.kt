package nl.parlio.api.core.relay.connection

@JvmRecord
data class RelayConnectionArgs<T>(
    val after: T?,
    val before: T?,
    val first: Int?,
    val last: Int?,
)
