package pt.unl.fct.iadi.novaevents.service

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import pt.unl.fct.iadi.novaevents.client.OpenWeatherCondition
import pt.unl.fct.iadi.novaevents.client.OpenWeatherResponse
import pt.unl.fct.iadi.novaevents.client.WeatherClient
import pt.unl.fct.iadi.novaevents.controller.dto.EventForm
import pt.unl.fct.iadi.novaevents.model.AppUser
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.repository.AppUserRepository
import pt.unl.fct.iadi.novaevents.repository.ClubRepository
import pt.unl.fct.iadi.novaevents.repository.EventRepository
import java.time.LocalDate
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class TestNovaeventsService {

    @Mock
    lateinit var appUserRepository: AppUserRepository

    @Mock
    lateinit var clubRepository: ClubRepository

    @Mock
    lateinit var eventRepository: EventRepository

    @AfterEach
    fun clearSecurityContext() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `rejects blank location for hiking club`() {
        val weatherService = WeatherService(FakeWeatherClient(null))
        val service = NovaeventsService(appUserRepository, clubRepository, eventRepository, weatherService)

        val club = Club(id = 10, name = "Hiking & Outdoors Club")
        `when`(clubRepository.findById(10)).thenReturn(Optional.of(club))

        val form = EventForm(
            name = "Trail Day",
            date = LocalDate.now().plusDays(1),
            type = Event.EventType.SOCIAL,
            location = "   ",
            description = "Morning hike"
        )

        assertThrows(OutdoorEventLocationRequiredException::class.java) {
            service.createEvent(10, form)
        }

        verify(eventRepository, never()).save(any(Event::class.java))
    }

    @Test
    fun `rejects event creation when hiking location is raining`() {
        val weatherService = WeatherService(FakeWeatherClient(OpenWeatherResponse(listOf(OpenWeatherCondition(main = "Rain")))))
        val service = NovaeventsService(appUserRepository, clubRepository, eventRepository, weatherService)

        val club = Club(id = 10, name = "Hiking & Outdoors Club")
        `when`(clubRepository.findById(10)).thenReturn(Optional.of(club))

        val form = EventForm(
            name = "Trail Day",
            date = LocalDate.now().plusDays(1),
            type = Event.EventType.SOCIAL,
            location = "Sintra",
            description = "Morning hike"
        )

        assertThrows(OutdoorEventBadWeatherException::class.java) {
            service.createEvent(10, form)
        }

        verify(eventRepository, never()).save(any(Event::class.java))
    }

    @Test
    fun `allows non outdoor club with blank location`() {
        val weatherService = WeatherService(FakeWeatherClient(OpenWeatherResponse(listOf(OpenWeatherCondition(main = "Rain")))))
        val service = NovaeventsService(appUserRepository, clubRepository, eventRepository, weatherService)

        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken("alice", "n/a", emptyList())

        val club = Club(id = 20, name = "Chess Club")
        `when`(clubRepository.findById(20)).thenReturn(Optional.of(club))
        `when`(eventRepository.existsByNameIgnoreCase("Weekly Match")).thenReturn(false)
        `when`(appUserRepository.findByUsername("alice")).thenReturn(AppUser(id = 1, username = "alice", password = "secret"))
        `when`(eventRepository.save(any(Event::class.java))).thenAnswer { it.arguments[0] as Event }

        val form = EventForm(
            name = "Weekly Match",
            date = LocalDate.now().plusDays(1),
            type = Event.EventType.MEETING,
            location = "   ",
            description = "Rounds"
        )

        val saved = service.createEvent(20, form)

        assertEquals("", saved.location)
        verify(eventRepository).save(any(Event::class.java))
    }

    private class FakeWeatherClient(private val response: OpenWeatherResponse?) : WeatherClient {
        override fun getCurrentWeather(location: String): OpenWeatherResponse? = response
    }
}

