package org.mutagen.backend.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.mutagen.backend.controller.ProcessingController.Companion.PROCESSING_PATH
import org.mutagen.backend.domain.dto.UploadVideoRequest
import org.mutagen.backend.domain.dto.ProcessingVideoResponse
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
        const val URL_PARAM: String = "url"
    }

    @PostMapping(UPLOAD_ENDPOINT)
    fun uploadVideo(
        @RequestBody videoDTO: UploadVideoRequest
    ): ResponseEntity<ProcessingVideoResponse> {
        val uploadStatusUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(PROCESSING_PATH)
            .path(STATUS_ENDPOINT)
            .queryParam(URL_PARAM, videoDTO.videoLink)
            .toUriString()

        val responseBody = ProcessingVideoResponse(
            message = "Video is being uploaded",
            uploadStatusUrl = uploadStatusUrl,
        )


        val flowBuilder = FlowRegistry.getInstance().getFlow(UploadVideoFlow::class.java)
        val flowContext = FlowContext().apply { insertObject(videoDTO) }
        flowBuilder.initAndRun(
            flowContext = flowContext,
            wait = false,
        )

        return ResponseEntity(responseBody, HttpStatus.ACCEPTED)
    }

    @GetMapping(STATUS_ENDPOINT)
    fun uploadStatus(@RequestParam(URL_PARAM) url: String): ResponseEntity<ProcessingVideoResponse> {
        val responseBody = ProcessingVideoResponse(
            uploadStatus = "в разработке",
        )
        // TODO: get status by url
        return ResponseEntity(responseBody, HttpStatus.OK)
    }
}
