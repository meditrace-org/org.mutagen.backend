package org.mutagen.backend.domain.model

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
        example = "Request accepted for processing",
    )
    val message: String? = null,

    @field:Schema(
        description = "URL для проверки статуса загрузки видео",
        example = "http://localhost:5004/api/v1/processing/status?url=https://cdn-st.rutubelist.ru/media/64/4a/4dd1fd724029a9893396a5cc3c45/fhd.mp4",
    )
    val uploadStatusUrl: String,

    @field:Schema(description = "Статус процесса загрузки видео")
    val uploadStatus: UploadStatus,
)
