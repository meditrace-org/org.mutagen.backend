package org.mutagen.backend.flow

import org.mutagen.backend.fetcher.test.quality.ValidateQueryFetcher
import org.springframework.context.annotation.Configuration
import ru.mephi.sno.libs.flow.belly.FlowBuilder
import ru.mephi.sno.libs.flow.config.BaseFlowConfiguration

/**
 * Граф загрузки видео
 */
@Configuration
open class QualityTestFlow(
    private val validateQueryFetcher: ValidateQueryFetcher
): BaseFlowConfiguration(QualityTestFlow::class) {

    override fun FlowBuilder.buildFlow() {
        sequence {
            fetch(validateQueryFetcher)
        }
    }

}