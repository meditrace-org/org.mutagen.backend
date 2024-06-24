package org.mutagen.backend.advice

import org.mutagen.backend.domain.model.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class QualityTestAdvice {

    companion object {
        class QualityTestException(msg: String) : RuntimeException(msg)
    }

    @ExceptionHandler(QualityTestException::class)
    fun handleException(e: QualityTestException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(e.message)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }
}