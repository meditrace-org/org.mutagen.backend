package org.mutagen.backend.fetcher

import org.mutagen.backend.domain.model.ChunkMessage
import org.mutagen.backend.domain.dto.VideoDTO
import org.mutagen.backend.service.MQSenderService
import org.mutagen.backend.service.ChunkingService
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
open class SendVideoChunksFetcher(
    private val chunkingService: ChunkingService,
    private val mqSenderService: MQSenderService,
) : GeneralFetcher() {

    @InjectData
    open fun doFetch(video: VideoDTO) {
        val chunkMessages = chunkingService
            .splitFileIntoChunks(video.localVideoPath)
            .map { chunk ->
                ChunkMessage(
                    video.uuid,
                    chunk,
                )
            }
        mqSenderService.sendAudioChunkMessages(chunkMessages)
    }

}