package org.mutagen.backend.domain.enums

import io.swagger.v3.oas.annotations.media.Schema

enum class UploadStatus {
    @Schema(
        description = "Загрузка начата",
    )
    STARTED,

    // TODO: statuses

    @Schema(
        description = "Успешно загружено в систему",
    )
    SUCCESS,
}