package nl.parlio.api.core.ext

import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.TypeDescriptor

// TODO Test
inline fun <reified T> ConversionService.convertOrNull(source: Any?): T? {
    return this.convert(source, T::class.java)
}

inline fun <reified T> ConversionService.convertExact(source: Any?): T {
    return convertOrNull<T>(source)!!
}

// TODO Check if convertListOnEach is faster - maybe cache TypeDescriptor.valueOf calls?
private inline fun <reified I, reified O> ConversionService.batchConvertList(
    sourceList: List<I>
): List<O> {
    val sourceType =
        TypeDescriptor.collection(List::class.java, TypeDescriptor.valueOf(I::class.java))
    val targetType =
        TypeDescriptor.collection(List::class.java, TypeDescriptor.valueOf(O::class.java))
    @Suppress("UNCHECKED_CAST") return this.convert(sourceList, sourceType, targetType) as List<O>
}

inline fun <I, reified O> ConversionService.convertListOnEach(sourceList: List<I>): List<O> {
    return sourceList.map { this.convertExact(it) }
}

// Generic <reified I> is reified for possible future changes to the method body
inline fun <reified I, reified O> ConversionService.convertList(sourceList: List<I>): List<O> {
    return convertListOnEach(sourceList)
}
