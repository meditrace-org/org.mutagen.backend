package org.mutagen.backend.controller

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.mutagen.backend.config.ApplicationConfig.Companion.STRATEGY
import org.mutagen.backend.controller.SearchController.Companion.SEARCH_PATH
import org.mutagen.backend.domain.model.SearchQueryResponse
import org.mutagen.backend.service.CacheService
import org.mutagen.backend.service.SearchService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.system.measureTimeMillis

@RestController
@RequestMapping(SEARCH_PATH)
@Tag(
    name = "Search API",
    description = "API для текстового поиска по загруженным видео"
)
open class SearchController(
    private val cacheService: CacheService,
    private val searchService: SearchService,
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
    fun find(
        @Parameter(description = "Текст запроса для поиска", required = true)
        @RequestParam(QUERY_PARAM) query: String,

        @Parameter(description = "Поисковая стратегия", required = false)
        @RequestParam(required = false) queryStrategy: String?
    ): ResponseEntity<SearchQueryResponse> {
        val strategy = queryStrategy ?: STRATEGY

        var result: SearchQueryResponse
        val time = measureTimeMillis {
            result = getSearchResult(query, strategy)
        }
        result = result.also { it.executionTime = time }

        return ResponseEntity(
            result,
            HttpStatus.OK
        )
    }

    private fun getSearchResult(queryText: String, strategy: String): SearchQueryResponse {
        cacheService.getResultForQuery(queryText)?.let {
            return it
        }

        val result = SearchQueryResponse(
            message = "success",
            result = searchService.doSearch(queryText, strategy)
        )

        result.let { cacheService.setResultForQuery(queryText, result) }
        return result
    }
}