package org.mutagen.backend.service

import org.springframework.stereotype.Service
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Сервис для загрузки видео и разбиения его на чанки
 */
@Service
class VideoChunkService {

    companion object {
        const val CHUNK_SIZE_MB = 4
        const val CHUNK_SIZE = 1024 * 1024 * CHUNK_SIZE_MB
    }

    fun readVideoInChunks(videoUrl: String): List<ByteArray> {
        val connection = URL(videoUrl).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val inputStream: InputStream = connection.inputStream
        val fileBytes = inputStream.readAllBytes()
        val chunks = mutableListOf<ByteArray>()
        var start = 0

        while (start < fileBytes.size) {
            val end = minOf(fileBytes.size, start + CHUNK_SIZE)
            val chunk = fileBytes.copyOfRange(start, end)
            chunks.add(chunk)
            start += CHUNK_SIZE
        }

        inputStream.close()
        connection.disconnect()

        return chunks
    }
}