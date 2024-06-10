package org.mutagen.backend.domain.enums

import io.swagger.v3.oas.annotations.media.Schema

enum class UploadStatus {
    @Schema(description = "Загрузка не начиналась в последнее время")
    NOT_UPLOADED,

    @Schema(description = "Загрузка начата")
    STARTED,

    @Schema(description = "В процессинге")
    PROCESSING,

    @Schema(description = "Ошибка валидаци")
    VALIDATION_ERROR,

    @Schema(description = "Успешно загружено в систему")
    SUCCESS,
}