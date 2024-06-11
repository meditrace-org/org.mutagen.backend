package org.mutagen.backend.domain.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import org.mutagen.backend.controller.SearchController.Companion.SEARCH_ENDPOINT
import org.mutagen.backend.controller.SearchController.Companion.SEARCH_PATH
import org.mutagen.backend.domain.model.VideoModel

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonInclude(Include.NON_NULL)
@Schema(description = "Ответ от ручки $SEARCH_PATH$SEARCH_ENDPOINT")
data class SearchQueryResponse(
    @field:Schema(description = "Время в миллисекундах, затраченное на выполнение запроса")
    val executionTime: Long,

    @field:Schema(description = "Список найденных видео в порядке уменьшения релевантности")
    val result: List<VideoModel>,
)