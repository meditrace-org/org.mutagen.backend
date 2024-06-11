package org.mutagen.backend.service

import org.mutagen.backend.domain.model.ChunkMessage
import org.mutagen.backend.domain.enums.QueuesMQ
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class MQSenderService(
    private val rabbitTemplate: RabbitTemplate
) {

    open fun sendMessage(que: QueuesMQ, message: Any) {
        rabbitTemplate.convertAndSend(que.queueName, message)
    }

    open fun sendVideoChunk(message: ChunkMessage) {
        sendMessage(QueuesMQ.VIDEO_CHUNKS, message)
    }

    open fun sendAudioChunk(message: ChunkMessage) {
        sendMessage(QueuesMQ.AUDIO_CHUNKS, message)
    }

    open fun sendAudioChunkMessages(messages: List<ChunkMessage>) {
        messages.forEach { message ->
            sendVideoChunk(message)
        }
    }
}