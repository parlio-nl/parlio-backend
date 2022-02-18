package nl.parlio.api.tweedekamer;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class JavaVersionTest {

  @Test
  void isOnJava17() {
    int version = Runtime.version().feature();
    assertThat(version).isEqualTo(17);
  }
}
