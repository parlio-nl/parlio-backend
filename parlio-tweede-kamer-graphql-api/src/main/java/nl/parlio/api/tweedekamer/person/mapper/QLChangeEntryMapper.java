package nl.parlio.api.tweedekamer.person.mapper;

import nl.parlio.api.tweedekamer.shared.types.changes.ChangeEntryDto;
import nl.parlio.api.tweedekamer.shared.types.changes.StringChangeEntryDto;
import nl.parlio.tweedekamer.gen.graphql.types.ChangeEntry;
import nl.parlio.tweedekamer.gen.graphql.types.StringChangeEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("NullableProblems")
@Mapper(componentModel = "spring")
public interface QLChangeEntryMapper extends Converter<ChangeEntryDto, ChangeEntry> {

  default ChangeEntry convert(ChangeEntryDto entry) {
    if (entry instanceof StringChangeEntryDto) {
      StringChangeEntryDto s = (StringChangeEntryDto) entry;
      return convertS(s);
    }
    return null;
  }

  @Mapping(source = "key", target = "key")
  @Mapping(source = "oldValue", target = "oldValue")
  @Mapping(source = "newValue", target = "newValue")
  StringChangeEntry convertS(StringChangeEntryDto entry);
}
