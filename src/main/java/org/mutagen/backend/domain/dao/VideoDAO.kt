package org.mutagen.backend.domain.dao

import org.mutagen.backend.domain.dto.VideoDTO
import org.mutagen.backend.service.StatementService
import org.springframework.stereotype.Component
import java.util.*

@Component
class VideoDAO(
    private val statementService: StatementService,
) {
    companion object Fields {
        const val TABLE_NAME = "video"
        const val UUID_FIELD = "uuid"
        const val IS_PROCESSED = "is_processed"
    }

    fun save(videoDTO: VideoDTO): VideoDTO {
        return statementService.singleQuery(
            "INSERT INTO $TABLE_NAME ($UUID_FIELD, $IS_PROCESSED) VALUES (?, ?)"
        ) { stmt ->
            stmt.setObject(1, videoDTO.uuid)
            stmt.setBoolean(2, videoDTO.isProcessed)
            stmt.executeQuery()
            return@singleQuery videoDTO
        }
    }

    fun findByUuid(uuid: UUID): VideoDTO? {
        return statementService.singleQuery(
            "SELECT $UUID_FIELD, $IS_PROCESSED FROM $TABLE_NAME WHERE $UUID_FIELD = (?)"
        ) { stmt ->
            stmt.setObject(1, uuid)
            val rs = stmt.executeQuery()
            if (rs.next()) {
                return@singleQuery VideoDTO(
                    uuid = rs.getObject(UUID_FIELD, UUID::class.java),
                    isProcessed = rs.getBoolean(IS_PROCESSED)
                )
            } else {
                null
            }
        }
    }

}