package nl.parlio.api.core.relay

import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.Objects
import nl.parlio.api.core.relay.cursor.RelayCursor

object Relay {

    private val B64_DECODER: Base64.Decoder = Base64.getUrlDecoder()
    private val B64_ENCODER: Base64.Encoder = Base64.getUrlEncoder().withoutPadding()

    @JvmStatic
    fun toGlobalId(type: String, id: Long): String {
        return B64_ENCODER.encodeToString("$type:$id".toByteArray(StandardCharsets.UTF_8))
    }

    @JvmStatic
    fun toGlobalId(globalId: GlobalId): String {
        return toGlobalId(globalId.type, globalId.id)
    }

    @JvmStatic
    fun toGlobalIdOrNull(stringId: String): GlobalId? {
        val decoded: ByteArray
        try {
            decoded = B64_DECODER.decode(stringId)
        } catch (e: IllegalArgumentException) {
            return null
        }
        val decodedStr = String(decoded, StandardCharsets.UTF_8)
        val split = decodedStr.split(':')
        if (split.size != 2) {
            return null
        }
        val id = split[1].toLongOrNull() ?: return null
        return GlobalId(split[0], id)
    }

    @JvmStatic
    fun toGlobalId(stringId: String): GlobalId {
        return toGlobalIdOrNull(stringId) ?: throw RuntimeException("Invalid identifier: $stringId")
    }

    @JvmStatic
    fun assertAndExtractId(stringId: String, expectedType: String): Long {
        val globalId = toGlobalId(stringId)
        assert(globalId.type == expectedType) { "Invalid identifier: $stringId" }
        return globalId.id
    }

    private object Cursor {
        @JvmStatic
        fun <T> toCursor(cursorType: String, cursorData: T, cursorFactory: (T) -> String): String {
            val cursorBytes =
                "$cursorType:${cursorFactory(cursorData)}".toByteArray(StandardCharsets.UTF_8)
            return B64_ENCODER.encodeToString(cursorBytes)
        }

        @JvmStatic
        fun <T> toCursor(relayCursor: RelayCursor<T>): String {
            return toCursor(relayCursor.cursorType, relayCursor.cursorData, Objects::toString)
        }

        @JvmStatic
        fun <T> toCursorOrNull(
            cursorString: String,
            cursorFactory: (String) -> T?
        ): RelayCursor<T>? {
            val decoded: ByteArray
            try {
                decoded = B64_DECODER.decode(cursorString)
            } catch (e: IllegalArgumentException) {
                return null
            }
            val decodedStr = String(decoded, StandardCharsets.UTF_8)
            val split = decodedStr.split(':')
            if (split.size != 2) {
                return null
            }
            val cursorData = cursorFactory(split[1]) ?: return null
            return RelayCursor(split[0], cursorData)
        }

        @JvmStatic
        fun <T> toCursor(cursorString: String, cursorFactory: (String) -> T?): RelayCursor<T> {
            return toCursorOrNull(cursorString, cursorFactory)
                ?: throw RuntimeException("Invalid cursor: $cursorString")
        }

        @JvmStatic
        fun <T> assertAndExtractCursorData(
            cursorString: String,
            expectedType: String,
            cursorFactory: (String) -> T?
        ): T {
            val relayCursor = toCursor(cursorString, cursorFactory)
            assert(relayCursor.cursorType == expectedType) { "Invalid cursor: $cursorString" }
            return relayCursor.cursorData
        }
    }

    object LongCursor {
        private const val LONG_CURSOR_PREFIX = "L"

        @JvmStatic
        fun assertAndExtract(
            cursorString: String,
        ): Long {
            return Cursor.assertAndExtractCursorData(
                cursorString,
                LONG_CURSOR_PREFIX,
                String::toLongOrNull
            )
        }

        @JvmStatic
        fun toCursor(cursorData: Long): String {
            return Cursor.toCursor(LONG_CURSOR_PREFIX, cursorData, Long::toString)
        }
    }
}
