package org.mutagen.backend.domain.enums

enum class QueuesMQ(
    val queueName: String
) {
    VIDEO_CHUNKS("video_chunks"),
    AUDIO_CHUNKS("audio_chunks"),
    DATA_QUEUE("data_queue"),
}