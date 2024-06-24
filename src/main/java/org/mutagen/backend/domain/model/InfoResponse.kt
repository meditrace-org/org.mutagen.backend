package org.mutagen.backend.domain.model

data class InfoResponse(
    val processing: Int,
    val strategies: List<String>,
)