package org.mutagen.backend.domain.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(Include.NON_NULL)
data class EmbeddingDataModel(
    @JsonProperty("uuid")
    val uuid: String,

    @JsonProperty("model")
    val model: String,

    @JsonProperty("text")
    val text: String? = null,

    @JsonProperty("encoded_chunk")
    val encodedChunk: List<Float> = listOf(),
)