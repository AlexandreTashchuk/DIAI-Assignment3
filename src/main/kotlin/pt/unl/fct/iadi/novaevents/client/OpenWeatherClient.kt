package pt.unl.fct.iadi.novaevents.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.invoker.createClient

@Component
class OpenWeatherClient(
    restClientBuilder: RestClient.Builder,
    @param:Value("\${weather.api.key}") private val apiKey: String
) : WeatherClient {

    private val weatherApi: OpenWeatherHttpExchange = HttpServiceProxyFactory
        .builderFor(
            RestClientAdapter.create(
                restClientBuilder
                    .baseUrl("https://api.openweathermap.org")
                    .build()
            )
        )
        .build()
        .createClient<OpenWeatherHttpExchange>()

    override fun getCurrentWeather(location: String): OpenWeatherResponse? {
        if (location.isBlank()) {
            return null
        }

        return runCatching {
            weatherApi.currentWeather(location, apiKey)
        }.getOrNull()
    }
}

