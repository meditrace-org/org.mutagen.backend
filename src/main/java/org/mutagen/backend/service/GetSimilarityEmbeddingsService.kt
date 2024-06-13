package org.mutagen.backend.service

import org.mutagen.backend.domain.dto.EmbeddingDTO
import org.springframework.stereotype.Service

@Service
class GetSimilarityEmbeddingsService(
    private val statementService: StatementService,
) {
    private fun getSimilarity(
        tableName: String,
        idField: String,
        embeddingField: String,
        annoy: String,
        limit: Int,
        targetVector: Array<Float>,
    ): Array<EmbeddingDTO?> {
        val result = arrayOfNulls<EmbeddingDTO>(limit)
        val query = """
        SELECT
            $idField,
            $embeddingField
        FROM $tableName
        WHERE
            $annoy($embeddingField, ?) <= 1000
        ORDER BY cosineDistance($embeddingField, ?) DESC
        LIMIT $limit
    """.trimIndent()

        statementService.singleQuery(query) { stmt, conn ->
            stmt.apply {
                setObject(1, conn.createArrayOf("Float32", targetVector))
                setArray(2, conn.createArrayOf("Float32", targetVector))
            }.executeQuery().use { rs ->
                var pos = 0
                while (rs.next()) {
                    val id = rs.getString(idField)
                    val value = rs.getArray(embeddingField).array as FloatArray
                    result[pos] = EmbeddingDTO(id, value)
                    pos++
                }
            }
        }

        return result
    }


}