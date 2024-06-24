package org.mutagen.backend.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import org.mutagen.backend.config.ApplicationConfig
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@Service
class Text2VectorService {

    companion object {
        private val log = LoggerFactory.getLogger(Text2VectorService::class.java)
    }

    fun getTextVector(text: String): FloatArray {
        val baseUrl = ApplicationConfig.TEXT2VECTOR_URL

        val urlBuilder = StringBuilder(baseUrl)
            .append("?input=")
            .append(URLEncoder.encode(text, "UTF-8"))

        val url = URL(urlBuilder.toString())

        log.info("Getting query t2v by url: {}", url.toString())

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonResponse = reader.use { it.readText() }

            val objectMapper = ObjectMapper()
            val responseObject = objectMapper.readValue(jsonResponse, Text2VectorResponse::class.java)

            connection.inputStream.close()

            log.info("Got vectors for query {} in {} sec", text, responseObject.time)
            return responseObject.result.toFloatArray()
        } else {
            throw RuntimeException("GET request to text2vector $url failed with response code $responseCode")
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Text2VectorResponse(
        val time: Float = 0.0f,
        val result: List<Float> = listOf(),
    )
}