package org.mutagen.backend.service

import com.github.benmanes.caffeine.cache.Cache
import org.mutagen.backend.domain.dto.ProcessingVideoResponse
import org.springframework.stereotype.Service

@Service
class CacheService(
    private val statusesCache: Cache<String, ProcessingVideoResponse>,
) {
    fun getStatus(videoUrl: String) = statusesCache.getIfPresent(videoUrl)
    fun setStatus(videoUrl: String, status: ProcessingVideoResponse) = statusesCache.put(videoUrl, status)
}