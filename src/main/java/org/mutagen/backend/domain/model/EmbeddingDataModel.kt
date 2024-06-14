package org.mutagen.backend.domain.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class EmbeddingDataModel(
    val uuid: String,
    val model: String,
    val text: String?,
    val encodedChunk: List<Float>,
)