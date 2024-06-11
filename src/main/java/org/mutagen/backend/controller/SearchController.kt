package org.mutagen.backend.controller

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.mutagen.backend.controller.SearchController.Companion.SEARCH_PATH
import org.mutagen.backend.domain.dto.SearchQueryResponse
import org.mutagen.backend.service.CacheService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

        val SEARCH_URL = "$SEARCH_PATH/${SEARCH_ENDPOINT.trimStart('/')}"
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Успешно"),
        ]
    )
    @GetMapping("/$SEARCH_ENDPOINT")
    fun uploadStatus(@RequestParam query: String): ResponseEntity<SearchQueryResponse> {
        // TODO: check cache
        return ResponseEntity(
            null,
            HttpStatus.OK,
        )
    }
}