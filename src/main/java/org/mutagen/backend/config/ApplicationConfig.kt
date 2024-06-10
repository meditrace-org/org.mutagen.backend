package org.mutagen.backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import kotlin.properties.Delegates

@Configuration
open class ApplicationConfig {

    companion object {
        lateinit var storagePath: String
        var shouldDeleteTemporaryFiles by Delegates.notNull<Boolean>()
    }

    @Value("\${mutagen.storage.path:.tmp/storage/}")
    fun setStoragePath(storagePath: String) {
        ApplicationConfig.storagePath = storagePath
    }

    @Value("\${mutagen.storage.delete-temp:true}")
    fun setShouldDeleteTemporaryFiles(shouldDeleteTemporaryFiles: Boolean) {
        ApplicationConfig.shouldDeleteTemporaryFiles = shouldDeleteTemporaryFiles
    }
}