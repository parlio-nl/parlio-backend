package nl.parlio.api.core.ext

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.InstanceOfAssertFactories
import org.junit.jupiter.api.Test

class MissingKeysTests {

    @Test
    fun withDefaultValue() {
        val originalMap = mapOf(1 to "1", 2 to "2", 3 to "3", 5 to "5")
        val allKeys = setOf(1, 2, 3, 4, 5)
        val newMap = originalMap.fillMissingKeys(allKeys, "?")
        assertThat(newMap).hasSize(5).containsOnlyKeys(1, 2, 3, 4, 5).satisfies {
            assertThat(it).extractingByKey(1).isEqualTo("1")
            assertThat(it).extractingByKey(2).isEqualTo("2")
            assertThat(it).extractingByKey(3).isEqualTo("3")
            assertThat(it).extractingByKey(4).isEqualTo("?")
            assertThat(it).extractingByKey(5).isEqualTo("5")
        }
    }

    @Test
    fun withDefaultValueNoMissingKeys() {
        val originalMap = mapOf(1 to "1", 2 to "2", 4 to "4", 3 to "3", 5 to "5")
        val allKeys = setOf(1, 2, 3, 4, 5)
        val newMap = originalMap.fillMissingKeys(allKeys, "?")
        assertThat(newMap).hasSize(5).containsOnlyKeys(1, 2, 3, 4, 5).satisfies {
            assertThat(it).extractingByKey(1).isEqualTo("1")
            assertThat(it).extractingByKey(2).isEqualTo("2")
            assertThat(it).extractingByKey(3).isEqualTo("3")
            assertThat(it).extractingByKey(4).isEqualTo("4")
            assertThat(it).extractingByKey(5).isEqualTo("5")
        }
    }

    @Test
    fun withDefaultValueMoreThenNecessary() {
        val originalMap = mapOf(1 to "1", 2 to "2", 3 to "3", 5 to "5", 6 to "6")
        val allKeys = setOf(1, 2, 3, 4, 5)
        val newMap = originalMap.fillMissingKeys(allKeys, "?")
        assertThat(newMap).hasSize(6).containsOnlyKeys(1, 2, 3, 4, 5, 6).satisfies {
            assertThat(it).extractingByKey(1).isEqualTo("1")
            assertThat(it).extractingByKey(2).isEqualTo("2")
            assertThat(it).extractingByKey(3).isEqualTo("3")
            assertThat(it).extractingByKey(4).isEqualTo("?")
            assertThat(it).extractingByKey(5).isEqualTo("5")
            assertThat(it).extractingByKey(6).isEqualTo("6")
        }
    }

    @Test
    fun withClosureValue() {
        val ia1 = intArrayOf()
        val ia2 = intArrayOf()
        val ia3 = intArrayOf()
        val ia5 = intArrayOf()
        val originalMap = mapOf(1 to ia1, 2 to ia2, 3 to ia3, 5 to ia5)
        val allKeys = setOf(1, 2, 3, 4, 5)
        val newMap = originalMap.fillMissingKeys(allKeys) { IntArray(it) }
        assertThat(newMap).hasSize(5).containsOnlyKeys(1, 2, 3, 4, 5).satisfies {
            assertThat(it).extractingByKey(1).isSameAs(ia1)
            assertThat(it).extractingByKey(2).isSameAs(ia2)
            assertThat(it).extractingByKey(3).isSameAs(ia3)
            assertThat(it).extractingByKey(4, InstanceOfAssertFactories.INT_ARRAY).hasSize(4)
            assertThat(it).extractingByKey(5).isSameAs(ia5)
        }
    }

    @Test
    fun withClosureValueNoMissingKeys() {
        val ia1 = intArrayOf()
        val ia2 = intArrayOf()
        val ia3 = intArrayOf()
        val ia4 = intArrayOf()
        val ia5 = intArrayOf()
        val originalMap = mapOf(1 to ia1, 2 to ia2, 3 to ia3, 4 to ia4, 5 to ia5)
        val allKeys = setOf(1, 2, 3, 4, 5)
        val newMap = originalMap.fillMissingKeys(allKeys) { IntArray(it) }
        assertThat(newMap).hasSize(5).containsOnlyKeys(1, 2, 3, 4, 5).satisfies {
            assertThat(it).extractingByKey(1).isSameAs(ia1)
            assertThat(it).extractingByKey(2).isSameAs(ia2)
            assertThat(it).extractingByKey(3).isSameAs(ia3)
            assertThat(it).extractingByKey(4).isSameAs(ia4)
            assertThat(it).extractingByKey(5).isSameAs(ia5)
        }
    }

    @Test
    fun withClosureValueMoreThenNecessary() {
        val ia2 = intArrayOf()
        val ia3 = intArrayOf()
        val ia5 = intArrayOf()
        val ia6 = intArrayOf()
        val originalMap = mapOf(2 to ia2, 3 to ia3, 5 to ia5, 6 to ia6)
        val allKeys = setOf(1, 2, 3, 4, 5)
        val newMap = originalMap.fillMissingKeys(allKeys) { IntArray(it) }
        assertThat(newMap).hasSize(6).containsOnlyKeys(1, 2, 3, 4, 5, 6).satisfies {
            assertThat(it).extractingByKey(1, InstanceOfAssertFactories.INT_ARRAY).hasSize(1)
            assertThat(it).extractingByKey(2).isSameAs(ia2)
            assertThat(it).extractingByKey(3).isSameAs(ia3)
            assertThat(it).extractingByKey(4, InstanceOfAssertFactories.INT_ARRAY).hasSize(4)
            assertThat(it).extractingByKey(5).isSameAs(ia5)
            assertThat(it).extractingByKey(6).isSameAs(ia6)
        }
    }
}
