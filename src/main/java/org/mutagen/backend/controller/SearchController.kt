package org.mutagen.backend.controller

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.mutagen.backend.controller.SearchController.Companion.SEARCH_PATH
import org.mutagen.backend.domain.model.SearchQueryResponse
import org.mutagen.backend.service.CacheService
import org.mutagen.backend.service.SearchService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.mephi.sno.libs.flow.fetcher.SystemFields
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
    fun find(@RequestParam(QUERY_PARAM) query: String): ResponseEntity<SearchQueryResponse> {
        var result: SearchQueryResponse
        val time = measureTimeMillis {
            result = getSearchResult(query)
        }
        result = result.also { it.executionTime = time }

        return ResponseEntity(
            result,
            HttpStatus.OK
        )
    }

    private fun getSearchResult(query: String): SearchQueryResponse {
        cacheService.getResultForQuery(query)?.let {
            return it
        }

        val result = SearchQueryResponse(
            message = "success",
            result = searchService.doSearch(query)
        )

        result.let { cacheService.setResultForQuery(query, result) }
        return result
    }
}