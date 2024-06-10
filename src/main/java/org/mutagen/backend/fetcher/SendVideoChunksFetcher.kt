package org.mutagen.backend.fetcher

import org.mutagen.backend.domain.dto.ChunkMessage
import org.mutagen.backend.domain.dto.VideoDTO
import org.mutagen.backend.service.MQSenderService
import org.mutagen.backend.service.ChunkingService
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class SendVideoChunksFetcher(
    private val chunkingService: ChunkingService,
    private val mqSenderService: MQSenderService,
) : GeneralFetcher() {

    @InjectData
    fun doFetch(video: VideoDTO) {
        val chunks = chunkingService.splitFileIntoChunks(video.localVideoPath)

        log.debug("Send video chunks to que: {}", video)
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