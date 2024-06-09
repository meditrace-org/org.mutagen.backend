package org.mutagen.backend.fetcher

import org.mutagen.backend.domain.dto.VideoDTO
import org.mutagen.backend.service.VideoChunkService
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class SendVideoChunksFetcher(
    private val videoChunkService: VideoChunkService,
) : GeneralFetcher() {

    @InjectData
    fun doFetch(video: VideoDTO) {
        val chunks = videoChunkService.readVideoInChunks(video.videoUrl)
        // TODO: send it to rabbitMq
    }
}