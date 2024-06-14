package org.mutagen.backend.config

import org.springframework.core.io.ClassPathResource
import java.io.InputStream


open class SqlScriptsConfig {

    companion object {
        val BEST_PARAMETERS_QUERY: String = getContent("sql/select_best_parameters.sql")
        val SEARCH_QUERY: String = getContent("sql/select_search_query.sql")

        private fun getContent(path: String): String {
            val resource = ClassPathResource(path)
            val inputStream: InputStream = resource.inputStream
            return inputStream.bufferedReader().use { it.readText() }
        }
    }
}