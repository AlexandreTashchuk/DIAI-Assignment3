package pt.unl.fct.iadi.novaevents.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
class Club(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var name: String = "",

    @Column(length = 2000)
    var description: String = "",

    @Enumerated(EnumType.STRING)
    var category: ClubCategory = ClubCategory.TECHNOLOGY

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