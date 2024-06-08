package org.mutagen.backend.fetcher

import org.mutagen.backend.domain.dto.UploadVideoRequest
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class VideoLinkValidateFetcher: GeneralFetcher() {

    @InjectData
    fun doFetch(videoDTO: UploadVideoRequest) {
        // TODO: validate link && exist
    }
}