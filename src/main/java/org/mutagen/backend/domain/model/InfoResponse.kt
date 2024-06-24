package org.mutagen.backend.domain.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import org.mutagen.backend.controller.ProcessingController.Companion.INFO_ENDPOINT
import org.mutagen.backend.controller.ProcessingController.Companion.PROCESSING_PATH

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonInclude(Include.NON_NULL)
@Schema(
    description = "Ответ от ручки $PROCESSING_PATH$INFO_ENDPOINT - полезная информация о системе"
)
data class InfoResponse(
    @field:Schema(
        description = "Количество одновременно обрабатываемых видео со стороны бэкенда (препроцессинг)",
        example = "219",
    )
    val processing: Int,

    @field:Schema(
        description = "Доступные поисковые стратегии"
    )
    val strategies: List<String>,
)