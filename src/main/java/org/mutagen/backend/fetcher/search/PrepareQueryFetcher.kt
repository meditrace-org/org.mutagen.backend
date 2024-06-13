package org.mutagen.backend.fetcher.search

import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class PrepareQueryFetcher: GeneralFetcher() {

    @InjectData
    fun doFetch(query: String): FloatArray {
        // TODO: onnx to FloatArray
        return FloatArray(0)
    }
}