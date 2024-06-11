package org.mutagen.backend.fetcher

import org.mutagen.backend.config.ApplicationConfig.Companion.shouldDeleteTemporaryFiles
import org.mutagen.backend.domain.model.ProcessingVideoResponse
import org.mutagen.backend.domain.dto.VideoDTO
import org.mutagen.backend.domain.enums.UploadStatus
import org.mutagen.backend.service.CacheService
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class DeleteTemporaryFilesFetcher(
    private val cacheService: CacheService,
) : GeneralFetcher() {

    @InjectData
    fun doFetch(
        video: VideoDTO,
        uploadStatusUrl: String,
    ) {
        log.info("Video successfully sent to processing: {}", video)
        if (shouldDeleteTemporaryFiles && !video.folder.toFile().deleteRecursively())
            log.error("Failed to delete file: {}", video)

        cacheService.setStatus(
            videoUrl = video.videoUrl,
            ProcessingVideoResponse(
                message = "In processing",
                uploadStatusUrl = uploadStatusUrl,
                uploadStatus = UploadStatus.PROCESSING
            )
        )
    }
}