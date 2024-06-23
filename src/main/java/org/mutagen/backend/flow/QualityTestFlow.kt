package org.mutagen.backend.flow

import org.mutagen.backend.fetcher.test.quality.QualityTestQueryMapper
import org.springframework.context.annotation.Configuration
import ru.mephi.sno.libs.flow.belly.FlowBuilder
import ru.mephi.sno.libs.flow.config.BaseFlowConfiguration

/**
 * Граф загрузки видео
 */
@Configuration
open class QualityTestFlow(
    private val qualityTestQueryMapper: QualityTestQueryMapper
): BaseFlowConfiguration(QualityTestFlow::class) {

    override fun FlowBuilder.buildFlow() {
        sequence {
            fetch(qualityTestQueryMapper)
        }
    }

}