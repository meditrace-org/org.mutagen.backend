package org.mutagen.backend.flow

import org.mutagen.backend.fetcher.VideoLinkValidateFetcher
import org.springframework.context.annotation.Configuration
import ru.mephi.sno.libs.flow.belly.FlowBuilder
import ru.mephi.sno.libs.flow.config.BaseFlowConfiguration

/**
 * Граф загрузки видео
 */
@Configuration
open class UploadVideoFlow(
    private val videoLinkValidateFetcher: VideoLinkValidateFetcher,
): BaseFlowConfiguration(UploadVideoFlow::class) {

    override fun FlowBuilder.buildFlow() {
        sequence {
            fetch(videoLinkValidateFetcher)
        }
    }
}