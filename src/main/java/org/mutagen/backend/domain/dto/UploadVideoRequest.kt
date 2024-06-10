package org.mutagen.backend.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Загружаемое видео")
data class UploadVideoRequest(
    @field:Schema(
        name = "video_link",
        description = "Ссылка на видео",
        example = "https://cdn-st.rutubelist.ru/media/fe/92/79d806dc4ff493eb1da6b1c97c14/fhd.mp4"
    )
    @JsonProperty("video_link")
    val videoLink: String,

    @field:Schema(
        description = "Описание видео",
        required = false,
        example = "#boobs , #bigass , #girls , #pussy , #еда , #готовка , #рецепт , " +
                "#кукинг , #мистика , #страшилка , #horror , #бизнес , #инвестиции"
    )
    val description: String? = null,
)
