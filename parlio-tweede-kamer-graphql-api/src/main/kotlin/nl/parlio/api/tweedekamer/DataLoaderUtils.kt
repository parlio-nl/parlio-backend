package nl.parlio.api.tweedekamer

object DataLoaderUtils {

    fun <K, V> Map<K, V>.fillMissingKeys(keys: Set<K>, defaultValue: V): Map<K, V> {
        return fillMissingKeys(keys) { defaultValue }
    }

    fun <K, V> Map<K, V>.fillMissingKeys(keys: Set<K>, valueMapper: (K) -> V): Map<K, V> {
        return this + keys.minus(this.keys).associateWith(valueMapper::invoke)
    }

}