package nl.parlio.api.tweedekamer.audit

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import nl.parlio.api.core.ext.convertExact
import nl.parlio.api.tweedekamer.audit.dto.ChangeEntryDto
import nl.parlio.api.tweedekamer.audit.dto.ChangeEntryKeyDto
import nl.parlio.api.tweedekamer.audit.dto.StringChangeEntryDto
import nl.parlio.tweedekamer.gen.jooq.tables.records.QChangeEventEntryRecord
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Component

@Component
class ChangeEventFactory(
    private val conversionService: ConversionService,
    private val objectMapper: ObjectMapper
) {

    fun decipherChangeRecord(eventEntry: QChangeEventEntryRecord): ChangeEntryDto {
        val key = conversionService.convertExact<ChangeEntryKeyDto>(eventEntry.key)

        val dataTree = objectMapper.readTree(eventEntry.data)
        val changeType = dataTree.get("t")
        if (changeType == null || !changeType.isTextual) {
            throw IllegalArgumentException(
                "Unrecognized ChangeEvent type (was: '${changeType}'): $dataTree, record: $eventEntry"
            )
        }

        val entry: ChangeEntryDto =
            when (val textType = changeType.textValue()) {
                "s" -> decipherStringChangeEvent(key, dataTree)
                else ->
                    throw IllegalArgumentException(
                        "Unknown ChangeEvent type: '$textType', record: $eventEntry (tree: ${dataTree})"
                    )
            }
        return entry
    }

    private fun decipherStringChangeEvent(
        key: ChangeEntryKeyDto,
        tree: JsonNode
    ): StringChangeEntryDto {
        val aNode = tree.get("a")
        val a: String? =
            when {
                aNode == null ->
                    throw IllegalArgumentException("Tree node 'a' must exist (tree: $tree)")
                aNode.isTextual -> aNode.textValue()
                aNode.isNull -> null
                else ->
                    throw IllegalArgumentException(
                        "Tree node 'a' must be 'null' or textual (tree: $tree"
                    )
            }
        val bNode = tree.get("b")
        val b: String? =
            when {
                bNode == null ->
                    throw IllegalArgumentException("Tree node 'b' must exist (tree: $tree)")
                bNode.isTextual -> bNode.textValue()
                bNode.isNull -> null
                else ->
                    throw IllegalArgumentException(
                        "Tree node 'b' must be 'null' or textual (tree: $tree"
                    )
            }
        return StringChangeEntryDto(a, b, key)
    }
}
