package nl.parlio.api.core.relay.connection

class RelayCursorException : RuntimeException {
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(message: String) : super(message)
}
