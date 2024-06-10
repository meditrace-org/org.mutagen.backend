package org.mutagen.backend.config

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
open class ApplicationConfig {

    companion object {
        lateinit var storagePath: String
    }

    @Value("\${mutagen.storage.path:.tmp/storage/}")
    fun setStoragePath(storagePath: String) {
        ApplicationConfig.storagePath = storagePath
    }

    @PostConstruct
    fun createStorageDirectory() {
        val storageDir = File(storagePath)
        if (!storageDir.exists())
            storageDir.mkdirs()
    }
}