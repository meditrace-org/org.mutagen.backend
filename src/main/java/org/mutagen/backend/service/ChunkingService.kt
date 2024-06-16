package org.mutagen.backend.service

import org.mutagen.backend.domain.dto.VideoDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path

/**
 * Сервис для загрузки видео и разбиения его на чанки
 */
@Service
class ChunkingService {

    companion object {
        const val CHUNK_DURATION_SECONDS = 12

        private const val CHUNK_NAME_FORMAT = "chunk_%03d.mp4"
        private val log = LoggerFactory.getLogger(ChunkingService::class.java)
    }

    fun splitVideoIntoChunks(video: VideoDTO): List<ByteArray> {
        return videoChunking(video).also {
            log.debug("Got {} chunks for {}", it.size, video)
        }.map {
            fileToByteArray(it)
        }
    }

    fun fileToByteArray(filePath: String): ByteArray {
        val file = File(filePath)
        return file.readBytes()
    }

    // возвращает список путей к чанкам
    private fun videoChunking(video: VideoDTO): List<String> {
        val cmd = arrayOf(
            "ffmpeg",
            "-i", video.localVideoPath,
            "-c", "copy",
            "-map", "0",
            "-segment_time", CHUNK_DURATION_SECONDS.toString(),
            "-f", "segment",
            Path.of(video.folder.toString(), CHUNK_NAME_FORMAT).toString()
        )

        val processBuilder = ProcessBuilder(*cmd)
        processBuilder.redirectErrorStream(true)

        val process = processBuilder.start()
        val inputStream = process.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            log.debug("{} : $line", video.uuid)
        }
        process.waitFor()

        val chunkPaths = mutableListOf<String>()
        for (i in 0 until Int.MAX_VALUE) {
            val chunkPath = Path.of(video.folder.toString(), String.format(CHUNK_NAME_FORMAT, i))
            if (Files.exists(chunkPath)) {
                chunkPaths.add(chunkPath.toString())
            } else {
                break
            }
        }

        return chunkPaths
    }
}