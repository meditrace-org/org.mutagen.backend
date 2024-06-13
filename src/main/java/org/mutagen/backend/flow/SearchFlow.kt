package org.mutagen.backend.flow

import org.mutagen.backend.fetcher.search.GetSimilarAudiosFetcher
import org.mutagen.backend.fetcher.search.GetSimilarVideosFetcher
import org.mutagen.backend.fetcher.search.PrepareQueryFetcher
import org.springframework.context.annotation.Configuration
import ru.mephi.sno.libs.flow.belly.FlowBuilder
import ru.mephi.sno.libs.flow.config.BaseFlowConfiguration

@Configuration
open class SearchFlow(
    private val prepareQueryFetcher: PrepareQueryFetcher,
    private val getSimilarAudiosFetcher: GetSimilarAudiosFetcher,
    private val getSimilarVideosFetcher: GetSimilarVideosFetcher,
): BaseFlowConfiguration(SearchFlow::class) {

    override fun FlowBuilder.buildFlow() {
        sequence {
            fetch(prepareQueryFetcher)
            group {
                fetch(getSimilarVideosFetcher)
                fetch(getSimilarAudiosFetcher)
            }
        }
    }
}