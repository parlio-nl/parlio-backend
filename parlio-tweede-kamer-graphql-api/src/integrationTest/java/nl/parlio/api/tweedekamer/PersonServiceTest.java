package nl.parlio.api.tweedekamer;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import nl.parlio.api.tweedekamer.person.root.dto.PersonDto;
import nl.parlio.api.tweedekamer.person.root.svc.PersonService;
import nl.parlio.tweedekamer.gen.jooq.tables.PersonTable;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonServiceTest extends SpringIntegrationTest {

  @Autowired PersonService personService;

  @Autowired DSLContext dslContext;

  @Test
  void findPeople() {
    dslContext
        .insertInto(PersonTable.PERSON)
        .set(PersonTable.PERSON.PERSON_ID, 2L)
        .set(PersonTable.PERSON.NAME_INITIALS, "name-Initials")
        .set(PersonTable.PERSON.DATE_OF_BIRTH, LocalDateTime.now().toString())
        .set(PersonTable.PERSON.FIRST_NAME, "first_-Name")
        .set(PersonTable.PERSON.FAMILY_NAME, "family_name")
        .set(PersonTable.PERSON.SLUG, "a-slug")
        .execute();
    Map<Long, PersonDto> people = personService.findPeople(Set.of(2L));
    assertThat(people)
        .hasSize(1)
        .containsKey(2L)
        .extractingByKey(2L)
        .isNotNull()
        .satisfies($ -> assertThat($).extracting(PersonDto::getId).isEqualTo(2L))
        .satisfies($ -> assertThat($).extracting(PersonDto::getSlug).isEqualTo("a-slug"))
        .satisfies($ -> assertThat($).extracting(PersonDto::getFirstName).isEqualTo("first_-Name"))
        .satisfies(
            $ -> assertThat($).extracting(PersonDto::getFamilyName).isEqualTo("family_name"));
  }
}
