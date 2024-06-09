package org.mutagen.backend.domain.dto

import java.util.*

data class ChunksMessage(
    val uuid: UUID
) {
    private lateinit var serializedChunk: String

    constructor(uuid: UUID, chunk: ByteArray) : this(uuid) {
        serializedChunk = serializeChunk(chunk)
    }

    private fun serializeChunk(chunk: ByteArray) = Base64.getEncoder().encodeToString(chunk)
}