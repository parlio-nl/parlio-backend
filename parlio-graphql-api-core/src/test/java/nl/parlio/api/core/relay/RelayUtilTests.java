package nl.parlio.api.core.relay;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class RelayUtilTests {

  @DisplayName("Relay.toGlobalId with and without GlobalId object")
  @ParameterizedTest(
      name =
          "toGlobalId(\"{0}\", {1}) is equal to toGlobalId(new GlobalId(type = \"{0}\", id = {1}))")
  @CsvSource({"User, 1", "User, 2", "A, 3"})
  void toGlobalIdExplicitImplicitAreSame(String type, long id) {
    final String withoutObj = Relay.toGlobalId(type, id);
    final String withObj = Relay.toGlobalId(new GlobalId(type, id));
    assertThat(withoutObj).isEqualTo(withObj);
  }

  @ParameterizedTest(name = "toGlobalId(\"{0}\", {1}) == {2}")
  @CsvSource({"'User', 1, 'VXNlcjox'", "'', 2, 'OjI'", "'ABC', -1, 'QUJDOi0x'"})
  void globalIdEncodesCorrectly(String type, long id, String expected) {
    final var output = Relay.toGlobalId(type, id);
    assertThat(output).isEqualTo(expected);
  }

  @ParameterizedTest(name = "(type, id) = toGlobalIdOrNull(\"{0}\"); type == \"{1}\" && id == {2}")
  @CsvSource({"'VXNlcjox', 'User', 1", "'OjI', '', 2", "'QUJDOi0x', 'ABC', -1"})
  void toGlobalIdOrNullOnValid(String globalId, String expectedType, long expectedId) {
    final GlobalId out = Relay.toGlobalIdOrNull(globalId);
    satisfiesGlobalId(out, expectedType, expectedId);
  }

  @ParameterizedTest(name = "toGlobalIdOrNull(\"{0}\") == null")
  @ValueSource(
      strings = {"[" /* Invalid Base64 */, "Ojo6" /* ::: */, "QTpi" /* A:b */, "QToxOg" /* A:1: */})
  void toGlobalIdOrNullOnInvalid(String globalId) {
    final GlobalId out = Relay.toGlobalIdOrNull(globalId);
    assertThat(out).isNull();
  }

  @ParameterizedTest(name = "toGlobalId(\"{0}\") throws")
  @ValueSource(
      strings = {"[" /* Invalid Base64 */, "Ojo6" /* ::: */, "QTpi" /* A:b */, "QToxOg" /* A:1: */})
  void toGlobalIdThrowsOnInvalid(String globalId) {
    assertThatThrownBy(() -> Relay.toGlobalId(globalId))
        .hasMessage("Invalid identifier: " + globalId)
        .hasNoCause()
        .isExactlyInstanceOf(RuntimeException.class);
  }

  @ParameterizedTest(name = "(type, id) = toGlobalId(\"{0}\"); type == \"{1}\" && id == {2}")
  @CsvSource({"'VXNlcjox', 'User', 1", "'OjI', '', 2", "'QUJDOi0x', 'ABC', -1"})
  void toGlobalIdOnValid(String globalId, String expectedType, long expectedId) {
    final GlobalId out = Relay.toGlobalId(globalId);
    satisfiesGlobalId(out, expectedType, expectedId);
  }

  @ParameterizedTest(name = "assertAndExtractId(\"{0}\", \"{1}\") == \"{2}\"")
  @CsvSource({"'VXNlcjox', 'User', 1", "'OjI', '', 2", "'QUJDOi0x', 'ABC', -1"})
  void assertAndExtractIdOnValid(String globalId, String expectedType, long expectedId) {
    assertThat(Relay.assertAndExtractId(globalId, expectedType)).isEqualTo(expectedId);
  }

  @ParameterizedTest(name = "assertAndExtractId(\"{0}\", \"{1}\") throws")
  @CsvSource({"'VXNlcjox', ''", "'OjI', 'User'", "'QUJDOi0x', 'CBA'"})
  void assertAndExtractIdOnInvalid(String globalId, String expectedType) {
    assertThatThrownBy(() -> Relay.assertAndExtractId(globalId, expectedType))
        .hasMessage("Invalid identifier: " + globalId)
        .hasNoCause()
        .isExactlyInstanceOf(AssertionError.class);
  }

  private void satisfiesGlobalId(GlobalId out, String expectedType, long expectedId) {
    assertThat(out)
        .isNotNull()
        .satisfies(
            $ ->
                assertThat($)
                    .extracting(GlobalId::type, as(InstanceOfAssertFactories.STRING))
                    .isNotNull()
                    .isEqualTo(expectedType))
        .satisfies(
            $ ->
                assertThat($)
                    .extracting(GlobalId::id, as(InstanceOfAssertFactories.LONG))
                    .isEqualTo(expectedId));
  }
}
