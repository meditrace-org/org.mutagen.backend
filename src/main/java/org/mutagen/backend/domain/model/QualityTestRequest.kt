package org.mutagen.backend.domain.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Запрос тестирования качества")
data class QualityTestRequest (
    @field:Schema(
        name = "test_videos",
        description = "Список UUID видео по которым производится поиск",
        example = "[\"d3939c32-b881-4871-a783-db56e4ba2088\", " +
                "\"8e5b6dbd-c624-46c5-a668-e7b6284499fc\", " +
                "\"2a2d445b-f1b1-4584-8f19-3ff996ad2d13\"]"
    )
    @JsonProperty("test_videos")
    val testVideos: List<String>,

    @field:Schema(
        name = "expected_result",
        description = "Список UUID видео - ожидаемый (идеальный) результат",
        example = "[\"8e5b6dbd-c624-46c5-a668-e7b6284499fc\", " +
                "\"d3939c32-b881-4871-a783-db56e4ba2088\", " +
                "\"2a2d445b-f1b1-4584-8f19-3ff996ad2d13\"]"
    )
    @JsonProperty("expected_result")
    val expectedResult: List<String>,

    @field:Schema(
        name = "params",
        description = "Тестируемые параметры"
    )
    @JsonProperty("params")
    val params: List<TestParamModel>,

    @field:Schema(
        name = "strategies",
        nullable = true,
        description = "Тестируемые стратегии. По умолчанию тестируются все стратегии",
        example = "[\"QUANTILE\"]"
    )
    @JsonProperty("strategies")
    val strategies: List<String>?,
)