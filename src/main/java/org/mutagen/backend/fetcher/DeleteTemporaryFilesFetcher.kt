package org.mutagen.backend.fetcher

import org.mutagen.backend.config.ApplicationConfig.Companion.shouldDeleteTemporaryFiles
import org.mutagen.backend.domain.dto.VideoDTO
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class DeleteTemporaryFilesFetcher : GeneralFetcher() {

    @InjectData
    fun doFetch(video: VideoDTO) {
        log.info("Video successfully sent to processing: {}", video)
        if (!shouldDeleteTemporaryFiles) return

        if (!video.folder.toFile().deleteRecursively()) {
            log.error("Failed to delete file: {}", video)
        }
    }
}