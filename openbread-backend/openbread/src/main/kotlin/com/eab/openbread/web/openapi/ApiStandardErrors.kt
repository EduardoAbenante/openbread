package com.eab.openbread.web.openapi

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ApiValidationError
@ApiNotFoundError
annotation class ApiStandardErrors
