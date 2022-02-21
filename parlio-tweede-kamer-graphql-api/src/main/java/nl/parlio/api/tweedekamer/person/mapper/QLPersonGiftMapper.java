package nl.parlio.api.tweedekamer.person.mapper;

import nl.parlio.api.tweedekamer.person.gift.dto.PersonGiftDto;
import nl.parlio.tweedekamer.gen.graphql.types.PersonGift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("NullableProblems")
@Mapper(componentModel = "spring")
public interface QLPersonGiftMapper extends Converter<PersonGiftDto, PersonGift> {

  @Mapping(
      target = "id",
      expression = "java(nl.parlio.api.core.relay.Relay.toGlobalId(\"PersonGift\", s.id()))")
  //  @Mapping(
  //      target = "person",
  //      expression =
  //
  // "java(nl.parlio.tweedekamer.gen.graphql.types.Person.newBuilder().id(nl.parlio.api.core.relay.Relay.toGlobalId(\"Person\", s.personId())).build())")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "date", target = "date")
  PersonGift convert(PersonGiftDto s);
}
