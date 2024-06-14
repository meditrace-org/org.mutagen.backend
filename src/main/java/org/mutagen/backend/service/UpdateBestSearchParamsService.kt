package org.mutagen.backend.service

import org.mutagen.backend.config.ApplicationConfig.Companion.ALPHA
import org.mutagen.backend.config.ApplicationConfig.Companion.BETA
import org.mutagen.backend.config.SqlScriptsConfig
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

    @Scheduled(fixedDelay = 10 * 60 * 1000)
    fun updateSearchParams() {
        val query = SqlScriptsConfig.BEST_PARAMETERS_QUERY
            .replace(":default_alpha", ALPHA.toString())
            .replace(":default_beta", BETA.toString())
        statementService.singleQuery(query) { stmt, _ ->
            val rs = stmt.executeQuery()
            if (rs.next()) {
                val new_alpha = rs.getFloat("alpha")
                val new_beta = rs.getFloat("beta")
                updateParams(new_alpha, new_beta)
                return@singleQuery
            }
        }
    }

    private fun updateParams(alpha: Float, beta: Float) {
        log.info("Update query params. New: alpha={}, beta={}", alpha, beta)
        ALPHA = alpha
        BETA = beta
    }
}