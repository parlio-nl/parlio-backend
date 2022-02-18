package nl.parlio.api.tweedekamer.person.mapper;

import nl.parlio.api.tweedekamer.person.dto.PersonDto;
import nl.parlio.tweedekamer.gen.jooq.tables.records.QPersonRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("NullableProblems")
@Mapper(componentModel = "spring")
public interface QPersonMapper extends Converter<QPersonRecord, PersonDto> {

  @Mapping(source = "personId", target = "id")
  @Mapping(source = "nameInitials", target = "initials")
  PersonDto convert(QPersonRecord person);
}
