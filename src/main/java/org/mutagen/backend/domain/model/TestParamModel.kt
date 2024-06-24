package org.mutagen.backend.domain.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Тестируемый поисковой параметр")
data class TestParamModel(
    @field:Schema(
        name = "name",
        description = "Имя параметра",
        example = "alpha"
    )
    @JsonProperty("name")
    val name: String = "alpha",

    @field:Schema(
        name = "start_value",
        description = "Начальное значение тестируемого параметра",
        example = "1.0"
    )
    @JsonProperty("start_value")
    val startValue: Float = 0.0f,

    @field:Schema(
        name = "end_value",
        description = "Конечное значение тестируемого параметра",
        example = "2.0"
    )
    @JsonProperty("end_value")
    val endValue: Float = 2.0f,

    @field:Schema(
        name = "step",
        description = "Шаг",
        example = "0.05"
    )
    @JsonProperty("step")
    val step: Float = 0.05f,
)