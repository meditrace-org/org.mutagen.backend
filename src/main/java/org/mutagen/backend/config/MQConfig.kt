package org.mutagen.backend.config

import org.mutagen.backend.domain.enums.QueuesMQ
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class MQConfig {

    @Bean
    open fun videoChunksQueue() = Queue(QueuesMQ.VIDEO_CHUNKS.queueName, true)

    @Bean
    open fun audioChunksQueue() = Queue(QueuesMQ.AUDIO_CHUNKS.queueName, true)

    @Bean
    open fun dataQueue() = Queue(QueuesMQ.DATA_QUEUE.queueName, true)

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
        return rabbitTemplate
    }
}