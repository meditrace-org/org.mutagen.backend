package org.mutagen.backend.fetcher.test.quality

import org.mutagen.backend.domain.model.QualityTestRequest
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class ValidateQueryFetcher : GeneralFetcher() {

    @InjectData
    fun doFetch(qualityTestRequest: QualityTestRequest) {
        val expectedSet = qualityTestRequest.expectedResult.toSet()
        val testDataSet = qualityTestRequest.testVideos.toSet()

        if (expectedSet.size != testDataSet.size)
            throw RuntimeException("Expected result and test videos arrays has different lengths.")
        if (expectedSet.any { !testDataSet.contains(it) })
            throw RuntimeException("Expected result and test videos contains different videos.")

        val transformTestDataMap = qualityTestRequest.testVideos.mapIndexed { index, testVideo ->
            testVideo to index
        }.toMap()


        val transformExpectedData = qualityTestRequest.expectedResult.map {
            transformTestDataMap[it]
        }
        val transformTestData = transformTestDataMap.values.toList()
    }
}