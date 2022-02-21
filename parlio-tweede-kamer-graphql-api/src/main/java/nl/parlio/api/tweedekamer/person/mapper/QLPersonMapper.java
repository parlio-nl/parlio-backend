package nl.parlio.api.tweedekamer.person.mapper;

import nl.parlio.api.tweedekamer.person.root.dto.PersonDto;
import nl.parlio.tweedekamer.gen.graphql.types.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("NullableProblems")
@Mapper(componentModel = "spring")
public interface QLPersonMapper extends Converter<PersonDto, Person> {

  @Mapping(target = "changeHistory", ignore = true)
  @Mapping(
      target = "id",
      expression = "java(nl.parlio.api.core.relay.Relay.toGlobalId(\"User\", s.getId()))")
  @Mapping(source = "initials", target = "nameInitials")
  Person convert(PersonDto s);
}
