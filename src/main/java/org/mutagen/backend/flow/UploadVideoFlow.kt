package org.mutagen.backend.flow

import org.mutagen.backend.domain.dto.VideoDTO
import org.mutagen.backend.fetcher.upload.*
import org.springframework.context.annotation.Configuration
import ru.mephi.sno.libs.flow.belly.FlowBuilder
import ru.mephi.sno.libs.flow.belly.FlowContext
import ru.mephi.sno.libs.flow.config.BaseFlowConfiguration

/**
 * Граф загрузки видео
 */
@Configuration
open class UploadVideoFlow(
    private val videoValidateFetcher: VideoValidateFetcher,
    private val downloadVideoFetcher: DownloadVideoFetcher,
    private val sendVideoChunksFetcher: SendVideoChunksFetcher,
    private val sendAudioFetcher: SendAudioFetcher,
    private val deleteTemporaryFilesFetcher: DeleteTemporaryFilesFetcher,
): BaseFlowConfiguration(UploadVideoFlow::class) {

    override fun FlowBuilder.buildFlow() {
        sequence {
            fetch(videoValidateFetcher)
            sequence (condition = { it.isVideoValid() }){
                fetch(downloadVideoFetcher)
                group {
                    fetch(sendVideoChunksFetcher)
                    fetch(sendAudioFetcher)
                }
                fetch(deleteTemporaryFilesFetcher)
            }
        }
    }

    private fun FlowContext.isVideoValid() = get<VideoDTO>()?.let { true } ?: false
}