package nl.parlio.api.tweedekamer.person.root.svc

import nl.parlio.api.core.ext.convertList
import nl.parlio.api.core.relay.connection.RelayConnectionArgs
import nl.parlio.api.tweedekamer.person.root.dto.PersonDto
import nl.parlio.api.tweedekamer.shared.utils.RelayConnectionOoq.relayLimit
import nl.parlio.api.tweedekamer.shared.utils.RelayConnectionOoq.relayOrderBy
import nl.parlio.api.tweedekamer.shared.utils.RelayConnectionOoq.relayWhere
import nl.parlio.tweedekamer.gen.jooq.tables.PersonTable.PERSON
import org.jooq.DSLContext
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service

@Service
class PersonServiceImpl(
    private val dsl: DSLContext,
    private val conversionService: ConversionService
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

    override fun findPeopleByConnection(args: RelayConnectionArgs<Long>): List<PersonDto> {
        val people =
            dsl.select(
                    PERSON.PERSON_ID,
                    PERSON.SLUG,
                    PERSON.FAMILY_NAME,
                    PERSON.NAME_INITIALS,
                    PERSON.FIRST_NAME
                )
                .from(PERSON)
                .where(relayWhere(PERSON.PERSON_ID, args))
                .orderBy(relayOrderBy(PERSON.PERSON_ID, args))
                .limit(relayLimit(args))
                .fetchInto(PERSON.recordType)
        return conversionService.convertList(people)
    }
}
