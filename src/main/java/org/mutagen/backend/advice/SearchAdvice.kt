package org.mutagen.backend.advice

import org.mutagen.backend.domain.model.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class SearchAdvice {

    companion object {
        class SearchControllerException(msg: String) : RuntimeException(msg)
    }

    @ExceptionHandler(SearchControllerException::class)
    fun handleException(e: SearchControllerException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(e.message)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}