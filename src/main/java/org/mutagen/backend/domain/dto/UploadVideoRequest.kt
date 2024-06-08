package org.mutagen.backend.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Загружаемое видео")
data class UploadVideoRequest(
    @Schema(
        description = "Ссылка на видео",
        example = "https://cdn-st.ritm.media/media/00/5d/fa5bcb3d40479d06d416d43a62ba/fhd.mp4"
    )
    @JsonProperty("video_link")
    val videoLink: String,

    @Schema(
        description = "Описание видео",
        required = false,
        example = "some example"
    )
    val description: String? = null,
)
