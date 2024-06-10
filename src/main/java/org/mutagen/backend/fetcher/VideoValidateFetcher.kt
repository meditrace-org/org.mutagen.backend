package org.mutagen.backend.fetcher

import org.mutagen.backend.domain.dao.VideoDAO
import org.mutagen.backend.domain.dto.ProcessingVideoResponse
import org.mutagen.backend.domain.dto.UploadVideoRequest
import org.mutagen.backend.domain.dto.VideoDTO
import org.mutagen.backend.domain.enums.UploadStatus
import org.mutagen.backend.service.CacheService
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher
import java.net.HttpURLConnection
import java.net.URL

/**
 * Фетчер валидации видео по url
 */
@Component
class VideoValidateFetcher(
    private val videoDAO: VideoDAO,
    private val cacheService: CacheService,
): GeneralFetcher() {

    private companion object {
        const val CONTENT_TYPE_PREFIX = "video/mp4"
        const val HTTP = "http"
        const val HTTPS = "https"
        val ALLOWED_PROTOCOLS = setOf(HTTP, HTTPS)
    }

    @InjectData
    fun doFetch(
        uploadVideoRequest: UploadVideoRequest,
        uploadStatusUrl: String,
    ): VideoDTO? {
        log.info("Receive video request: {}", uploadVideoRequest)

        uploadVideoRequest.apply {
            catchingInvalidRequest(videoLink)?.let {
                cacheService.setStatus(
                    videoLink,
                    ProcessingVideoResponse(
                        message = it,
                        uploadStatusUrl = uploadStatusUrl,
                        uploadStatus = UploadStatus.VALIDATION_ERROR,
                    )
                )
                return null
            }

            if (videoDAO.isVideoWithUrlExists(videoLink)) {
                log.warn("Video was uploaded early: $videoLink")
                return null
            }

            return videoDAO.save(
                VideoDTO(
                    isProcessed = false,
                    videoUrl = videoLink,
                )
            )
        }
    }

    private fun catchingInvalidRequest(videoLink: String): String? {
        return when {
            !isValidUrl(videoLink) -> "Incorrect url: $videoLink"
            !isValidProtocol(videoLink) -> "Incorrect protocol: $videoLink. It should be only $HTTP or $HTTPS"
            !isVideoContent(videoLink) -> "Incorrect content: $videoLink"
            else -> null
        }
    }

    private fun isValidUrl(url: String): Boolean {
        return runCatching {
            URL(url).toURI()
            true
        }.getOrElse { false }
    }

    private fun isValidProtocol(url: String) = URL(url).protocol in ALLOWED_PROTOCOLS

    private fun isVideoContent(url: String): Boolean {
        return runCatching {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "HEAD"
            connection.connect()
            connection.contentType.startsWith(CONTENT_TYPE_PREFIX)
        }.getOrElse { false }
    }
}