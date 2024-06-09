package org.mutagen.backend.service

import org.mutagen.backend.domain.dto.ChunksMessage
import org.mutagen.backend.domain.enums.QueuesMQ
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class MQSenderService(
    private val rabbitTemplate: RabbitTemplate
) {
    private fun sendMessage(que: QueuesMQ, message: Any) {
        rabbitTemplate.convertAndSend(que.name, message)
    }

    fun sendVideoChunks(message: ChunksMessage) {
        sendMessage(QueuesMQ.VIDEO_CHUNKS, message)
    }

    fun sendAudioChunks(message: ChunksMessage) {
        sendMessage(QueuesMQ.AUDIO_CHUNKS, message)
    }
}