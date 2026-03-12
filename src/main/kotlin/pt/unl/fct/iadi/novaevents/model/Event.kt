package pt.unl.fct.iadi.novaevents.model

import java.time.LocalDate

class Event(
    val id: Long,
    val clubId: Long,
    val name: String,
    val date: LocalDate,
    val location: String,
    val type: EventType,
    val description: String,
) {
    enum class EventType {
        WORKSHOP,
        TALK,
        COMPETITION,
        SOCIAL,
        MEETING,
        OTHER
    }
}