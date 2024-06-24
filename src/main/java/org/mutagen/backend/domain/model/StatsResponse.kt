package org.mutagen.backend.domain.model

data class StatsResponse(
    val processing: Int,
    val strategies: List<String>,
)