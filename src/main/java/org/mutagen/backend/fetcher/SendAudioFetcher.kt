package org.mutagen.backend.fetcher

import org.mutagen.backend.domain.dto.ChunkMessage
import org.mutagen.backend.domain.dto.VideoDTO
import org.mutagen.backend.service.ChunkingService
import org.mutagen.backend.service.MQSenderService
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher
import java.io.BufferedReader
import java.io.InputStreamReader

@Component
class SendAudioFetcher(
    private val chunkingService: ChunkingService,
    private val mqSenderService: MQSenderService,
) : GeneralFetcher() {

    @InjectData
    fun doFetch(video: VideoDTO) {
        convertToAudio(video)

        val audioAsBytes = chunkingService.fileToByteArray(video.localAudioPath)

        log.debug("Send audio chunks to que: {}", video)
        mqSenderService.sendAudioChunk(
            ChunkMessage(
                video.uuid,
                audioAsBytes,
            )
        )
    }

    private fun convertToAudio(video: VideoDTO) {
        log.debug("Converting to audio: {}", video)

        val cmd = arrayOf(
            "ffmpeg",
            "-i", video.localVideoPath,
            "-vn",
            "-acodec", "libmp3lame",
            "-ab", "128k",
            "-ar", "44100",
            "-ac", "2",
            video.localAudioPath,
        )

        val processBuilder = ProcessBuilder(*cmd)
        processBuilder.redirectErrorStream(true)

        val process = processBuilder.start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            log.debug("{} : $line", video.uuid)
        }

        process.waitFor()
    }
}