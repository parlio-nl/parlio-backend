package nl.parlio.api.tweedekamer.person.mapper;

import nl.parlio.api.tweedekamer.audit.dto.ChangeEntryKeyDto;
import nl.parlio.tweedekamer.gen.graphql.types.ChangeEntryKey;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("NullableProblems")
@Mapper(componentModel = "spring")
public interface EventEntryKeyMapper extends Converter<ChangeEntryKey, ChangeEntryKeyDto> {

  @Override
  ChangeEntryKeyDto convert(ChangeEntryKey source);
}
