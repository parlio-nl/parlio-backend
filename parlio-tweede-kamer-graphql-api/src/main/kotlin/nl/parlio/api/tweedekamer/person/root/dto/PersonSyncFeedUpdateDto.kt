package nl.parlio.api.tweedekamer.person.root.dto

import nl.parlio.api.tweedekamer.person.PersonChangeOperation

data class PersonSyncFeedUpdateDto(
    override val id: Long,
    override val personId: Long,
    override val op: String = PersonChangeOperation.TK_SYNC_FEED_INGEST
) : PersonChangeEventDto {}
