package org.mutagen.backend.domain.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonInclude(Include.NON_NULL)
@Schema(description = "Элемент видео")
data class VideoModel(
    @field:Schema(description = "Уникальный идентификатор видео")
    val uuid: UUID,

    @field:Schema(description = "URL видео")
    val videoUrl: String,
)