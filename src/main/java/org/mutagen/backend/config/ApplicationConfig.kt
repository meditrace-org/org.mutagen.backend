package org.mutagen.backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
open class ApplicationConfig {

    companion object {
        lateinit var storagePath: String
    }

    @Value("\${mutagen.storage.path:.tmp/storage/}")
    fun setStoragePath(storagePath: String) {
        ApplicationConfig.storagePath = storagePath
    }
}