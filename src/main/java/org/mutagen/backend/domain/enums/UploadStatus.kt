package org.mutagen.backend.domain.enums

import io.swagger.v3.oas.annotations.media.Schema

enum class UploadStatus {
    @Schema(description = "Загрузка начата, сейчас видео скачивается / разбивается на чанки / преобразовывается в аудио")
    PREPROCESSING,

    @Schema(description = "Ошибка валидаци")
    VALIDATION_ERROR,

    @Schema(description = "В процессинге")
    PROCESSING,

    @Schema(description = "Успешно загружено в систему")
    SUCCESS,

    @Schema(description = "Видео не загружалось в последнее время")
    NOT_UPLOADED,
}