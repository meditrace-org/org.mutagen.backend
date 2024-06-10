package org.mutagen.backend.config

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import jakarta.annotation.PostConstruct
import org.mutagen.backend.domain.dto.ProcessingVideoResponse
import org.mutagen.backend.domain.dto.UploadVideoRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

@Configuration
open class ApplicationConfig {

    companion object {
        const val CACHE_STATUSES_SIZE = 100_000L
        const val CACHE_STATUSES_DAYS = 2L
        lateinit var storagePath: String
        var shouldDeleteTemporaryFiles by Delegates.notNull<Boolean>()
        var clearTempOnStart by Delegates.notNull<Boolean>()

        private val log = LoggerFactory.getLogger(ApplicationConfig::class.java)
    }

    @Bean
    open fun statusesCache(): Cache<String, ProcessingVideoResponse> =
        Caffeine.newBuilder()
            .expireAfterWrite(CACHE_STATUSES_DAYS, TimeUnit.DAYS)
            .maximumSize(CACHE_STATUSES_SIZE)
            .build()

    @Value("\${mutagen.storage.path:.tmp/storage/}")
    fun setStoragePath(storagePath: String) {
        ApplicationConfig.storagePath = storagePath
    }

    @Value("\${mutagen.storage.delete-temp:true}")
    fun setShouldDeleteTemporaryFiles(shouldDeleteTemporaryFiles: Boolean) {
        ApplicationConfig.shouldDeleteTemporaryFiles = shouldDeleteTemporaryFiles
    }

    @Value("\${mutagen.storage.start-clear-temp:true}")
    fun setClearTempOnStart(clearTempOnStart: Boolean) {
        ApplicationConfig.clearTempOnStart = clearTempOnStart
    }

    @PostConstruct
    fun init() {
        if (clearTempOnStart && !File(storagePath).deleteRecursively()) {
            log.error("Cannot delete temporary files on start")
        }
    }
}