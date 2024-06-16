package org.mutagen.backend.config

import org.springframework.core.io.ClassPathResource
import java.io.InputStream


open class SqlScriptsConfig {

    companion object {
        val BEST_PARAMETERS_QUERY: String = getContent("sql/select_best_parameters.sql")
        val SEARCH_QUERY: String = getContent("sql/select_search_query.sql")

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
    }
}