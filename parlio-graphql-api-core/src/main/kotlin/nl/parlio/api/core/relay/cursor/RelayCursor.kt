package nl.parlio.api.core.relay.cursor

@JvmRecord data class RelayCursor<T>(val cursorType: String, val cursorData: T)
