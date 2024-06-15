package org.mutagen.backend.service

import com.rabbitmq.client.Channel
import org.mutagen.backend.config.SqlScriptsConfig.Companion.Insert.AUDIO_EMBEDDING
import org.mutagen.backend.config.SqlScriptsConfig.Companion.Insert.FACE_EMBEDDING
import org.mutagen.backend.config.SqlScriptsConfig.Companion.Insert.VIDEO_EMBEDDING
import org.mutagen.backend.domain.model.EmbeddingDataModel
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service

/**
 * Выгружает данные из очередей в базу данных
 */
@Service
class MQListenerService(
    private val statementService: StatementService,
) {

    companion object {
        private val log = LoggerFactory.getLogger(MQListenerService::class.java)

        private const val VIDEO_EMB = "video_emb"
        private const val AUDIO_EMB = "audio_emb"
        private const val FACE_EMB = "face_emb"
    }

    @RabbitListener(
        queuesToDeclare = [
            Queue(name = VIDEO_EMB, durable = "false", declare = "false")
        ],
        ackMode = "MANUAL"
    )
    fun videoEmbQueueListener(
        embeddingDataModel: EmbeddingDataModel,
        @Header(AmqpHeaders.CHANNEL) channel: Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) deliveryTag: Long
    )  = uploadData(
        embeddingDataModel = embeddingDataModel,
        channel = channel,
        deliveryTag = deliveryTag,
        insertQuery = VIDEO_EMBEDDING,
        dbName = VIDEO_EMB,
    )

    @RabbitListener(
        queuesToDeclare = [
            Queue(name = AUDIO_EMB, durable = "false", declare = "false")
        ],
        ackMode = "MANUAL"
    )
    fun audioEmbQueueListener(
        embeddingDataModel: EmbeddingDataModel,
        @Header(AmqpHeaders.CHANNEL) channel: Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) deliveryTag: Long
    ) = uploadData(
        embeddingDataModel = embeddingDataModel,
        channel = channel,
        deliveryTag = deliveryTag,
        insertQuery = AUDIO_EMBEDDING,
        dbName = AUDIO_EMB,
    )

    @RabbitListener(
        queuesToDeclare = [
            Queue(name = FACE_EMB, durable = "false", declare = "false")
        ],
        ackMode = "MANUAL"
    )
    fun faceEmbQueueListener(
        embeddingDataModel: EmbeddingDataModel,
        @Header(AmqpHeaders.CHANNEL) channel: Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) deliveryTag: Long
    ) = uploadData(
        embeddingDataModel = embeddingDataModel,
        channel = channel,
        deliveryTag = deliveryTag,
        insertQuery = FACE_EMBEDDING,
        dbName = FACE_EMB,
    )

    private fun uploadData(
        embeddingDataModel: EmbeddingDataModel,
        channel: Channel,
        deliveryTag: Long,
        insertQuery: String,
        dbName: String,
    ) {
        log.debug("Insert {} into $dbName", embeddingDataModel)
        runCatching {
            val query = insertQuery.prepareQuery(embeddingDataModel)
            statementService.simpleQuery(query)
        }.onFailure {
            log.error("Failed to insert embedding data to $dbName", it)
            channel.abort()
        }.onSuccess {
            channel.basicAck(deliveryTag, false)
        }
    }

    private fun String.prepareQuery(embeddingDataModel: EmbeddingDataModel) =
        this
            .replace(":uuid", "'${embeddingDataModel.uuid}'")
            .replace(":model", "'${embeddingDataModel.model}'")
            .replace(":embedding", embeddingDataModel.encodedChunk.toString())
            .replace(
                ":text",
                embeddingDataModel.text?.let { "'$it'" } ?: "NULL",
            )

}