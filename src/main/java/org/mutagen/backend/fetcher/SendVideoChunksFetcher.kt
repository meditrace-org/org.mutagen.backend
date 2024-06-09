package org.mutagen.backend.fetcher

import org.mutagen.backend.domain.dto.ChunkMessage
import org.mutagen.backend.domain.dto.VideoDTO
import org.mutagen.backend.service.MQSenderService
import org.mutagen.backend.service.VideoChunkService
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class SendVideoChunksFetcher(
    private val videoChunkService: VideoChunkService,
    private val mqSenderService: MQSenderService,
) : GeneralFetcher() {

    @InjectData
    fun doFetch(video: VideoDTO) {
        val chunks = videoChunkService.readVideoInChunks(video.videoUrl)

        chunks.forEach { chunk ->
            mqSenderService.sendVideoChunks(
                ChunkMessage(
                    video.uuid,
                    chunk,
                )
            )
        }
    }
}