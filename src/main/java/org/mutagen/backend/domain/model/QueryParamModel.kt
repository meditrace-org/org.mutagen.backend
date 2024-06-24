package org.mutagen.backend.domain.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Подобранный лучший параметр")
data class QueryParamModel (
    @field:Schema(
        name = "name",
        description = "Имя параметра",
        example = "alpha"
    )
    @JsonProperty("name")
    val name: String = "alpha",

    @field:Schema(
        name = "value",
        description = "Оптимальное значение",
        example = "0.77"
    )
    @JsonProperty("best_value")
    val value: Float = 0.77f,
)