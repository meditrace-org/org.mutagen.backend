package org.mutagen.backend.domain.dto

import java.util.UUID

data class VideoDTO(
    val uuid: UUID = UUID.randomUUID(),
    val isProcessed: Boolean,
    val videoUrl: String,
)