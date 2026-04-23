package pt.unl.fct.iadi.novaevents.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import pt.unl.fct.iadi.novaevents.repository.ClubRepository
import pt.unl.fct.iadi.novaevents.repository.EventRepository
import pt.unl.fct.iadi.novaevents.service.WeatherService
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TestEventCreationRules {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var clubRepository: ClubRepository

    @Autowired
    lateinit var eventRepository: EventRepository

    @MockBean
    lateinit var weatherService: WeatherService

    @Test
    fun `hiking event requires location`() {
        val hikingClub = clubRepository.findAll().first { it.name == "Hiking & Outdoors Club" }
        val before = eventRepository.count()

        mockMvc.perform(
            post("/clubs/${hikingClub.id}/events")
                .with(user("alice").roles("EDITOR"))
                .with(csrf())
                .param("name", "No Location Hike")
                .param("date", LocalDate.now().plusDays(5).toString())
                .param("type", "SOCIAL")
                .param("location", "")
                .param("description", "desc")
        )
            .andExpect(status().isOk)
            .andExpect(view().name("events/create"))
            .andExpect(model().attributeHasFieldErrors("eventForm", "location"))

        assertEquals(before, eventRepository.count())
    }

    @Test
    fun `hiking event is rejected in rain`() {
        val hikingClub = clubRepository.findAll().first { it.name == "Hiking & Outdoors Club" }
        val before = eventRepository.count()
        given(weatherService.isRaining("Sintra")).willReturn(true)

        mockMvc.perform(
            post("/clubs/${hikingClub.id}/events")
                .with(user("alice").roles("EDITOR"))
                .with(csrf())
                .param("name", "Rainy Hike")
                .param("date", LocalDate.now().plusDays(6).toString())
                .param("type", "SOCIAL")
                .param("location", "Sintra")
                .param("description", "desc")
        )
            .andExpect(status().isOk)
            .andExpect(view().name("events/create"))
            .andExpect(model().attributeHasFieldErrors("eventForm", "location"))

        assertEquals(before, eventRepository.count())
    }
}

