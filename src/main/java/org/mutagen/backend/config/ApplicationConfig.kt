package org.mutagen.backend.config

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.File
import kotlin.properties.Delegates

@Configuration
open class ApplicationConfig {

    companion object {
        lateinit var STORAGE_PATH: String
        lateinit var TEXT2VECTOR_URL: String
        var SIMILAR_AUDIO_LIMIT by Delegates.notNull<Int>()
        var SIMILAR_VIDEO_LIMIT by Delegates.notNull<Int>()

        var ALPHA by Delegates.notNull<Float>()
        var BETA by Delegates.notNull<Float>()
        var LIMIT by Delegates.notNull<Int>()

        var shouldDeleteTemporaryFiles by Delegates.notNull<Boolean>()
        var clearTempOnStart by Delegates.notNull<Boolean>()

        private val log = LoggerFactory.getLogger(ApplicationConfig::class.java)
    }

    @Value("\${mutagen.search.limit:10}")
    fun setLimit(value: Int) {
        ApplicationConfig.LIMIT = value
    }

    @Value("\${mutagen.search.alpha:0.5}")
    fun setAlpha(value: Float) {
        ApplicationConfig.ALPHA = value
    }

    @Value("\${mutagen.search.beta:0.5}")
    fun setBeta(value: Float) {
        ApplicationConfig.BETA = value
    }

    @Value("\${mutagen.search.similarity.limits.audio:200}")
    fun setSimilarAudioLimit(limit: Int) {
        ApplicationConfig.SIMILAR_AUDIO_LIMIT = limit
    }

    @Value("\${mutagen.search.similarity.limits.video:600}")
    fun setSimilarVideoLimit(limit: Int) {
        ApplicationConfig.SIMILAR_VIDEO_LIMIT = limit
    }

    @Value("\${mutagen.storage.path:./data/.tmp/}")
    fun setStoragePath(storagePath: String) {
        ApplicationConfig.STORAGE_PATH = storagePath
    }

    // TODO
    @Value("\${mutagen.text2vec.url:TODO}")
    fun setTextToVectorServiceUrl(textToVectorServiceUrl: String) {
        ApplicationConfig.TEXT2VECTOR_URL = textToVectorServiceUrl
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
        if (clearTempOnStart && !File(STORAGE_PATH).deleteRecursively()) {
            log.error("Cannot delete temporary files on start")
        }
    }
}