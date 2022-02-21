package nl.parlio.api.tweedekamer.audit.svc

import nl.parlio.api.tweedekamer.audit.ChangeEventFactory
import nl.parlio.api.tweedekamer.audit.dto.ChangeEntryDto
import nl.parlio.api.tweedekamer.audit.dto.ChangeEventDto
import nl.parlio.api.tweedekamer.audit.dto.GenericChangeEventDto
import nl.parlio.api.tweedekamer.audit.types.ChangeEventModel
import nl.parlio.api.tweedekamer.audit.types.ChangeEventSearchComposite
import nl.parlio.api.tweedekamer.person.PersonChangeOperation
import nl.parlio.api.tweedekamer.person.root.dto.PersonSyncFeedUpdateDto
import nl.parlio.tweedekamer.gen.jooq.tables.ChangeEventEntryTable.CHANGE_EVENT_ENTRY
import nl.parlio.tweedekamer.gen.jooq.tables.ChangeEventTable.CHANGE_EVENT
import nl.parlio.tweedekamer.gen.jooq.tables.records.QChangeEventEntryRecord
import nl.parlio.tweedekamer.gen.jooq.tables.records.QChangeEventRecord
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.impl.DSL.noCondition
import org.springframework.stereotype.Service

@Service
class AuditLogServiceImpl(
    private val dsl: DSLContext,
    private val changeEventFactory: ChangeEventFactory
) : AuditLogService {

    override fun <T : Number> findMultipleChangeHistory(
        searchComposites: Set<ChangeEventSearchComposite<T>>
    ): Map<ChangeEventSearchComposite<T>, List<ChangeEventDto>> {
        val allCondition =
            searchComposites
                .groupBy { it.eventModel }
                .mapValues { (model, searchComposites) ->
                    return@mapValues CHANGE_EVENT
                        .MODEL
                        .eq(model.shortName)
                        .and(
                            CHANGE_EVENT.REF.`in`(
                                searchComposites.map {
                                    assert(it.eventModel == model)
                                    return@map it.ref
                                }
                            )
                        )
                }
                .values
                .fold(noCondition(), Condition::or)

        val changeEvents: Map<Record, List<QChangeEventRecord>> =
            dsl.select(
                    CHANGE_EVENT.CHANGE_EVENT_ID,
                    CHANGE_EVENT.OPERATION_NAME,
                    CHANGE_EVENT.MODEL,
                    CHANGE_EVENT.REF
                )
                .from(CHANGE_EVENT)
                .where(allCondition)
                .fetchGroups(arrayOf(CHANGE_EVENT.MODEL, CHANGE_EVENT.REF), CHANGE_EVENT.recordType)

        return changeEvents
            .mapKeys { (record, _) ->
                @Suppress("UNCHECKED_CAST")
                ChangeEventSearchComposite(
                    ChangeEventModel.valueOfShortName(record[CHANGE_EVENT.MODEL]),
                    record[CHANGE_EVENT.REF] as T
                )
            }
            .mapValues { (tempComposite, changeEvents) ->
                changeEvents.map { changeEventRecord ->
                    mapChangeEvent(tempComposite.ref.toLong(), changeEventRecord)
                }
            }
    }

    private fun mapChangeEvent(personId: Long, event: QChangeEventRecord): ChangeEventDto {
        if (event.operationName == PersonChangeOperation.TK_SYNC_FEED_INGEST) {
            return PersonSyncFeedUpdateDto(event.changeEventId, personId)
        }
        if (event.operationName == "GENERIC") {
            return GenericChangeEventDto(event.changeEventId, "GENERIC")
        }
        throw RuntimeException("Unknown event: $event, for person: $personId")
    }

    override fun <T : Number> findMultipleChangeEntries(
        changeEventIds: Set<T>
    ): Map<T, List<ChangeEntryDto>> {
        val events: Map<Long, List<QChangeEventEntryRecord>> =
            dsl.select(
                    CHANGE_EVENT_ENTRY.CHANGE_EVENT_ID,
                    CHANGE_EVENT_ENTRY.KEY,
                    CHANGE_EVENT_ENTRY.DATA
                )
                .from(CHANGE_EVENT_ENTRY)
                .where(CHANGE_EVENT_ENTRY.CHANGE_EVENT_ID.`in`(changeEventIds))
                .fetchGroups(CHANGE_EVENT_ENTRY.CHANGE_EVENT_ID, CHANGE_EVENT_ENTRY.recordType)

        return events
            .mapValues { (_, changeEntries) ->
                changeEntries.map(changeEventFactory::decipherChangeRecord)
            }
            .mapKeys { @Suppress("UNCHECKED_CAST") (it.key as T) }
    }
}
