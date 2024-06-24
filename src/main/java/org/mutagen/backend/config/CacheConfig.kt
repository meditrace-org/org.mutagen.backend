package org.mutagen.backend.config

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.mutagen.backend.domain.model.ProcessingVideoResponse
import org.mutagen.backend.domain.model.SearchQueryResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
open class CacheConfig {

    companion object {
        const val CACHE_STATUSES_SIZE = 100_000L
        const val CACHE_STATUSES_DAYS = 2L

        const val STATUSES_QUALIFIER = "statuses"
        const val SEARCH_QUERY_QUALIFIER = "search_query"
    }

    @Value("\${mutagen.cache.expire-s.search:120}")
    private lateinit var cacheSearchExp: String

    @Value("\${mutagen.cache.expire-s.status:120}")
    private lateinit var cacheStatusExp: String

    @Bean
    @Qualifier(STATUSES_QUALIFIER)
    open fun statusesCache(): Cache<String, ProcessingVideoResponse> =
        Caffeine.newBuilder()
            .expireAfterWrite(cacheStatusExp.toLong(), TimeUnit.DAYS)
            .maximumSize(CACHE_STATUSES_SIZE)
            .build()

    @Bean
    @Qualifier(SEARCH_QUERY_QUALIFIER)
    open fun searchQueryCache(): Cache<Pair<String, Int>, SearchQueryResponse> =
        Caffeine.newBuilder()
            .expireAfterWrite(cacheSearchExp.toLong(), TimeUnit.SECONDS)
            .maximumSize(CACHE_STATUSES_SIZE)
            .build()
}