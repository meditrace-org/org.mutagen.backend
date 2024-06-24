package org.mutagen.backend.service

import org.mutagen.backend.advice.SearchAdvice.Companion.SearchControllerException
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

    fun doSearch(queryText: String, strategy: String, limit: Int = LIMIT): List<VideoModel> {
        val vector = runCatching {
            text2VectorService.getTextVector(queryText)
        }.onFailure {
            throw SearchControllerException(it.localizedMessage)
        }.getOrThrow()

        val params = SqlScriptsConfig.getBestParams(strategy)
        val sql: String = SqlScriptsConfig.getSearchQuery(strategy)
            .replace(":audio_limit", SIMILAR_AUDIO_LIMIT.toString())
            .replace(":video_limit", SIMILAR_VIDEO_LIMIT.toString())
            .replace(":alpha", params.alpha.toString())
            .replace(":beta", params.beta.toString())
            .replace(":limit", limit.toString())
            .replace(":target", vector?.asList().toString())

        val result = mutableListOf<VideoModel>()
        runCatching {
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
        }.onFailure {
            throw SearchControllerException(it.localizedMessage)
        }


        return result
    }
}