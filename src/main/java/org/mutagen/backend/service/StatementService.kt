package org.mutagen.backend.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.PreparedStatement
import javax.sql.DataSource

/**
 * Вспомогательный класс для обработки запросов
 */
@Component
class StatementService(
    private val dataSource: DataSource,
) {

    companion object {
        private val log = LoggerFactory.getLogger(StatementService::class.java)
    }

    fun <T> singleQuery(
        query: String,
        stmtAction: (PreparedStatement, Connection) -> T,
    ): T {
        log.debug("Doing SQL query:\n{}", query)
        dataSource.connection.use { conn ->
            conn.prepareStatement(query).use { stmt ->
                return stmtAction(stmt, conn)
            }
        }
    }

    fun simpleQuery(query: String) {
        log.debug("Doing SQL query:\n{}", query)
        dataSource.connection.use { conn ->
            conn.createStatement().use {
                it.executeQuery(query)
            }
        }
    }
}