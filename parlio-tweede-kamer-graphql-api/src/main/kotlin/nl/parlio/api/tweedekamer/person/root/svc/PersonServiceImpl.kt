package nl.parlio.api.tweedekamer.person.root.svc

import nl.parlio.api.core.ext.convertExact
import nl.parlio.api.core.ext.convertList
import nl.parlio.api.core.relay.connection.RelayConnectionArgs
import nl.parlio.api.tweedekamer.person.root.dto.PersonDto
import nl.parlio.api.tweedekamer.shared.utils.RelayConnectionOoq.relayLimit
import nl.parlio.api.tweedekamer.shared.utils.RelayConnectionOoq.relayOrderBy
import nl.parlio.api.tweedekamer.shared.utils.RelayConnectionOoq.relayWhere
import nl.parlio.tweedekamer.gen.jooq.tables.PersonTable.PERSON
import nl.parlio.tweedekamer.gen.jooq.tables.records.QPersonRecord
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service

@Service
class PersonServiceImpl(
    private val dsl: DSLContext,
    private val conversionService: ConversionService
) : PersonService {

    override fun findPeople(ids: Set<Long>): Map<Long, PersonDto> {
        val people = findPeopleBatched(PERSON.PERSON_ID.`in`(ids))
        return people.map { conversionService.convertExact<PersonDto>(it) }.associateBy { it.id }
    }

    override fun findPeopleBySlugs(slugs: Set<String>): Map<String, PersonDto> {
        val people = findPeopleBatched(PERSON.SLUG.`in`(slugs))
        return people.map { conversionService.convertExact<PersonDto>(it) }.associateBy { it.slug }
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

    private fun findPeopleBatched(whereCondition: Condition): Result<QPersonRecord> {
        return dsl.select(
                PERSON.PERSON_ID,
                PERSON.SLUG,
                PERSON.FAMILY_NAME,
                PERSON.NAME_INITIALS,
                PERSON.FIRST_NAME
            )
            .from(PERSON)
            .where(whereCondition)
            .fetchInto(PERSON)
    }
}
