package pt.unl.fct.iadi.novaevents.model

import java.util.UUID

class Club(
    val id: Long,
    val name: String,
    val description: String,
    val category: ClubCategory,
) {
    enum class ClubCategory {
        TECHNOLOGY,
        ARTS,
        SPORTS,
        ACADEMIC,
        SOCIAL,
        CULTURAL
    }
}