package community.flock.examples.axon.webshop.app.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(AppException::class)
    fun handleException(e: AppException) = e.handle()

    private fun AppException.handle() =
        ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(message)
}
