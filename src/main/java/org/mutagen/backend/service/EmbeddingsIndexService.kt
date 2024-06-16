package org.mutagen.backend.service

import org.mutagen.backend.config.SqlScriptsConfig.Companion.Delete.DELETE_BAD_AUDIO_EMB
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class EmbeddingsIndexService(
    private val statementService: StatementService,
) {

    companion object {
        private val log = LoggerFactory.getLogger(UpdateBestSearchParamsService::class.java)
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000)
    fun deleteBadAudioEmbeddings() {
        runCatching {
            val query = DELETE_BAD_AUDIO_EMB
            statementService.simpleQuery(query)
        }.onSuccess {
            log.info("Bad audio embeddings was deleted successfully")
        }
    }
}