package org.mutagen.backend.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.mutagen.backend.config.ApplicationConfig
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Service
class Text2VectorService {

    companion object {
        private val log = LoggerFactory.getLogger(Text2VectorService::class.java)
    }

    fun getTextVector(text: String): FloatArray? {
        val baseUrl = ApplicationConfig.TEXT2VECTOR_URL

        val urlBuilder = StringBuilder(baseUrl).append("?input=").append(text)

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
            return responseObject.result.toFloatArray()
        } else {
            log.error("GET request to text2vector failed with response code $responseCode")
            return null
        }
    }

    private data class Text2VectorResponse(
        val result: List<Float>
    )
}