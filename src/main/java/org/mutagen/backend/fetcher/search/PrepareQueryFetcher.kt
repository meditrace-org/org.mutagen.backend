package org.mutagen.backend.fetcher.search

import org.mutagen.backend.service.Text2VectorService
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class PrepareQueryFetcher(
    private val text2VectorService: Text2VectorService,
): GeneralFetcher() {

    @InjectData
    fun doFetch(query: String): FloatArray {
        val result = text2VectorService.getTextVector(query)
        return FloatArray(0)
    }
}