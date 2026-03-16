package pt.unl.fct.iadi.novaevents.controller

import org.springframework.http.HttpStatus
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import pt.unl.fct.iadi.novaevents.service.ClubNotFoundException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ClubNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleClubNotFound(
        ex: ClubNotFoundException,
        model: Model
    ): String {

        model.addAttribute("message", ex.message)

        return "error/404"
    }
}