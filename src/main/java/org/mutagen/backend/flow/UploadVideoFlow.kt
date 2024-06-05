package org.mutagen.backend.flow

import org.springframework.context.annotation.Configuration
import ru.mephi.sno.libs.flow.belly.FlowBuilder
import ru.mephi.sno.libs.flow.config.BaseFlowConfiguration

/**
 * Граф для загрузки видео
 */
@Configuration
open class UploadVideoFlow(

): BaseFlowConfiguration(UploadVideoFlow::class) {

    override fun FlowBuilder.buildFlow() {
        sequence {
        }
    }
}