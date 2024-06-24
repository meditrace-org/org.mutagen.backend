package org.mutagen.backend.domain.dao

import org.mutagen.backend.config.SqlScriptsConfig.Companion.Select.CHECK_PROCESSED_VIDEO_BY_URL
import org.mutagen.backend.domain.dto.VideoDTO
import org.mutagen.backend.service.StatementService
import org.springframework.stereotype.Component
import java.util.*

@Component
class VideoDAO(
    private val statementService: StatementService,
) {
    companion object Fields {
        const val TABLE_NAME = "vr.video"
        const val UUID_FIELD = "uuid"
        const val VIDEO_URL = "url"
        const val IS_PROCESSED = "is_processed"
    }

    fun save(videoDTO: VideoDTO): VideoDTO {
        return statementService.singleQuery(
            "INSERT INTO $TABLE_NAME ($UUID_FIELD, $IS_PROCESSED, $VIDEO_URL) VALUES (?, ?, ?)"
        ) { stmt, _ ->
            stmt.setObject(1, videoDTO.uuid)
            stmt.setBoolean(2, videoDTO.isProcessed)
            stmt.setString(3, videoDTO.videoUrl)
            stmt.executeQuery()
            return@singleQuery videoDTO
        }
    }

    fun findByUuid(uuid: UUID): VideoDTO? {
        return statementService.singleQuery(
            "SELECT $UUID_FIELD, $IS_PROCESSED, $VIDEO_URL FROM $TABLE_NAME WHERE $UUID_FIELD = (?)"
        ) { stmt, _ ->
            stmt.setObject(1, uuid)
            val rs = stmt.executeQuery()
            if (rs.next()) {
                return@singleQuery VideoDTO(
                    uuid = rs.getObject(UUID_FIELD, UUID::class.java),
                    isProcessed = rs.getBoolean(IS_PROCESSED),
                    videoUrl = rs.getString(VIDEO_URL)
                )
            } else {
                null
            }
        }
    }

    fun isProcessed(videoUrl: String): Boolean {
        val sql = CHECK_PROCESSED_VIDEO_BY_URL
        return statementService.singleQuery(sql) { stmt, _ ->
            stmt.setString(1, videoUrl)
            val rs = stmt.executeQuery()
            return@singleQuery rs.next() && rs.getInt(1) > 0
        }
    }

    // TODO: вынести запрос в resources
    fun isVideoWithUrlExists(videoUrl: String): Boolean {
        return statementService.singleQuery(
            """
                select count(1) > 0 from vr.embeddings e
                join vr.video o ON o.uuid = e.uuid
                where o.url = (?)
            """.trimIndent()
        ) { stmt, _ ->
            stmt.setString(1, videoUrl)
            val rs = stmt.executeQuery()
            return@singleQuery rs.next() && rs.getInt(1) > 0
        }
    }

}