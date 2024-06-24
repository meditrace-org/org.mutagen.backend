package org.mutagen.backend.fetcher.test.quality

import org.mutagen.backend.config.ApplicationConfig.Companion.LIMIT
import org.mutagen.backend.config.ApplicationConfig.Companion.SIMILAR_AUDIO_LIMIT
import org.mutagen.backend.config.ApplicationConfig.Companion.SIMILAR_VIDEO_LIMIT
import org.mutagen.backend.config.SqlScriptsConfig
import org.mutagen.backend.domain.model.QualityTestData
import org.mutagen.backend.domain.model.VideoModel
import org.mutagen.backend.service.StatementService
import org.mutagen.backend.service.Text2VectorService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.belly.InjectData
import ru.mephi.sno.libs.flow.fetcher.GeneralFetcher

@Component
class QualityTestFetcher(
    private val text2VectorService: Text2VectorService,
    private val statementService: StatementService,
): GeneralFetcher() {

    companion object {
        private val log = LoggerFactory.getLogger(QualityTestFetcher::class.java)
    }

    @InjectData
    fun doFetch(testData: QualityTestData) {
        val paramsAsList = getParamsAsLists(testData)
        val names = paramsAsList.map { it.first }
        testData.apply {
            strategies.forEach {
                testStrategy(it, query, mapper, expectedDataEmbedding, testDataEmbedding, paramsAsList, names)
            }
        }
    }

    private fun getParamsAsLists(testData: QualityTestData): List<Pair<String, List<Float>>> {
        val result = mutableListOf<Pair<String, MutableList<Float>>>()
        testData.params.forEach {
            var cur = it.startValue
            val currentRes = Pair<String, MutableList<Float>>(it.name, mutableListOf())
            while (cur <= it.endValue) {
                currentRes.second.add(cur)
                cur += it.step
            }
            result.add(currentRes)
        }
        return result
    }

    private fun testStrategy(
        strategy: String,
        query: String,
        mapper: Map<String, Int>,
        expectedDataEmbedding: List<Int>,
        testDataEmbedding: List<Int>,
        paramsAsList: List<Pair<String, List<Float>>>,
        names: List<String>,
        index: Int = 0,
        result: List<Float> = listOf(),
    ) {
        if (index == paramsAsList.size) {
            val params = result.mapIndexed { ind, value -> names[ind] to value }.toMap()
            testStrategyWithParams(strategy, query, mapper, expectedDataEmbedding, testDataEmbedding, params)
            return
        }

        paramsAsList[index].second.forEach { value ->
            testStrategy(
                strategy,
                query,
                mapper,
                expectedDataEmbedding,
                testDataEmbedding,
                paramsAsList,
                names,
                index + 1,
                result + value,
            )
        }
    }

    private fun testStrategyWithParams(
        strategy: String,
        query: String,
        mapper: Map<String, Int>,
        expectedDataEmbedding: List<Int>,
        testDataEmbedding: List<Int>,
        params: Map<String, Float>
    ) {
        val vector = text2VectorService.getTextVector(query)
        val uuids = testDataEmbedding
            .map { it + .0f }
            .joinToString(prefix = "[", postfix = "]", transform = { "'$it'" })

        val sql: String = SqlScriptsConfig.getSearchQuery(strategy)
            .replace(":audio_limit", SIMILAR_AUDIO_LIMIT.toString())
            .replace(":video_limit", SIMILAR_VIDEO_LIMIT.toString())
            .replace(":limit", LIMIT.toString())
            .replace(":uuids", uuids)
            .replace(":target", vector?.asList().toString()).apply {
                params.forEach {
                    replace(":${it.key}", it.value.toString())
                }
            }

        val result = mutableListOf<VideoModel>()
        statementService.singleQuery(sql) { stmt, _ ->
            stmt.executeQuery().use { rs ->
                while (rs.next()) {
                    val uuid = rs.getString("uuid")
                    val videoUrl = rs.getString("video_url")
                    result.add(
                        VideoModel(
                            uuid = uuid,
                            videoUrl = videoUrl,
                        )
                    )
                }
            }
        }

    }
}