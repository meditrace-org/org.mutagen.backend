package org.mutagen.backend.config

import org.mutagen.backend.domain.enums.QueuesMQ
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
open class MQConfig {

    @Bean
    open fun videoChunksQueue() = Queue(QueuesMQ.VIDEO_CHUNKS.queueName, true)

    @Bean
    open fun audioChunksQueue() = Queue(QueuesMQ.AUDIO_CHUNKS.queueName, true)

    @Bean
    open fun videoEmbQueue() = Queue(QueuesMQ.VIDEO_EMB.queueName, false)

    @Bean
    open fun audioEmbQueue() = Queue(QueuesMQ.AUDIO_EMB.queueName, false)

    @Bean
    open fun faceEmbQueue() = Queue(QueuesMQ.FACE_EMB.queueName, false)

    @Bean
    open fun messageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    open fun rabbitTemplate(
        connectionFactory: ConnectionFactory,
        messageConverter: Jackson2JsonMessageConverter
    ): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = messageConverter
        rabbitTemplate.isChannelTransacted = true
        return rabbitTemplate
    }

    @Bean
    open fun transactionManager(rabbitTemplate: RabbitTemplate): PlatformTransactionManager {
        return RabbitTransactionManager(rabbitTemplate.connectionFactory)
    }
}
