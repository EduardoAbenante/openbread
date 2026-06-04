package com.eab.openbread.web.exception

import com.eab.openbread.domain.exception.DuplicateResourceException
import com.eab.openbread.domain.exception.ResourceNotFoundException
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*

@RestControllerAdvice
class GlobalExceptionHandler(private val messageSource: MessageSource) {

    /**
     * Handles validation errors from @Valid DTOs.
     * Returns a map: { "field": "error message" }
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException, locale: Locale): ResponseEntity<Map<String, Any?>> {
        val errors = ex.bindingResult.allErrors.associate { error ->
            val field = (error as FieldError).field
            field to error.defaultMessage
        }
        val message = messageSource.getMessage("error.validation_failed", null, locale)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf(
                "message" to message,
                "errors" to errors
            ))
    }

    /**
     * Handles cases where a resource does not exist.
     */
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException, locale: Locale): ResponseEntity<Map<String, String?>> {
        val message = try {
            messageSource.getMessage(ex.message ?: "error.user.not_found", null, locale)
        } catch (e: Exception) {
            ex.message
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(mapOf("message" to message))
    }

    /**
     * Handles duplicate resource errors (email, NIF, etc.)
     */
    @ExceptionHandler(DuplicateResourceException::class)
    fun handleDuplicate(ex: DuplicateResourceException, locale: Locale): ResponseEntity<Map<String, String?>> {
        val message = try {
            messageSource.getMessage(ex.message ?: "error.unexpected", null, locale)
        } catch (e: Exception) {
            ex.message
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(mapOf("message" to message))
    }

    /**
     * Handles illegal operations (e.g., user deleting themselves).
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException, locale: Locale): ResponseEntity<Map<String, String?>> {
        val message = try {
            messageSource.getMessage(ex.message ?: "error.unexpected", null, locale)
        } catch (e: Exception) {
            ex.message
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("message" to message))
    }

    /**
     * Fallback for unexpected errors.
     */
    @ExceptionHandler(Exception::class)
    fun handleUnexpected(ex: Exception, locale: Locale): ResponseEntity<Map<String, String?>> {
        ex.printStackTrace() // opcional: log real en fichero
        val message = messageSource.getMessage("error.unexpected", null, locale)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(mapOf("message" to message))
    }
}
