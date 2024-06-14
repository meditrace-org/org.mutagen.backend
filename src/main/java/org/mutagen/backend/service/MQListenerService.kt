package org.mutagen.backend.service

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

/**
 * Выгружает данные из очередей в базу данных
 */
@Service
class MQListenerService {

//    @RabbitListener(queues = ["TODO"])
//    fun methodOne() {
//
//    }
}