package org.mutagen.backend.domain.dto

import java.util.*

data class ChunkMessage(
    val uuid: UUID,
    val serializedChunk: String
) {
    companion object {
        private fun serializeChunk(chunk: ByteArray) = Base64.getEncoder().encodeToString(chunk)
    }

    constructor(uuid: UUID, chunk: ByteArray) : this(uuid, serializeChunk(chunk))
}