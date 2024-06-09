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
        const val VIDEO_URL = "url"
        const val IS_PROCESSED = "is_processed"
    }

    fun save(videoDTO: VideoDTO): VideoDTO {
        return statementService.singleQuery(
            "INSERT INTO $TABLE_NAME ($UUID_FIELD, $IS_PROCESSED, $VIDEO_URL) VALUES (?, ?, ?)"
        ) { stmt ->
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
        ) { stmt ->
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

    fun findByUrl(videoUrl: String): VideoDTO? {
        return statementService.singleQuery(
            "SELECT $UUID_FIELD, $IS_PROCESSED, $VIDEO_URL FROM $TABLE_NAME WHERE $VIDEO_URL = (?)"
        ) { stmt ->
            stmt.setString(1, videoUrl)
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

    fun isVideoWithLinkExists(videoUrl: String): Boolean {
        return statementService.singleQuery(
            "SELECT COUNT(1) FROM $TABLE_NAME WHERE $VIDEO_URL = (?)"
        ) { stmt ->
            stmt.setObject(1, videoUrl)
            val rs = stmt.executeQuery()
            return@singleQuery rs.next() && rs.getInt(1) == 1
        }
    }

}