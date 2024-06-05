package org.mutagen.backend.config

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean

class RabbitConfig {

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory) = RabbitTemplate(connectionFactory)

    @Bean
    fun queue(): Queue {
        return Queue("videoChunksQueue", false)
    }
}
