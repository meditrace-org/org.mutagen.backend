package org.mutagen.backend.fetcher

import org.mutagen.backend.domain.dto.VideoDTO
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

@Component
class DownloadVideoFetcher : GeneralFetcher() {

    @InjectData
    fun doFetch(video: VideoDTO) {
        downloadVideo(video.videoUrl, video.localVideoPath)
    }

    private fun downloadVideo(videoUrl: String, savePath: String) {
        log.debug("Download video by {} into {}", videoUrl, savePath)
        val connection = URL(videoUrl).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val inputStream = connection.inputStream
        val outputStream = FileOutputStream(savePath)

        val buffer = ByteArray(1024)
        var bytesRead: Int

        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        outputStream.close()
        inputStream.close()
        connection.disconnect()
    }
}