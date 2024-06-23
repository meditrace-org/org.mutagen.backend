package org.mutagen.backend.fetcher.test.quality

import org.mutagen.backend.advice.QualityTestAdvice.Companion.QualityTestException
import org.mutagen.backend.config.SqlScriptsConfig
import org.mutagen.backend.domain.model.QualityTestData
import org.mutagen.backend.domain.model.QualityTestRequest
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class QualityTestQueryMapper : GeneralFetcher() {

    companion object {
        private val diffSizes = QualityTestException("Expected result and test videos arrays has different lengths.")
        private val diffContent = QualityTestException("Expected result and test videos contains different videos.")
    }

    @InjectData
    fun doFetch(qualityTestRequest: QualityTestRequest): QualityTestData {
        val expectedSet = qualityTestRequest.expectedResult.toSet()
        val testDataSet = qualityTestRequest.testVideos.toSet()

        if (expectedSet.size != testDataSet.size)
            throw diffSizes
        if (expectedSet.any { !testDataSet.contains(it) })
            throw diffContent

        val videoMapper = qualityTestRequest.testVideos.mapIndexed { index, testVideo ->
            testVideo to index
        }.toMap()
        val expectedDataEmbedding = qualityTestRequest.expectedResult.map {
            videoMapper[it] ?: throw diffContent
        }
        val testDataEmbedding = videoMapper.values.toList()
        val strategies = qualityTestRequest.strategies
            .takeIf { it?.isNotEmpty() ?: false }
            ?: SqlScriptsConfig.getAllStrategies()

        return QualityTestData(
            mapper = videoMapper,
            expectedDataEmbedding = expectedDataEmbedding,
            testDataEmbedding = testDataEmbedding,
            params = qualityTestRequest.params,
            strategies = strategies
        )
    }
}