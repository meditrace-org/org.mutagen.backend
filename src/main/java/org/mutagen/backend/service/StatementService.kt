package org.mutagen.backend.service

import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import javax.sql.DataSource

/**
 * Вспомогательный класс для обработки запросов
 */
@Component
class StatementService(
    private val dataSource: DataSource,
) {

    fun <T> singleQuery(
        query: String,
        stmtAction: (PreparedStatement) -> T,
    ): T {
        dataSource.connection.use { conn ->
            conn.prepareStatement(query).use { stmt ->
                return stmtAction(stmt)
            }
        }
    }
}