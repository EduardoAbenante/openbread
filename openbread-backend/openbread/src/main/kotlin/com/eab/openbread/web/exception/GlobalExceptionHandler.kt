package com.eab.openbread.web.exception

import com.eab.openbread.domain.exception.DuplicateResourceException
import com.eab.openbread.domain.exception.ResourceNotFoundException
import com.eab.openbread.web.dto.exception.ErrorResponseDTO
import com.eab.openbread.web.dto.exception.ValidationErrorResponseDTO
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
     * Returns a structured ValidationErrorResponseDto.
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException, locale: Locale): ResponseEntity<ValidationErrorResponseDTO> {
        val errors = ex.bindingResult.allErrors.associate { error ->
            val field = (error as FieldError).field
            field to error.defaultMessage
        }
        val message = messageSource.getMessage("error.validation_failed", null, locale)

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ValidationErrorResponseDTO(message = message, errors = errors))
    }

    /**
     * Handles cases where a resource does not exist.
     */
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException, locale: Locale): ResponseEntity<ErrorResponseDTO> {
        val message = try {
            messageSource.getMessage(ex.message ?: "error.user.not_found", null, locale)
        } catch (e: Exception) {
            ex.message
        }

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponseDTO(message = message))
    }

    /**
     * Handles duplicate resource errors (email, NIF, etc.)
     */
    @ExceptionHandler(DuplicateResourceException::class)
    fun handleDuplicate(ex: DuplicateResourceException, locale: Locale): ResponseEntity<ErrorResponseDTO> {
        val message = try {
            messageSource.getMessage(ex.message ?: "error.unexpected", null, locale)
        } catch (e: Exception) {
            ex.message
        }

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponseDTO(message = message))
    }

    /**
     * Handles illegal operations (e.g., user deleting themselves).
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException, locale: Locale): ResponseEntity<ErrorResponseDTO> {
        val message = try {
            messageSource.getMessage(ex.message ?: "error.unexpected", null, locale)
        } catch (e: Exception) {
            ex.message
        }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponseDTO(message = message))
    }

    /**
     * Fallback for unexpected errors.
     */
    @ExceptionHandler(Exception::class)
    fun handleUnexpected(ex: Exception, locale: Locale): ResponseEntity<ErrorResponseDTO> {
        ex.printStackTrace()
        val message = messageSource.getMessage("error.unexpected", null, locale)

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponseDTO(message = message))
    }
}