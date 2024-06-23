package org.mutagen.backend.controller

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.mutagen.backend.controller.ProcessingController.Companion.UPLOAD_ENDPOINT
import org.mutagen.backend.controller.TestController.Companion.TEST_PATH
import org.mutagen.backend.domain.enums.UploadStatus
import org.mutagen.backend.domain.model.*
import org.mutagen.backend.flow.QualityTestFlow
import org.mutagen.backend.flow.UploadVideoFlow
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mephi.sno.libs.flow.belly.FlowContext
import ru.mephi.sno.libs.flow.registry.FlowRegistry

@RestController
@RequestMapping(TEST_PATH)
@Tag(
    name = "Test API",
    description = "API для тестирования поисковых стратегий"
)
class TestController {

    companion object {
        const val TEST_PATH = "/api/v1/test/"
        const val QUALITY_TEST_ENDPOINT = "quality_test"
        const val SPEED_TEST_ENDPOINT = "speed_test"
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Обработка успешно начата"),
        ]
    )
    @PostMapping("/$QUALITY_TEST_ENDPOINT")
    fun uploadVideo(
        @RequestBody qualityTestRequest: QualityTestRequest
    ): ResponseEntity<QualityTestResponse?> {
        val flowBuilder = FlowRegistry.getInstance().getFlow(QualityTestFlow::class.java)

        val flowContext = FlowContext().apply {
            insertObject(qualityTestRequest)
        }
        flowBuilder.initAndRun(
            flowContext = flowContext,
            wait = true,
        )


        return ResponseEntity(null, HttpStatus.OK)
    }
}