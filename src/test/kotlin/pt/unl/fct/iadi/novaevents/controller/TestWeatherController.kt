package pt.unl.fct.iadi.novaevents.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.mockito.BDDMockito.given
import pt.unl.fct.iadi.novaevents.service.WeatherService

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TestWeatherController {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var weatherService: WeatherService

    @Test
    fun `returns JSON weather for authenticated API request`() {
        given(weatherService.isRaining("Sintra")).willReturn(false)

        mockMvc.perform(
            get("/api/weather")
                .param("location", "Sintra")
                .accept(MediaType.APPLICATION_JSON)
                .with(user("alice").roles("EDITOR"))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.raining").value(false))
    }

    @Test
    fun `returns HTML fragment weather for authenticated API request`() {
        given(weatherService.isRaining("Sintra")).willReturn(true)

        mockMvc.perform(
            get("/api/weather")
                .param("location", "Sintra")
                .accept(MediaType.TEXT_HTML)
                .with(user("alice").roles("EDITOR"))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Raining")))
    }

    @Test
    fun `returns JSON unauthorized for anonymous API request`() {
        mockMvc.perform(
            get("/api/weather")
                .param("location", "Sintra")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Unauthorized")))
    }
}

