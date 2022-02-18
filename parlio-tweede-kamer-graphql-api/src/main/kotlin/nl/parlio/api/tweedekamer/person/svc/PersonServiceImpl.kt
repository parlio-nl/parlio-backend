package nl.parlio.api.tweedekamer.person.svc

import com.fasterxml.jackson.databind.ObjectMapper
import nl.parlio.api.tweedekamer.person.PersonChangeOperation
import nl.parlio.api.tweedekamer.person.dto.PersonChangeEventDto
import nl.parlio.api.tweedekamer.person.dto.PersonDto
import nl.parlio.api.tweedekamer.person.dto.PersonSyncFeedUpdateDto
import nl.parlio.api.tweedekamer.shared.types.changes.ChangeEntryDto
import nl.parlio.api.tweedekamer.shared.types.changes.ChangeEntryKeyDto
import nl.parlio.api.tweedekamer.shared.types.changes.StringChangeEntryDto
import nl.parlio.tweedekamer.gen.jooq.tables.ChangeEventEntryTable.CHANGE_EVENT_ENTRY
import nl.parlio.tweedekamer.gen.jooq.tables.ChangeEventTable.CHANGE_EVENT
import nl.parlio.tweedekamer.gen.jooq.tables.PersonTable.PERSON
import nl.parlio.tweedekamer.gen.jooq.tables.records.QChangeEventEntryRecord
import nl.parlio.tweedekamer.gen.jooq.tables.records.QChangeEventRecord
import org.jooq.DSLContext
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service

@Service
class PersonServiceImpl(
    private val dsl: DSLContext,
    private val conversionService: ConversionService,
    private val objectMapper: ObjectMapper
) : PersonService {

    override fun findPeople(ids: Set<Long>): Map<Long, PersonDto> {
        val people =
            dsl.select(
                    PERSON.PERSON_ID,
                    PERSON.SLUG,
                    PERSON.FAMILY_NAME,
                    PERSON.NAME_INITIALS,
                    PERSON.FIRST_NAME
                )
                .from(PERSON)
                .where(PERSON.PERSON_ID.`in`(ids))
                .fetchInto(PERSON)
        return people
            .mapNotNull { conversionService.convert(it, PersonDto::class.java) }
            .associateBy { it.id }
    }

    override fun findMultipleChangeHistory(
        personIds: Set<Long>
    ): Map<Long, List<PersonChangeEventDto>> {
        val events: Map<Long, List<QChangeEventRecord>> =
            dsl.select(
                    CHANGE_EVENT.CHANGE_EVENT_ID,
                    CHANGE_EVENT.OPERATION_NAME,
                    CHANGE_EVENT.MODEL,
                    CHANGE_EVENT.REF
                )
                .from(CHANGE_EVENT)
                .where(CHANGE_EVENT.MODEL.eq("Person").and(CHANGE_EVENT.REF.`in`(personIds)))
                .fetchGroups(CHANGE_EVENT.REF, CHANGE_EVENT.recordType)

        return events.mapValues { (personId, events) ->
            events.map { event -> mapChangeEvent(personId, event) }
        }
    }

    fun mapChangeEvent(personId: Long, event: QChangeEventRecord): PersonChangeEventDto {
        if (event.operationName == PersonChangeOperation.TK_SYNC_FEED_INGEST) {
            return PersonSyncFeedUpdateDto(event.changeEventId, personId)
        }
        throw RuntimeException("Unknown event: $event, for person: $personId")
    }

    override fun findChangeLog(changeEventIds: Set<Long>): Map<Long, List<ChangeEntryDto>> {
        val events: Map<Long, List<QChangeEventEntryRecord>> =
            dsl.select(
                    CHANGE_EVENT_ENTRY.CHANGE_EVENT_ID,
                    CHANGE_EVENT_ENTRY.KEY,
                    CHANGE_EVENT_ENTRY.DATA
                )
                .from(CHANGE_EVENT_ENTRY)
                .where(CHANGE_EVENT_ENTRY.CHANGE_EVENT_ID.`in`(changeEventIds))
                .fetchGroups(CHANGE_EVENT_ENTRY.CHANGE_EVENT_ID, CHANGE_EVENT_ENTRY.recordType)

        return events.mapValues { (_, changeEntries) -> changeEntries.map(::mapChangeEntry) }
    }

    fun mapChangeEntry(eventEntry: QChangeEventEntryRecord): ChangeEntryDto {
        // TODO Improve this method
        val key = conversionService.convert(eventEntry.key, ChangeEntryKeyDto::class.java)!!

        val dataTree = objectMapper.readTree(eventEntry.data)
        val t = dataTree.get("t")
        if (t == null || !t.isTextual) {
            TODO("Incorrect t: $dataTree")
        }

        val entry: ChangeEntryDto =
            when (t.textValue()) {
                "s" ->
                    StringChangeEntryDto(
                        dataTree.get("b").textValue(),
                        dataTree.get("a").textValue(),
                        key
                    )
                else -> TODO()
            }
        return entry
    }
}
