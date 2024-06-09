package org.mutagen.backend.fetcher

import org.mutagen.backend.domain.dao.VideoDAO
import org.mutagen.backend.domain.dto.UploadVideoRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class VideoLinkValidateFetcher(
    private val videoDAO: VideoDAO,
): GeneralFetcher() {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @InjectData
    fun doFetch(uploadVideoRequest: UploadVideoRequest) {
        if (videoDAO.isVideoWithLinkExists(uploadVideoRequest.videoLink)) {
            // TODO: stop flow execution
        }
    }
}