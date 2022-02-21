package nl.parlio.api.tweedekamer.person.gift.svc

import nl.parlio.api.core.ext.convertList
import nl.parlio.api.core.relay.connection.RelayConnectionArgs
import nl.parlio.api.tweedekamer.person.gift.dto.PersonGiftDto
import nl.parlio.api.tweedekamer.shared.utils.RelayConnectionOoq.relayLimit
import nl.parlio.api.tweedekamer.shared.utils.RelayConnectionOoq.relayOrderBy
import nl.parlio.api.tweedekamer.shared.utils.RelayConnectionOoq.relayWhere
import nl.parlio.tweedekamer.gen.jooq.tables.PersonGiftTable.PERSON_GIFT
import org.jooq.DSLContext
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service

@Service
class PersonGiftServiceImpl(
    private val dsl: DSLContext,
    private val conversionService: ConversionService
) : PersonGiftService {

    override fun findGiftsByPerson(
        personId: Long,
        args: RelayConnectionArgs<Long>
    ): List<PersonGiftDto> {
        val gifts =
            dsl.selectFrom(PERSON_GIFT)
                .where(
                    relayWhere(PERSON_GIFT.PERSON_GIFT_ID, PERSON_GIFT.PERSON_ID, personId, args)
                )
                .orderBy(relayOrderBy(PERSON_GIFT.PERSON_GIFT_ID, args))
                .limit(relayLimit(args))
                .fetchInto(PERSON_GIFT.recordType)
        return conversionService.convertList(gifts)
    }
}
