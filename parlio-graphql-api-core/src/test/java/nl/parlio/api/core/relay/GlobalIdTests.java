package nl.parlio.api.core.relay;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class GlobalIdTests {

  @Test
  void isGlobalIdClassJvmRecord() {
    assertThat(GlobalId.class).matches(Class::isRecord);
  }
}
