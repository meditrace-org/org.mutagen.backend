package org.mutagen.backend.service

import org.mutagen.backend.config.SqlScriptsConfig.Companion.Insert.AUDIO_EMBEDDING
import org.mutagen.backend.config.SqlScriptsConfig.Companion.Insert.VIDEO_EMBEDDING
import org.mutagen.backend.domain.model.EmbeddingDataModel
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
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

    @RabbitListener(queues = [VIDEO_EMB], ackMode = "MANUAL")
    fun videoEmbQueueListener(
        embeddingDataModel: EmbeddingDataModel,
//        channel: Channel,
//        deliveryTag: Long,
    ) {
        log.debug("Insert {} into $VIDEO_EMB", embeddingDataModel)
        runCatching {
            val query = VIDEO_EMBEDDING.prepareQuery(embeddingDataModel)

            statementService.simpleQuery(query)
        }.onFailure {
            log.error("Failed to insert embedding data into $VIDEO_EMB", it)
        }.onSuccess {
//            channel.basicAck(deliveryTag, false)
        }
    }

    @RabbitListener(queues = [AUDIO_EMB], ackMode = "MANUAL")
    fun audioEmbQueueListener(
        embeddingDataModel: EmbeddingDataModel,
//        channel: Channel,
//        deliveryTag: Long,
    ) {
        log.debug("Insert {} into $AUDIO_EMB", embeddingDataModel)
        runCatching {
            val query = AUDIO_EMBEDDING.prepareQuery(embeddingDataModel)

            statementService.simpleQuery(query)
        }.onFailure {
            log.error("Failed to insert embedding data into $AUDIO_EMB", it)
        }.onSuccess {
//            channel.basicAck(deliveryTag, false)
        }
    }

    @RabbitListener(queues = [FACE_EMB], ackMode = "MANUAL")
    fun faceEmbQueueListener(
        embeddingDataModel: EmbeddingDataModel,
//        channel: Channel,
//        deliveryTag: Long
    ) {
        log.debug("Insert {} into $FACE_EMB", embeddingDataModel)
        runCatching {
            val query = AUDIO_EMBEDDING.prepareQuery(embeddingDataModel)

            statementService.simpleQuery(query)
        }.onFailure {
            log.error("Failed to insert embedding data to $FACE_EMB", it)
        }.onSuccess {
//            channel.basicAck(deliveryTag, false)
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