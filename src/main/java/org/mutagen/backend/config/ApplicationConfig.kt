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
        lateinit var storagePath: String
        var shouldDeleteTemporaryFiles by Delegates.notNull<Boolean>()
        var clearTempOnStart by Delegates.notNull<Boolean>()

        private val log = LoggerFactory.getLogger(ApplicationConfig::class.java)
    }

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