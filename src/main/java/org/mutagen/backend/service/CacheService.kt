package org.mutagen.backend.service

import com.github.benmanes.caffeine.cache.Cache
import org.mutagen.backend.config.CacheConfig.Companion.SEARCH_QUERY_QUALIFIER
import org.mutagen.backend.config.CacheConfig.Companion.STATUSES_QUALIFIER
import org.mutagen.backend.domain.model.ProcessingVideoResponse
import org.mutagen.backend.domain.model.SearchQueryResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class CacheService(
    @Qualifier(STATUSES_QUALIFIER) private val statusesCache: Cache<String, ProcessingVideoResponse>,
    @Qualifier(SEARCH_QUERY_QUALIFIER) private val searchQueryCache: Cache<String, SearchQueryResponse>,
) {
    fun getStatus(videoUrl: String) = statusesCache.getIfPresent(videoUrl)
    fun setStatus(videoUrl: String, status: ProcessingVideoResponse) = statusesCache.put(videoUrl, status)

    fun getResultForQuery(query: String) = searchQueryCache.getIfPresent(query.lowercase())
    fun setResultForQuery(query: String, status: SearchQueryResponse) =
        searchQueryCache.put(query.lowercase(), status)
}