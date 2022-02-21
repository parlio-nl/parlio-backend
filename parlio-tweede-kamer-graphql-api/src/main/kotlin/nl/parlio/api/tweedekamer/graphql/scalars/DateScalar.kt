package nl.parlio.api.tweedekamer.graphql.scalars

import com.netflix.graphql.dgs.DgsScalar
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingSerializeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@DgsScalar(name = "Date")
class DateScalar : Coercing<LocalDate, String> {

    override fun serialize(dataFetcherResult: Any): String {
        if (dataFetcherResult is LocalDate) {
            return dataFetcherResult.format(DateTimeFormatter.ISO_LOCAL_DATE)
        }
        throw CoercingSerializeException("Not a valid Date")
    }

    override fun parseValue(input: Any): LocalDate {
        return LocalDate.parse(input.toString(), DateTimeFormatter.ISO_LOCAL_DATE)
    }

    override fun parseLiteral(input: Any): LocalDate {
        if (input is StringValue) {
            return LocalDate.parse(input.value, DateTimeFormatter.ISO_LOCAL_DATE)
        }
        throw CoercingParseLiteralException("Value is not a valid ISO date")
    }
}
