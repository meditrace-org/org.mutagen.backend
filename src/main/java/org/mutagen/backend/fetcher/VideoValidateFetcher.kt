package org.mutagen.backend.fetcher

import org.mutagen.backend.domain.dao.VideoDAO
import org.mutagen.backend.domain.dto.UploadVideoRequest
import org.mutagen.backend.domain.dto.VideoDTO
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
): GeneralFetcher() {

    companion object {
        const val CONTENT_TYPE_PREFIX = "video/"
    }

    @InjectData
    fun doFetch(uploadVideoRequest: UploadVideoRequest): VideoDTO? {
        if (videoDAO.isVideoWithUrlExists(uploadVideoRequest.videoLink)) {
            return null
        }
        if (!isVideoContent(uploadVideoRequest.videoLink)) {
            return null
        }
        return videoDAO.save(
            VideoDTO(
                isProcessed = false,
                videoUrl = uploadVideoRequest.videoLink,
            )
        )
    }

    private fun isVideoContent(url: String): Boolean {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "HEAD"
        return runCatching {
            connection.connect()
            connection.contentType.startsWith(CONTENT_TYPE_PREFIX)
        }.getOrElse { false }
    }
}