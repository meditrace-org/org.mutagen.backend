package org.mutagen.backend.service

import org.mutagen.backend.config.ApplicationConfig.Companion.ALPHA
import org.mutagen.backend.config.ApplicationConfig.Companion.BETA
import org.mutagen.backend.config.ApplicationConfig.Companion.LIMIT
import org.mutagen.backend.config.ApplicationConfig.Companion.SIMILAR_AUDIO_LIMIT
import org.mutagen.backend.config.ApplicationConfig.Companion.SIMILAR_VIDEO_LIMIT
import org.mutagen.backend.config.SqlScriptsConfig
import org.mutagen.backend.domain.model.VideoModel
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val text2VectorService: Text2VectorService,
    private val statementService: StatementService,
) {

    fun doSearch(queryText: String, strategy: String): List<VideoModel> {
        val vector = text2VectorService.getTextVector(queryText)
        val params = SqlScriptsConfig.getBestParams(strategy)
        val sql: String = SqlScriptsConfig.getSearchQuery(strategy)
            .replace(":audio_limit", SIMILAR_AUDIO_LIMIT.toString())
            .replace(":video_limit", SIMILAR_VIDEO_LIMIT.toString())
            .replace(":alpha", params.alpha.toString())
            .replace(":beta", params.beta.toString())
            .replace(":limit", LIMIT.toString())
            .replace(":target", vector?.asList().toString())

        val result = mutableListOf<VideoModel>()
        statementService.singleQuery(sql) { stmt, _ ->
            stmt.executeQuery().use { rs ->
                while (rs.next()) {
                    val uuid = rs.getString("uuid")
                    val videoUrl = rs.getString("video_url")
                    result.add(
                        VideoModel(
                            uuid = uuid,
                            videoUrl = videoUrl,
                        )
                    )
                }
            }
        }


        return result
    }
}