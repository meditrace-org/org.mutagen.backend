package org.mutagen.backend.component

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
object ApplicationProperties {
    @Value("\${mutagen.files.path:temp/files}")
    lateinit var filesPath: String
}