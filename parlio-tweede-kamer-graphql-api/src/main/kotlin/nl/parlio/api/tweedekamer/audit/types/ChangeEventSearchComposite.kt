package nl.parlio.api.tweedekamer.audit.types

data class ChangeEventSearchComposite<T : Number>(val eventModel: ChangeEventModel, val ref: T)
