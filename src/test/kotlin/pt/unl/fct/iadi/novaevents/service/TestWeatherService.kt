package pt.unl.fct.iadi.novaevents.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pt.unl.fct.iadi.novaevents.client.OpenWeatherCondition
import pt.unl.fct.iadi.novaevents.client.OpenWeatherResponse
import pt.unl.fct.iadi.novaevents.client.WeatherClient

class TestWeatherService {

    @Test
    fun `returns true when weather main is Rain`() {
        val service = WeatherService(FakeWeatherClient(OpenWeatherResponse(listOf(OpenWeatherCondition(main = "Rain")))))

        assertEquals(true, service.isRaining("Lisbon"))
    }

    @Test
    fun `returns false when weather main is Clear`() {
        val service = WeatherService(FakeWeatherClient(OpenWeatherResponse(listOf(OpenWeatherCondition(main = "Clear")))))

        assertEquals(false, service.isRaining("Lisbon"))
    }

    @Test
    fun `returns null when no weather data is available`() {
        val service = WeatherService(FakeWeatherClient(OpenWeatherResponse(emptyList())))

        assertEquals(null, service.isRaining("Lisbon"))
    }

    @Test
    fun `returns null for blank location`() {
        val service = WeatherService(FakeWeatherClient(OpenWeatherResponse(listOf(OpenWeatherCondition(main = "Rain")))))

        assertEquals(null, service.isRaining("   "))
    }

    private class FakeWeatherClient(private val response: OpenWeatherResponse?) : WeatherClient {
        override fun getCurrentWeather(location: String): OpenWeatherResponse? = response
    }
}

