package pt.unl.fct.iadi.novaevents.controller

import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

interface NovaeventsAPI {

    @RequestMapping(
        value = ["/clubs"],
        method = [RequestMethod.GET]
    )
    fun listAllClubs(model: Model): String

    @RequestMapping(
        value = ["/clubs/{id}"],
        method = [RequestMethod.GET]
    )
    fun getClubDetail(@PathVariable id: Long, model: Model): String
}