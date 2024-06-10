package org.mutagen.backend.fetcher

import org.mutagen.backend.domain.dto.VideoDTO
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class RemoveTemporaryFilesFetcher : GeneralFetcher() {

    @InjectData
    fun doFetch(video: VideoDTO) {
        log.info("Video successfully sent to processing: {}", video)
        if (!video.folder.toFile().deleteRecursively()) {
            log.error("Failed to delete file: {}", video)
        }
    }
}