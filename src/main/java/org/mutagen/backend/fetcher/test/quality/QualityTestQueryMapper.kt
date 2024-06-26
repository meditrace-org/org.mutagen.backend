package org.mutagen.backend.fetcher.test.quality

import org.mutagen.backend.advice.QualityTestAdvice.Companion.QualityTestException
import org.mutagen.backend.config.SqlScriptsConfig
import org.mutagen.backend.config.SqlScriptsConfig.Companion.Select.VIDEOS_COUNT_BY_UUID
import org.mutagen.backend.domain.model.QualityTestData
import org.mutagen.backend.domain.model.QualityTestRequest
import org.mutagen.backend.domain.model.TestParamModel
import org.mutagen.backend.service.StatementService
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher
import java.util.*
import kotlin.math.floor

@Component
class QualityTestQueryMapper(
    private val statementService: StatementService
) : GeneralFetcher() {

    companion object {
        private val diffSizesException =
            QualityTestException("The expected set of videos should not exceed the length of the test set.")
        private val diffContentException =
            QualityTestException("The expected set of videos must be a subset of the test set.")
        private val iterationLimitException =
            QualityTestException("Too many iterations.")
        private val emptyParametersException =
            QualityTestException("Empty parameters.")
        private val incorrectParametersException =
            QualityTestException("Incorrect parameters.")
        private val tooSmallStepException =
            QualityTestException("Step value of one of the parameters is too small.")
        private val incorrectParamName =
            QualityTestException("Only english letters and underscores are allowed in parameter names.")
        private val incorrectUuid =
            QualityTestException("Incorrect UUID values found")
        private val nonExistentVideosException =
            QualityTestException("Can't find some videos. Check uuid's")
        private val nonExistentStrategiesException =
            QualityTestException("Can't find some strategies. " +
                    "Available volumes: ${SqlScriptsConfig.getAllStrategies()}")
        private val nonUniqueSetsException =
            QualityTestException("Data sets should have unique elements.")

        private const val STEPS_LIMIT = 20
        private const val eps = 0.000001f
    }

    @InjectData
    fun doFetch(qualityTestRequest: QualityTestRequest): QualityTestData {
        checkParams(qualityTestRequest)
        checkUuids(qualityTestRequest.testVideos)

        val expectedSet = qualityTestRequest.expectedResult.toSet()
        val testDataSet = qualityTestRequest.testVideos.toSet()

        if (expectedSet.size != qualityTestRequest.expectedResult.size)
            throw nonUniqueSetsException
        if (expectedSet.size != qualityTestRequest.testVideos.size)
            throw nonUniqueSetsException

        if (expectedSet.size < testDataSet.size)
            throw diffSizesException
        if (!testDataSet.containsAll(expectedSet))
            throw diffContentException

        val videoMapper = qualityTestRequest.testVideos.mapIndexed { index, testVideo ->
            testVideo to index
        }.toMap()
        val expectedDataEmbedding = qualityTestRequest.expectedResult.map {
            videoMapper[it] ?: throw diffContentException
        }
        val testDataEmbedding = videoMapper.values.toList()
        val strategies = qualityTestRequest.strategies
            .takeIf { it?.isNotEmpty() ?: false }
            ?: SqlScriptsConfig.getAllStrategies()

        if (!SqlScriptsConfig.getAllStrategies().toSet().containsAll(strategies.toSet())) {
            throw nonExistentStrategiesException
        }

        checkVideosIsExists(expectedSet)

        return QualityTestData(
            query = qualityTestRequest.testQuery,
            mapper = videoMapper,
            expectedDataEmbedding = expectedDataEmbedding,
            testDataEmbedding = testDataEmbedding,
            params = qualityTestRequest.params,
            strategies = strategies,
        )
    }

    private fun checkUuids(uuids: List<String>) {
        uuids.forEach {
            checkIsStringUuid(it)
        }
    }

    private fun checkParams(qualityTestRequest: QualityTestRequest) {
        qualityTestRequest.params.apply {
            if (isEmpty()) throw emptyParametersException
            forEach { checkParam(it) }
        }
    }

    private fun checkParam(param: TestParamModel) {
        param.apply {
            if (endValue < startValue)
                throw incorrectParametersException
            if (step < eps )
                throw tooSmallStepException
            val stepsCount = floor((endValue - startValue) / step).toInt() + 1
            if (stepsCount > STEPS_LIMIT)
                throw iterationLimitException

            if (!Regex("[a-zA-Z_]+").matches(name))
                throw incorrectParamName
        }
    }

    private fun checkIsStringUuid(uuidString: String) {
        try {
            UUID.fromString(uuidString)
        } catch (e: Exception) {
            throw incorrectUuid
        }
    }

    private fun checkVideosIsExists(testDataSet: Set<String>) {
        val sql: String = VIDEOS_COUNT_BY_UUID
            .replace(
                ":uuids",
                testDataSet.joinToString(prefix = "[", postfix = "]", transform = { "'$it'" })
            )

        val isOk = statementService.singleQuery(sql) { stmt, _ ->
            val rs = stmt.executeQuery()
            return@singleQuery rs.next() && rs.getInt(1) == testDataSet.size
        }

        if (!isOk) throw nonExistentVideosException
    }
}