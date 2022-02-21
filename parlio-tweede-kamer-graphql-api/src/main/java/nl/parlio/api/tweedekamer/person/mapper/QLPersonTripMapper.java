package nl.parlio.api.tweedekamer.person.mapper;

import nl.parlio.api.tweedekamer.person.trip.dto.PersonTripDto;
import nl.parlio.tweedekamer.gen.graphql.types.PersonTrip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("NullableProblems")
@Mapper(componentModel = "spring")
public interface QLPersonTripMapper extends Converter<PersonTripDto, PersonTrip> {

  @Mapping(
      target = "id",
      expression = "java(nl.parlio.api.core.relay.Relay.toGlobalId(\"PersonTrip\", s.id()))")
  @Mapping(source = "destination", target = "destination")
  @Mapping(source = "purpose", target = "purpose")
  @Mapping(source = "paidBy", target = "paidBy")
  @Mapping(source = "endDate", target = "endDate")
  @Mapping(source = "startDate", target = "startDate")
  PersonTrip convert(PersonTripDto s);
}
