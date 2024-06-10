package org.mutagen.backend.domain.dto

import org.mutagen.backend.config.ApplicationConfig
import java.nio.file.Paths
import java.util.UUID

data class VideoDTO(
    val uuid: UUID = UUID.randomUUID(),
    val isProcessed: Boolean,
    val videoUrl: String,
) {
    val folder = Paths.get(ApplicationConfig.storagePath, uuid.toString()).toAbsolutePath()
    val localVideoPath = Paths.get(folder.toString(), "video.mp4").toString()
    val localAudioPath = Paths.get(folder.toString(), uuid.toString(), "audio.mp4").toString()
}