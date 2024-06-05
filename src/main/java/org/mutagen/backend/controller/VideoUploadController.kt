package org.mutagen.backend.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.mutagen.backend.domain.dto.UploadVideoRequest
import org.springframework.hateoas.server.LinkBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/upload/")
@Tag(
    name = "Upload API",
    description = "API для загрузки видео"
)
class VideoUploadController {

    @PostMapping("/api/upload/")
    fun uploadVideo(
        @RequestBody videoDTO: UploadVideoRequest
    ): ResponseEntity<String> {
        val statusUrl = WebMvcLinkBuilder
            .linkTo(
                WebMvcLinkBuilder
                    .methodOn(VideoUploadController::class.java)
                    .uploadStatus()
            )
            .toUri()
            .toString()

        return ResponseEntity(
            "Video is being uploaded. ",
            HttpStatus.ACCEPTED
        )
    }

    @GetMapping("/api/upload/status")
    fun uploadStatus(/* TODO: uuid hash? */): ResponseEntity<String> {
        return ResponseEntity("Status of video upload", HttpStatus.OK)
    }
}