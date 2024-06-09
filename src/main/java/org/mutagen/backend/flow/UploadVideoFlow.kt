package org.mutagen.backend.flow

import org.mutagen.backend.domain.dto.VideoDTO
import org.mutagen.backend.fetcher.SendVideoChunksFetcher
import org.mutagen.backend.fetcher.VideoValidateFetcher
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
    private val sendVideoChunksFetcher: SendVideoChunksFetcher,
): BaseFlowConfiguration(UploadVideoFlow::class) {

    override fun FlowBuilder.buildFlow() {
        sequence {
            fetch(videoValidateFetcher)
            sequence (condition = { it.isVideoValid() }){
                group {
                    fetch(sendVideoChunksFetcher)
                    // TODO: fetch sendAudioChunksFetcher
                }
            }
        }
    }

    private fun FlowContext.isVideoValid() = get<VideoDTO>()?.let { true } ?: false
}