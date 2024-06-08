package org.mutagen.backend.domain.dto

import java.util.UUID

// TODO: video link
data class VideoDTO(
    val uuid: UUID = UUID.randomUUID(),
    val isProcessed: Boolean,
)