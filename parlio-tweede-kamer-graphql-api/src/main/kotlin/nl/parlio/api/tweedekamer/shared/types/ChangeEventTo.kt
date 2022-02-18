package nl.parlio.api.tweedekamer.shared.types

interface ChangeEventTo {
    val id: Long
    val op: String
}