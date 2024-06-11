package org.mutagen.backend.fetcher

import org.mutagen.backend.domain.dto.SearchQueryResponse
import org.mutagen.backend.domain.model.VideoModel
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher
import java.util.*

@Component
class PrepareQueryFetcher: GeneralFetcher() {

    @InjectData
    fun doFetch(query: String): SearchQueryResponse {
        return SearchQueryResponse(
            executionTime = 0,
            result = listOf(
                VideoModel(
                    uuid = UUID.randomUUID(),
                    videoUrl = "test"
                )
            ),
        )
    }
}