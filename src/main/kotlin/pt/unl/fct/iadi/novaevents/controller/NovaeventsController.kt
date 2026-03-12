package pt.unl.fct.iadi.novaevents.controller

import org.springframework.stereotype.Controller
import pt.unl.fct.iadi.novaevents.service.NovaeventsService

@Controller
class NovaeventsController (val service: NovaeventsService) {
}