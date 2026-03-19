package pt.unl.fct.iadi.novaevents.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import pt.unl.fct.iadi.novaevents.model.Event
import java.time.LocalDate

data class EventForm(

    @field:NotBlank(message = "Event name is required")
    val name: String? = null,

    @field:NotNull(message = "Event date is required")
    val date: LocalDate? = null,

    @field:NotNull(message = "Event type is required")
    val type: Event.EventType? = null,

    val location: String? = null,
    val description: String? = null
)