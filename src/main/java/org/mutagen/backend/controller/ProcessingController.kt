package org.mutagen.backend.controller

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.mutagen.backend.controller.ProcessingController.Companion.PROCESSING_PATH
import org.mutagen.backend.domain.dao.VideoDAO
import org.mutagen.backend.domain.model.UploadVideoRequest
import org.mutagen.backend.domain.model.ProcessingVideoResponse
import org.mutagen.backend.domain.enums.UploadStatus
import org.mutagen.backend.domain.model.StatsResponse
import org.mutagen.backend.flow.UploadVideoFlow
import org.mutagen.backend.service.CacheService
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
open class ProcessingController(
    private val cacheService: CacheService,
    private val videoDAO: VideoDAO
) {

    companion object {
        const val PROCESSING_PATH = "/api/v1/processing/"
        const val STATUS_ENDPOINT = "status"
        const val UPLOAD_ENDPOINT = "upload"

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
    @PostMapping("/$UPLOAD_ENDPOINT")
    fun uploadVideo(
        @RequestBody videoRequest: UploadVideoRequest
    ): ResponseEntity<ProcessingVideoResponse> {
        val uploadStatusUrl = buildUploadStatusLink(videoRequest.videoLink)

        val flowBuilder = FlowRegistry.getInstance().getFlow(UploadVideoFlow::class.java)
        val flowContext = FlowContext().apply {
            insertObject(videoRequest)
            insertObject(uploadStatusUrl)
        }
        flowBuilder.initAndRun(
            flowContext = flowContext,
            wait = false,
        )

        val responseBody = ProcessingVideoResponse(
            message = "Request accepted for processing",
            uploadStatusUrl = uploadStatusUrl,
            UploadStatus.PREPROCESSING
        )

        return ResponseEntity(responseBody, HttpStatus.ACCEPTED)
    }

    @GetMapping("/$STATUS_ENDPOINT")
    fun uploadStatus(@RequestParam(URL_PARAM) url: String): ResponseEntity<ProcessingVideoResponse> {
        cacheService.getStatus(url)?.let {
            return ResponseEntity(it, HttpStatus.OK)
        }

        val uploadStatusUrl = buildUploadStatusLink(url)
        val videoDTO = videoDAO.findByUrl(url)
            ?: return ResponseEntity(
                ProcessingVideoResponse(
                    message = "Video hasn't been uploaded recently",
                    uploadStatusUrl = uploadStatusUrl,
                    uploadStatus = UploadStatus.NOT_UPLOADED,
                ),
                HttpStatus.OK
            )

        val response = when(videoDTO.isProcessed) {
            true -> ProcessingVideoResponse(
                message = "Uploaded successfully",
                uploadStatusUrl = uploadStatusUrl,
                uploadStatus = UploadStatus.SUCCESS,
            )
            false -> ProcessingVideoResponse(
                message = "In processing",
                uploadStatusUrl = uploadStatusUrl,
                uploadStatus = UploadStatus.PROCESSING,
            )
        }
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/stats")
    fun getAllVideos(): ResponseEntity<StatsResponse> {
        val runs = FlowRegistry
            .getInstance()
            .getFlow(UploadVideoFlow::class.java)
            .flowRunsCount()

        return ResponseEntity(
            StatsResponse(runs),
            HttpStatus.OK
        )
    }
}
