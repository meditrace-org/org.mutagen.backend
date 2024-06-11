package org.mutagen.backend.flow

import org.mutagen.backend.fetcher.PrepareQueryFetcher
import org.springframework.context.annotation.Configuration
import ru.mephi.sno.libs.flow.belly.FlowBuilder
import ru.mephi.sno.libs.flow.config.BaseFlowConfiguration

@Configuration
open class SearchFlow(
    private val prepareQueryFetcher: PrepareQueryFetcher,
): BaseFlowConfiguration(SearchFlow::class) {

    override fun FlowBuilder.buildFlow() {
        sequence {
            fetch(prepareQueryFetcher)
        }
    }
}