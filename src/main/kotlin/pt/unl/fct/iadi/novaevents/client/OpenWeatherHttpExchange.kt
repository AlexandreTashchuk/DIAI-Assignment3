package pt.unl.fct.iadi.novaevents.client

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange
interface OpenWeatherHttpExchange {

    @GetExchange("/data/2.5/weather")
    fun currentWeather(
        @RequestParam("q") location: String,
        @RequestParam("appid") apiKey: String
    ): OpenWeatherResponse
}

