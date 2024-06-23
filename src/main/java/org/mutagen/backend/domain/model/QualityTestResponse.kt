package org.mutagen.backend.domain.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import org.mutagen.backend.controller.TestController.Companion.QUALITY_TEST_ENDPOINT
import org.mutagen.backend.controller.TestController.Companion.TEST_PATH

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonInclude(Include.NON_NULL)
@Schema(
    description = "Ответ от ручки $TEST_PATH$QUALITY_TEST_ENDPOINT - результат тестирования стратегий и параметров"
)
data class QualityTestResponse (
    @field:Schema(description = "Лучшая стратегия")
    val bestStrategy: String,

    @field:Schema(description = "Лучший набор параметров")
    val bestParam: List<BestParamModel>,

    @field:Schema(description = "Результат этой стратегии")
    val result: List<String>,

    @field:Schema(description = "Схожесть с ожидаемым результатом")
    val score: Float,

    @field:Schema(description = "Сообщение от сервера")
    val message: String
)