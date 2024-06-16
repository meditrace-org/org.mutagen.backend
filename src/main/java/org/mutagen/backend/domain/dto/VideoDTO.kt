package org.mutagen.backend.domain.dto

import org.mutagen.backend.config.ApplicationConfig
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID

data class VideoDTO(
    val uuid: UUID,
    val isProcessed: Boolean,
    val videoUrl: String,
) {
    val folder: Path = Paths.get(ApplicationConfig.STORAGE_PATH, uuid.toString())
    val localVideoPath = Paths.get(folder.toString(), "video.mp4").toString()
    val localAudioPath = Paths.get(folder.toString(), "audio.mp3").toString()

    constructor(isProcessed: Boolean, videoUrl: String) : this(
        uuid = UUID.nameUUIDFromBytes(videoUrl.toByteArray()),
        isProcessed = isProcessed,
        videoUrl = videoUrl,
    )
}