package org.mutagen.backend.controller

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.mutagen.backend.controller.SearchController.Companion.SEARCH_PATH
import org.mutagen.backend.domain.model.SearchQueryResponse
import org.mutagen.backend.flow.SearchFlow
import org.mutagen.backend.service.CacheService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.mephi.sno.libs.flow.belly.FlowContext
import ru.mephi.sno.libs.flow.fetcher.SystemFields
import ru.mephi.sno.libs.flow.registry.FlowRegistry
import kotlin.system.measureTimeMillis

@RestController
@RequestMapping(SEARCH_PATH)
@Tag(
    name = "Search API",
    description = "API для текстового поиска по загруженным видео"
)
open class SearchController(
    private val cacheService: CacheService,
) {

    companion object {
        const val SEARCH_PATH = "/api/v1/search/"
        const val SEARCH_ENDPOINT = "find"
        const val QUERY_PARAM = "query"
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Успешно"),
        ]
    )
    @GetMapping("/$SEARCH_ENDPOINT")
    fun find(@RequestParam(QUERY_PARAM) query: String): ResponseEntity<SearchQueryResponse> {
        var result: SearchQueryResponse
        val time = measureTimeMillis {
            result = getSearchResult(query)
        }
        result = result.also { it.executionTime = time }

        return ResponseEntity(
            result,
            if (result.isFailure) HttpStatus.INTERNAL_SERVER_ERROR else HttpStatus.OK
        )
    }

    private fun getSearchResult(query: String): SearchQueryResponse {
        cacheService.getResultForQuery(query)?.let {
            return it
        }

        val flowBuilder = FlowRegistry.getInstance().getFlow(SearchFlow::class.java)
        val flowContext = FlowContext().apply {
            insertObject(query)
        }
        flowBuilder.initAndRun(
            flowContext = flowContext,
            wait = true,
        )
        val result = flowContext.get<SearchQueryResponse>() ?: SearchQueryResponse(
            message = flowContext.get<SystemFields>()
                ?.exception
                ?.cause
                ?.stackTraceToString()
                ?.shortMessage() ?: "Server error",
            isFailure = true,
        )
        result.let { cacheService.setResultForQuery(query, result) }
        return result
    }

    private fun String.shortMessage(maxLength: Int = 800): String {
        return if (this.length > maxLength) {
            this.substring(0, maxLength - 3).plus("...")
        } else {
            this
        }
    }
}