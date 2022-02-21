package nl.parlio.api.core.relay

import java.nio.charset.StandardCharsets
import java.util.Base64

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

}