package org.mutagen.backend.service

import org.mutagen.backend.config.ApplicationConfig.Companion.paramsByStrategy
import org.mutagen.backend.config.SqlScriptsConfig.Companion.Select.BEST_PARAMETERS_BY_STRATEGY
import org.mutagen.backend.domain.model.SearchQueryParam
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class UpdateBestSearchParamsService(
    private val statementService: StatementService,
) {

    companion object {
        private val log = LoggerFactory.getLogger(UpdateBestSearchParamsService::class.java)
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000)
    fun updateSearchParamsByStrategies() {
        val query = BEST_PARAMETERS_BY_STRATEGY
        statementService.singleQuery(query) { stmt, _ ->
            val rs = stmt.executeQuery()
            while (rs.next()) {
                val alpha = rs.getFloat("alpha")
                val beta = rs.getFloat("beta")
                val strategy = rs.getString("strategy")
                updateParams(alpha, beta, strategy)
            }
        }
    }

    private fun updateParams(alpha: Float, beta: Float, strategy: String) {
        log.info("Update query params. New: alpha={}, beta={} for strategy {}", alpha, beta, strategy)
        paramsByStrategy[strategy] = SearchQueryParam(alpha, beta)
    }
}