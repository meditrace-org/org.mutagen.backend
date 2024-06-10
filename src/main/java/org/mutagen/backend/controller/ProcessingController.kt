package org.mutagen.backend.controller

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.mutagen.backend.controller.ProcessingController.Companion.PROCESSING_PATH
import org.mutagen.backend.domain.dto.UploadVideoRequest
import org.mutagen.backend.domain.dto.ProcessingVideoResponse
import org.mutagen.backend.domain.enums.UploadStatus
import org.mutagen.backend.flow.UploadVideoFlow
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import ru.mephi.sno.libs.flow.belly.FlowContext
import ru.mephi.sno.libs.flow.registry.FlowRegistry

@RestController
@RequestMapping(PROCESSING_PATH)
@Tag(
    name = "Upload API",
    description = "API для загрузки видео"
)
open class ProcessingController {

    companion object {
        const val PROCESSING_PATH = "/api/v1/processing/"
        const val STATUS_ENDPOINT = "/status"
        const val UPLOAD_ENDPOINT = "/upload"
        const val URL_PARAM = "url"
    }

    private fun buildUploadStatusLink(url: String) =
        ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path(PROCESSING_PATH)
            .path(STATUS_ENDPOINT)
            .queryParam(URL_PARAM, url)
            .toUriString()

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Обработка успешно начата"),
        ]
    )
    @PostMapping(UPLOAD_ENDPOINT)
    fun uploadVideo(
        @RequestBody videoRequest: UploadVideoRequest
    ): ResponseEntity<ProcessingVideoResponse> {
        val flowBuilder = FlowRegistry.getInstance().getFlow(UploadVideoFlow::class.java)
        val flowContext = FlowContext().apply { insertObject(videoRequest) }
        flowBuilder.initAndRun(
            flowContext = flowContext,
            wait = false,
        )

        val uploadStatusUrl = buildUploadStatusLink(videoRequest.videoLink)
        val responseBody = ProcessingVideoResponse(
            message = "Request accepted for processing",
            uploadStatusUrl = uploadStatusUrl,
            UploadStatus.STARTED
        )

        return ResponseEntity(responseBody, HttpStatus.ACCEPTED)
    }

    @GetMapping(STATUS_ENDPOINT)
    fun uploadStatus(@RequestParam(URL_PARAM) url: String): ResponseEntity<ProcessingVideoResponse> {
        // TODO: get upload status
        val responseBody = ProcessingVideoResponse(
            uploadStatusUrl = buildUploadStatusLink(url),
            uploadStatus = UploadStatus.STARTED,
        )

        return ResponseEntity(responseBody, HttpStatus.OK)
    }
}
