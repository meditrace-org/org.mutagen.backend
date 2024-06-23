package org.mutagen.backend.config

import org.mutagen.backend.config.ApplicationConfig.Companion.STRATEGY
import org.springframework.core.io.ClassPathResource
import java.io.FileNotFoundException
import java.io.InputStream
import java.nio.file.Paths


open class SqlScriptsConfig {

    companion object {
        val BEST_PARAMETERS_QUERY: String = getContent("sql/select_best_parameters.sql")

        private val searchQueriesByStrategy: Map<String, String>
        private const val STRATEGIES_PATH = "/sql/search/strategy"

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

        private fun getSearchQuery(strategy: String): String {
            return searchQueriesByStrategy[strategy]
                ?: throw RuntimeException("Strategy $strategy not found")
        }

        fun getSearchQuery() = getSearchQuery(STRATEGY)

        init {
            val sqlFormat = "sql"
            val resource = ClassPathResource(STRATEGIES_PATH).file
                .listFiles { file -> file.extension.lowercase() == sqlFormat }
                ?.map { it.nameWithoutExtension }
                ?: throw FileNotFoundException("Can't find any search strategy.")

            searchQueriesByStrategy = resource.associateWith {
                getContent(Paths.get(STRATEGIES_PATH, "$it.$sqlFormat").toString())
            }
        }
    }
}