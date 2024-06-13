package org.mutagen.backend.fetcher.search

import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

/**
 * Фетчер который достает топ-N релеватных векторов по визуальному контенту
 */
@Component
open class GetSimilarVideosFetcher : GeneralFetcher(){

    @InjectData
    fun doFetch() {
    }
}