package nl.parlio.api.tweedekamer.person.mapper;

import nl.parlio.api.tweedekamer.person.gift.dto.PersonGiftDto;
import nl.parlio.tweedekamer.gen.jooq.tables.records.QPersonGiftRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("NullableProblems")
@Mapper(componentModel = "spring")
public interface QPersonGiftMapper extends Converter<QPersonGiftRecord, PersonGiftDto> {

  @Mapping(source = "personId", target = "personId")
  @Mapping(source = "personGiftId", target = "id")
  @Mapping(source = "date", target = "date")
  @Mapping(source = "description", target = "description")
  PersonGiftDto convert(QPersonGiftRecord person);
}
