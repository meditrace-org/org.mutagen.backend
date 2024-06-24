package org.mutagen.backend.config

import org.mutagen.backend.config.ApplicationConfig.Companion.ALPHA
import org.mutagen.backend.config.ApplicationConfig.Companion.BETA
import org.mutagen.backend.config.ApplicationConfig.Companion.STRATEGY
import org.mutagen.backend.config.ApplicationConfig.Companion.paramsByStrategy
import org.mutagen.backend.domain.model.SearchQueryParam
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Paths


open class SqlScriptsConfig {

    companion object {
        private val searchQueriesByStrategy: Map<String, String>
        private val testSearchQueriesByStrategy: Map<String, String>
        private const val STRATEGIES_PATH = "/sql/search/strategy"
        private const val STRATEGIES_TEST_PATH = "/sql/search/strategy/test"

        object Select {
            val BEST_PARAMETERS_BY_STRATEGY: String = getContent("sql/select_best_parameters_by_strategy.sql")
            val VIDEOS_COUNT_BY_UUID: String = getContent("sql/select_videos_count_by_uuids.sql")
            val CHECK_PROCESSED_VIDEO_BY_URL: String = getContent("sql/select_video_embds.sql")
        }

        object Insert {
            val VIDEO_EMBEDDING: String = getContent("sql/insert_video_embedding.sql")
            val AUDIO_EMBEDDING: String = getContent("sql/insert_audio_embedding.sql")
            val FACE_EMBEDDING: String = getContent("sql/insert_face_embedding.sql")
            val TEST_RESULT: String = getContent("sql/insert_test_result.sql")
        }

        object Delete {
            val DELETE_BAD_AUDIO_EMB: String = getContent("sql/delete_bad_audio_embeddings.sql")
        }

        private fun getContent(path: String): String {
            val resource = ClassPathResource(path)
            val inputStream: InputStream = resource.inputStream
            return inputStream.bufferedReader().use { it.readText() }
        }

        fun getSearchQuery(strategy: String): String {
            return searchQueriesByStrategy[strategy]
                ?: throw RuntimeException("Strategy $strategy not found")
        }

        fun getTestSearchQuery(strategy: String): String {
            return searchQueriesByStrategy[strategy]
                ?: throw RuntimeException("Strategy $strategy not found")
        }

        fun getBestParams(strategy: String): SearchQueryParam {
            return paramsByStrategy[strategy] ?: SearchQueryParam(ALPHA, BETA)
        }

        fun getAllStrategies() = searchQueriesByStrategy.keys.toList()

        init {
            val sqlFormat = "sql"
            val resolver = PathMatchingResourcePatternResolver()

            val resources = resolver
                .getResources("classpath:${Paths.get(STRATEGIES_PATH, "*.sql")}")
            val sqlFiles = resources.mapNotNull { resource ->
                try {
                    resource.inputStream.use { inputStream ->
                        BufferedReader(InputStreamReader(inputStream)).use {
                            resource.filename?.uppercase()?.removeSuffix(".$sqlFormat".uppercase())
                        }
                    }
                } catch (e: Exception) {
                    null
                }
            }

            searchQueriesByStrategy = sqlFiles.associateWith {
                getContent(Paths.get(STRATEGIES_PATH, "$it.$sqlFormat").toString())
            }
            testSearchQueriesByStrategy = sqlFiles.associateWith {
                getContent(Paths.get(STRATEGIES_TEST_PATH, "$it.$sqlFormat").toString())
            }
        }
    }
}