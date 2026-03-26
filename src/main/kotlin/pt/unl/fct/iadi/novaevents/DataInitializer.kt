package pt.unl.fct.iadi.novaevents

import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.repository.ClubRepository
import pt.unl.fct.iadi.novaevents.repository.EventRepository
import java.time.LocalDate

@Component
class DataInitializer(
    private val clubRepository: ClubRepository,
    private val eventRepository: EventRepository
) : ApplicationRunner {

    override fun run(args: org.springframework.boot.ApplicationArguments?) {

        // Prevent duplicate inserts
        if (clubRepository.count() > 0) return

        // 1. Create Clubs
        val chessClub = Club(name = "Chess Club", description = "A community for players ...", category = Club.ClubCategory.SPORTS)
        val roboticsClub = Club(name = "Robotics Club", description = "The Robotics Club is the place ...", category = Club.ClubCategory.TECHNOLOGY)
        val photographyClub = Club(name = "Photography Club", description = "A space for photography enthusiasts ...", category = Club.ClubCategory.SOCIAL)
        val hikingClub = Club(name = "Hiking & Outdoors Club", description = "Focused on outdoor activities ...", category = Club.ClubCategory.SOCIAL)
        val filmSociety = Club(name = "Film Society", description = "A club for cinema enthusiasts ...", category = Club.ClubCategory.CULTURAL)
        val clubs = listOf(chessClub, roboticsClub, photographyClub, hikingClub, filmSociety)
        clubRepository.saveAll(clubs)

        // 2. Create Events (at least one per club)
        val events = listOf(
            Event(name = "Beginner's Chess Workshop", date = LocalDate.now().plusDays(7), location = "Room A101", type = Event.EventType.WORKSHOP, description = "Introduction to chess basics"),
            Event(name = "Spring Chess Tournament", date = LocalDate.now().plusDays(13), location = "Main Hall", type = Event.EventType.COMPETITION, description = "University open spring chess tournament"),
            Event(name = "Robotics Workshop", date = LocalDate.now().plusDays(10), location = "Lab 1", type = Event.EventType.WORKSHOP, description = "Build a robot"),
            Event(name = "Photo Walk", date = LocalDate.now().plusDays(3), location = "City Center", type = Event.EventType.SOCIAL, description = "Outdoor photography"),
            Event(name = "Mountain Hike", date = LocalDate.now().plusDays(7), location = "Sintra", type = Event.EventType.SOCIAL, description = "Day hike"),
            Event(name = "Film Screening", date = LocalDate.now().plusDays(2), location = "Auditorium", type = Event.EventType.MEETING, description = "Classic movie night")
            // Add more events to reach 15
        )
        eventRepository.saveAll(events)
    }
}