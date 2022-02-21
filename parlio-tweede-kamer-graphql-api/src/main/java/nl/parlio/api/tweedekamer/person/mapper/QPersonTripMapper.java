package nl.parlio.api.tweedekamer.person.mapper;

import nl.parlio.api.tweedekamer.person.trip.dto.PersonTripDto;
import nl.parlio.tweedekamer.gen.jooq.tables.records.QPersonTripRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("NullableProblems")
@Mapper(componentModel = "spring")
public interface QPersonTripMapper extends Converter<QPersonTripRecord, PersonTripDto> {

  @Mapping(source = "personTripId", target = "id")
  @Mapping(source = "destination", target = "destination")
  @Mapping(source = "paidBy", target = "paidBy")
  @Mapping(source = "startDate", target = "startDate")
  @Mapping(source = "endDate", target = "endDate")
  @Mapping(source = "purpose", target = "purpose")
  PersonTripDto convert(QPersonTripRecord person);
}
