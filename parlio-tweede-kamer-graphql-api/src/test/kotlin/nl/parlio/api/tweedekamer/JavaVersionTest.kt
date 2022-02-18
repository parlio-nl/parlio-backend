package nl.parlio.api.tweedekamer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JavaVersionTest {

    @Test
    fun isOnJava17() {
        val version = Runtime.version().feature()
        assertThat(version).isEqualTo(17)
    }
}