package org.mutagen.backend.domain.dto

import org.mutagen.backend.config.ApplicationConfig
import java.nio.file.Paths
import java.util.UUID

data class VideoDTO(
    val uuid: UUID = UUID.randomUUID(),
    val isProcessed: Boolean,
    val videoUrl: String,
) {
    val localVideoPath: String
        get() = Paths.get(ApplicationConfig.storagePath, uuid.toString(), "video.mp4").toString()
    val localAudioPath: String
        get() = Paths.get(ApplicationConfig.storagePath, uuid.toString(), "audio.mp4").toString()
}