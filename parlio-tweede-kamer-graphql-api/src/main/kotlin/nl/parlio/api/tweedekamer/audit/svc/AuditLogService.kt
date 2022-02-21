package nl.parlio.api.tweedekamer.audit.svc

import nl.parlio.api.tweedekamer.audit.dto.ChangeEntryDto
import nl.parlio.api.tweedekamer.audit.dto.ChangeEventDto
import nl.parlio.api.tweedekamer.audit.types.ChangeEventSearchComposite

interface AuditLogService {

    fun <T : Number> findMultipleChangeHistory(
        searchComposites: Set<ChangeEventSearchComposite<T>>
    ): Map<ChangeEventSearchComposite<T>, List<ChangeEventDto>>

    fun <T : Number> findMultipleChangeEntries(changeEventIds: Set<T>): Map<T, List<ChangeEntryDto>>
}
