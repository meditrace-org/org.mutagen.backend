package org.mutagen.backend.domain.model

data class QualityTestData(
    val query: String,
    val mapper: Map<String, Int>,
    val expectedDataEmbedding: List<Int>,
    val testDataEmbedding: List<Int>,
    val params: List<TestParamModel>,
    val strategies: List<String>
)