package org.mutagen.backend.config

import org.mutagen.backend.config.ApplicationConfig.Companion.ALPHA
import org.mutagen.backend.config.ApplicationConfig.Companion.BETA
import org.mutagen.backend.config.ApplicationConfig.Companion.STRATEGY
import org.mutagen.backend.config.ApplicationConfig.Companion.paramsByStrategy
import org.mutagen.backend.domain.model.SearchQueryParam
import org.springframework.core.io.ClassPathResource
import java.io.FileNotFoundException
import java.io.InputStream
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
        }

        object Insert {
            val VIDEO_EMBEDDING: String = getContent("sql/insert_video_embedding.sql")
            val AUDIO_EMBEDDING: String = getContent("sql/insert_audio_embedding.sql")
            val FACE_EMBEDDING: String = getContent("sql/insert_face_embedding.sql")
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
            val resource = ClassPathResource(STRATEGIES_PATH).file
                .listFiles { file -> file.extension.lowercase() == sqlFormat }
                ?.map { it.nameWithoutExtension }
                ?: throw FileNotFoundException("Can't find any search strategy.")

            searchQueriesByStrategy = resource.associateWith {
                getContent(Paths.get(STRATEGIES_PATH, "$it.$sqlFormat").toString())
            }
            testSearchQueriesByStrategy = resource.associateWith {
                getContent(Paths.get(STRATEGIES_TEST_PATH, "$it.$sqlFormat").toString())
            }
        }
    }
}