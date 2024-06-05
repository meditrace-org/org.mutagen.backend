package org.mutagen.backend.service

import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class VideoChunkService {

    fun readVideoInChunks(filePath: String, chunkSize: Int): List<ByteArray> {
        val path: Path = Paths.get(filePath)
        val fileBytes = Files.readAllBytes(path)
        val chunks = mutableListOf<ByteArray>()
        var start = 0

        while (start < fileBytes.size) {
            val end = minOf(fileBytes.size, start + chunkSize)
            val chunk = fileBytes.copyOfRange(start, end)
            chunks.add(chunk)
            start += chunkSize
        }

        return chunks
    }
}