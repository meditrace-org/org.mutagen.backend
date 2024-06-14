package org.mutagen.backend.domain.enums

enum class QueuesMQ(
    val queueName: String
) {
    VIDEO_CHUNKS("video_chunks"),
    AUDIO_CHUNKS("audio_chunks"),

    VIDEO_EMB("video_emb"),
    AUDIO_EMB("audio_emb"),
    FACE_EMB("face_emb"),
}