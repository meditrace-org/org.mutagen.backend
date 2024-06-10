package org.mutagen.backend.domain.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import org.mutagen.backend.controller.ProcessingController.Companion.PROCESSING_PATH
import org.mutagen.backend.domain.enums.UploadStatus

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonInclude(Include.NON_NULL)
@Schema(description = "Ответ от эндпоинтов $PROCESSING_PATH")
data class ProcessingVideoResponse(
    @field:Schema(
        description = "Сообщение от сервера",
        nullable = true,
    )
    val message: String? = null,

    @field:Schema(description = "URL для проверки статуса загрузки видео")
    val uploadStatusUrl: String,

    @field:Schema(
        description = "Статус процесса загрузки видео",
    )
    val uploadStatus: UploadStatus,
)
