package org.mutagen.backend.service

import org.springframework.stereotype.Service
import java.io.File

/**
 * Сервис для загрузки видео и разбиения его на чанки
 */
@Service
class VideoChunkService {

    companion object {
        const val CHUNK_SIZE_MB = 3
        const val CHUNK_SIZE = 1024 * 1024 * CHUNK_SIZE_MB
    }

    fun readVideoInChunks(filePath: String): List<ByteArray> {
        val file = File(filePath)
        val fileBytes = file.readBytes()
        val chunks = mutableListOf<ByteArray>()
        var start = 0

        while (start < fileBytes.size) {
            val end = minOf(fileBytes.size, start + CHUNK_SIZE)
            val chunk = fileBytes.copyOfRange(start, end)
            chunks.add(chunk)
            start += CHUNK_SIZE
        }

        return chunks
    }

}